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

import java.util.*;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
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

    //@Override
    public void putProperty(String product, String component, String propertyName, Property property) {
    	ConfigurationRepository.DEFAULT.upsert(product, component, propertyName, property);
    	//UriBuilder.fromPath(ReadService.PROD_COMP_PROP).build(property.getProduct(),property.getComponent(), property.getName());

		//return Response.created(configURI).build();

    }

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
        Property result = ConfigurationRepository.DEFAULT.upsert(product, component, propertyName, property);
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


    private Collection<Relation> linksGetProperty(String Path, String product, String component, String property){

        Collection<Relation> ret = new ArrayList<>();

        ret.add(new Relation(Type.SELF, UriBuilder.fromPath(baseURI + Path).build(product, component, property)));
        ret.add(new Relation(Type.UPDATE, UriBuilder.fromPath(baseURI + Path).build(product, component, property)));

        return ret;
    }


    private ResponseBuilder addHeaderLinks(Collection<Relation> relations, ResponseBuilder resp) {
        for (Relation rel : relations) {
            resp.header(HttpLinkHeader.getHeaderName(), new HttpLinkHeader(rel.getURI(), rel.getType().toString()).toString());
        }
        return resp;
    }


}
