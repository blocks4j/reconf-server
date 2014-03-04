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

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.ResponseBuilder;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import reconf.server.domain.*;
import reconf.server.domain.Component;
import reconf.server.persistence.*;
import reconf.server.rest.*;

@Controller
public class ReadServiceImpl implements ReadService {

    @Autowired @Qualifier("DOMAIN") private String baseURI;

    @Override
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

    public Response getPropertyV1(String product, String component, String property) {
        Property result = ConfigurationRepository.DEFAULT.get(product, component, property);
        ResponseBuilder resp = Response.ok(result.getValue());
        resp.header(HttpLinkHeader.getHeaderName(), new HttpLinkHeader(UriBuilder.fromPath(baseURI + ReadService.PROD_COMP_PROP).build(product, component, property), "self").toString());
        resp.header(HttpLinkHeader.getHeaderName(), new HttpLinkHeader(UriBuilder.fromPath(baseURI + ReadService.PROD_COMP_PROP).build(product, component, property), "update").toString());
        return resp.build();
    }

    public Response getPropertyV2(String product, String component, String property) {
        Property result = ConfigurationRepository.DEFAULT.get(product, component, property);
        result.getRest().addLink(UriBuilder.fromPath(baseURI + ReadService.PROD_COMP_PROP).build(product, component, property), "self");
        result.getRest().addLink(UriBuilder.fromPath(baseURI + ReadService.PROD_COMP_PROP).build(product, component, property), "update");
        result.getRest().addLink(UriBuilder.fromPath(baseURI + ReadService.PROD_COMP_PROP).build(product, component, property), "remove");
        return Response.ok(result).build();
    }
}
