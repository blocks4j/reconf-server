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

import java.net.URI;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.UriBuilder;

import org.springframework.stereotype.Controller;

import reconf.server.domain.Component;
import reconf.server.domain.Property;
import reconf.server.persistence.ConfigurationRepository;

@Controller
public class ReadServiceImpl implements ReadService {

    @Override
    public Property getProperty(@PathParam("product") String product, 
    							@PathParam("component") String component, 
    							@PathParam("property") String property) {
        Property result = ConfigurationRepository.DEFAULT.get(product, component, property);

//        if (result == "") {
//            return Response.status(Response.Status.NOT_FOUND).build();
//        }
        
        return result;
        
        //return Response.ok().entity(p).build();
    }

    @Override
    public void putProperty(@PathParam("product") String product, 
			@PathParam("component") String component, 
			@PathParam("property") String propertyName,
    		Property property) {
    	ConfigurationRepository.DEFAULT.upsert(product, component, propertyName, property);
    	
    	URI configURI = UriBuilder.fromPath(ReadService.PROD_COMP_PROP).build(property.getProduct(),property.getComponent(), property.getName());
    	
		//return Response.created(configURI).build();
		
    }

	@Override
	@GET
	@Path("product/{productName}/component/{componentName}/property")
	public Component getComponent(
			@PathParam("productName") String productName,
			@PathParam("componentName") String componentName) {
		
		return ConfigurationRepository.DEFAULT.get(productName, componentName);
	}

    
//    @Override
//    public Response getComponent(@Form ReadOperation op, @Form ReadRequest req) {
//        return null;
//    }

}
