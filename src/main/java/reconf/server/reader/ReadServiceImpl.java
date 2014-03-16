/*
 *    Copyright 1996-2013 UOL Inc
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package reconf.server.reader;

import java.lang.annotation.*;
import java.util.*;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.annotation.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import reconf.server.domain.*;
import reconf.server.domain.Relation.Type;
import reconf.server.domain.Component;
import reconf.server.persistence.*;
import reconf.server.rest.*;

@Controller
public class ReadServiceImpl implements ReadService {

    @Autowired @Qualifier("DOMAIN") private String baseURI;



	@Override
	public Component getComponent(
			@PathParam("productName") String productName,
			@PathParam("componentName") String componentName) {

		return ConfigurationRepository.DEFAULT.get(productName, componentName);
	}

	/* V1 */

	@Override
    public Response getPropertiesV1apiV2(String product, String component) {
	    Component comp = ConfigurationRepository.DEFAULT.get(product, component);
        if(comp == null)
        {
            return Response.status(Status.NOT_FOUND).build();
        }
        if(comp.getProperties() == null || comp.getProperties().isEmpty()){
            return Response.noContent().build();
        }
        StringBuilder sb = new StringBuilder("[");
        for (Property prop : comp.getProperties()) {
            sb.append("'").append(prop.getName()).append("'");
            sb.append(",");
            sb.append("'").append(prop.getValue()).append("'");
        }
        sb.append("]");
        return Response.ok(sb.toString()).build();
    }

	@Override
	public Response getPropertyV1(String product, String component, String property) {
        return getPropertyV1s(ReadService.PROD_COMP_PROPv1, product, component, property);
    }
	@Override
    public Response getPropertyV1apiV2(String product, String component, String property) {
	    return getPropertyV1s(ReadService.PROD_COMP_PROP, product, component, property);
    }
	private Response getPropertyV1s(String Path, String product, String component, String property) {
        Property result = ConfigurationRepository.DEFAULT.get(product, component, property);
        if(result == null){
            return Response.status(Status.NOT_FOUND).build();
        }
        ResponseBuilder resp = Response.ok(result.getValue());        ;
        return addHeaderLinks(linksGetProperty(Path, product, component, property), resp).build();
    }


	/* V2 */

	@Override
    public Response getProductsV2() {
        Collection<Product> prods = ConfigurationRepository.DEFAULT.getProducts();
        if(prods == null || prods.isEmpty()){
            return Response.noContent().build();
        }
        for (Product prod : prods) {
            prod.setRelations(linksGetProduct(PROD, prod.getName(), prod));
        }
        return Response.ok(prods).build();
    }

    @Override
    public Response getProductV2(String product) {
        Product prod = ConfigurationRepository.DEFAULT.get(product);
        if(prod == null)
        {
            return Response.status(Status.NOT_FOUND).build();
        }
        for (Component comp : prod.getComponents()) {
            comp.setRelations(linksGetComponent(PROD_COMP, product, comp.getName(), comp));
        }
        prod.setRelations(linksGetProduct(PROD, product, prod));
        return Response.ok(prod).build();
    }
    @Override
    public Response putProductV2(String productName, Product product) {
        Product found = ConfigurationRepository.DEFAULT.get(productName);
        Product result = null;
        try{
            result = ConfigurationRepository.DEFAULT.upsert(productName,product);
        }catch(RuntimeException re){
            return Response.status(Status.BAD_REQUEST).build();
        }
        if (result == null){
            return Response.status(Status.NOT_FOUND).build();
        }
        if(found == null){
            return Response.created(UriBuilder.fromPath(baseURI + PROD).build(productName)).build();
        } else {
            return Response.noContent().build();
        }
    }
    @Override
    public Response deleteProductV2(String product) {
        Product found = ConfigurationRepository.DEFAULT.get(product);
        if (found == null){
            return Response.status(Status.NOT_FOUND).build();
        }
        try{
            if(!ConfigurationRepository.DEFAULT.deleted(product)){
                return Response.status(Status.BAD_REQUEST).build();
            }
        }catch(RuntimeException re){
            Collection<Relation> relations = new ArrayList<>();
            for(Component c:found.getComponents()){
                relations.add(new Relation(Type.REMOVE, UriBuilder.fromPath(baseURI + PROD_COMP).build(product, c.getName())));
            }
            return Response.status(Status.BAD_REQUEST).entity(relations).build();
        }
        return Response.noContent().build();
    }


    @Override
    public Response getComponentsV2(String product) {
	    Product prod = ConfigurationRepository.DEFAULT.get(product);
        if(prod == null)
        {
            return Response.status(Status.NOT_FOUND).build();
        }
        if(prod.getComponents() == null || prod.getComponents().isEmpty()){
            return Response.noContent().build();
        }
        for (Component comp : prod.getComponents()) {
            comp.setRelations(linksGetComponent(PROD_COMP, product, comp.getName(), comp));
        }
        return Response.ok(prod.getComponents()).build();
    }

	@Override
    public Response getComponentV2(String product, String component) {
	    Component comp = ConfigurationRepository.DEFAULT.get(product, component);
        if(comp == null)
        {
            return Response.status(Status.NOT_FOUND).build();
        }
        comp.setRelations(linksGetComponent(ReadService.PROD_COMP, product, component, comp));
        return Response.ok(comp).build();
	}
	@Override
    public Response putComponentV2(String product, String componentName, Component component) {
	    Component found = ConfigurationRepository.DEFAULT.get(product, componentName);
	    Component result = null;
        try{
            result = ConfigurationRepository.DEFAULT.upsert(product,componentName, component);
        }catch(RuntimeException re){
            return Response.status(Status.BAD_REQUEST).build();
        }
        if (result == null){
            return Response.status(Status.NOT_FOUND).build();
        }
        if(found == null){
            return Response.created(UriBuilder.fromPath(baseURI + PROD_COMP).build(product, componentName)).build();
        } else {
            return Response.noContent().build();
        }
    }
	@Override
    public Response deleteComponentV2(String product, String component) {
	    Component found = ConfigurationRepository.DEFAULT.get(product, component);
        if (found == null){
            return Response.status(Status.NOT_FOUND).build();
        }
        try{
            if(!ConfigurationRepository.DEFAULT.deleted(product, component)){
                return Response.status(Status.BAD_REQUEST).build();
            }
        }catch(RuntimeException re){
            if("INVALID".equals(re.getMessage())){
                Collection<Relation> relations = new ArrayList<>();
                for(Property p:found.getProperties()){
                    relations.add(new Relation(Type.REMOVE, UriBuilder.fromPath(baseURI + PROD_COMP_PROP).build(product, component, p.getName())));
                }

                return Response.status(Status.BAD_REQUEST).entity(relations).build();
            }
        }
        return Response.noContent().build();
    }


	@Override
    public Response getPropertiesV2(String product, String component) {
	    Component comp = ConfigurationRepository.DEFAULT.get(product, component);
	    if(comp == null)
	    {
	        return Response.status(Status.NOT_FOUND).build();
	    }
	    if(comp.getProperties() == null || comp.getProperties().isEmpty()){
	        return Response.noContent().build();
	    }
	    for (Property prop : comp.getProperties()) {
	        prop.setRelations(linksGetProperty(ReadService.PROD_COMP_PROP, product, component, prop.getName()));
        }
	    return Response.ok(comp.getProperties()).build();
    }

    @Override
    public Response getPropertyV2(String product, String component, String property) {
        Property result = ConfigurationRepository.DEFAULT.get(product, component, property);
        if(result == null){
            return Response.status(Status.NOT_FOUND).build();
        }
        result.setRelations(linksGetProperty(ReadService.PROD_COMP_PROP, product, component, property));
        return Response.ok(result).build();
    }

    @Override
    public Response putPropertyV2(String product, String component, String propertyName, Property property) {
        Property found = ConfigurationRepository.DEFAULT.get(product, component, propertyName);
        Property result = null;
        try{
            result = ConfigurationRepository.DEFAULT.upsert(product, component, propertyName, property);
        }catch(RuntimeException re){
            return Response.notModified().build();
        }
        if (result == null){
            return Response.status(Status.NOT_FOUND).build();
        }
        if(found == null){
            return Response.created(UriBuilder.fromPath(baseURI + PROD_COMP_PROP).build(product, component, propertyName)).build();
        } else {
            return Response.noContent().build();
        }
    }

    @Override
    public Response deletePropertyV2(String product, String component, String propertyName){
        Property found = ConfigurationRepository.DEFAULT.get(product, component, propertyName);
        if (found == null){
            return Response.status(Status.NOT_FOUND).build();
        }
        if(!ConfigurationRepository.DEFAULT.deleted(product, component, propertyName)){
            return Response.status(Status.BAD_REQUEST).build();
        }
        return Response.noContent().build();
    }


    private Collection<Relation> linksGetProduct(String path, String product, Product prod) {

        Collection<Relation> ret = new ArrayList<>();

        ret.add(new Relation(Type.SELF, UriBuilder.fromPath(baseURI + path).build(product)));
        ret.add(new Relation(Type.UPDATE, UriBuilder.fromPath(baseURI + path).build(product)));
        ret.add(new Relation(Type.REMOVE, UriBuilder.fromPath(baseURI + path).build(product)));

        for(Component c: prod.getComponents()){
            c.setRelations(linksGetComponent(PROD_COMP, product, c.getName(), c));
        }

        return ret;

    }

    private Collection<Relation> linksGetComponent(String Path, String product, String component, Component comp){

        Collection<Relation> ret = new ArrayList<>();

        ret.add(new Relation(Type.SELF, UriBuilder.fromPath(baseURI + Path).build(product, component)));
        ret.add(new Relation(Type.UPDATE, UriBuilder.fromPath(baseURI + Path).build(product, component)));
        ret.add(new Relation(Type.REMOVE, UriBuilder.fromPath(baseURI + Path).build(product, component)));

        for(Property p: comp.getProperties()){
            p.setRelations(linksGetProperty(PROD_COMP_PROP, product, component, p.getName()));
        }

        return ret;
    }

    private Collection<Relation> linksGetProperty(String Path, String product, String component, String property){

        Collection<Relation> ret = new ArrayList<>();

        ret.add(new Relation(Type.SELF, UriBuilder.fromPath(baseURI + Path).build(product, component, property)));
        ret.add(new Relation(Type.UPDATE, UriBuilder.fromPath(baseURI + Path).build(product, component, property)));
        ret.add(new Relation(Type.REMOVE, UriBuilder.fromPath(baseURI + Path).build(product, component, property)));

        return ret;
    }


    private ResponseBuilder addHeaderLinks(Collection<Relation> relations, ResponseBuilder resp) {
        for (Relation rel : relations) {
            resp.header(HttpLinkHeader.getHeaderName(), new HttpLinkHeader(rel.getURI(), rel.getType().toString()).toString());
        }
        return resp;
    }


}
