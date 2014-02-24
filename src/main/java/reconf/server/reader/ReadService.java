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

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import reconf.server.domain.Component;
import reconf.server.domain.Property;

@Path(ReadService.ROOT)
@Consumes({"application/json"})
@Produces({"application/json"})
public interface ReadService {

    final String ROOT = "/";
     
    final String PROD_COMP_PROP = "product/{productName}/component/{componentName}/property/{propertyName}";    
    final String PROD_COMP = "product/{productName}/component/{componentName}";

    //@AddLinks
    //@LinkResource
    
    @GET
    @Path(PROD_COMP)
    Component getComponent(@PathParam("productName") String product, 
    		@PathParam("componentName") String component);
    
    @GET
    @Path(PROD_COMP_PROP)
    Property getProperty(@PathParam("productName") String product, 
    		@PathParam("componentName") String component, 
    		@PathParam("propertyName") String property);

    @PUT
    @Path(PROD_COMP_PROP)
    public void putProperty(@PathParam("productName") String product, 
			@PathParam("componentName") String component, 
			@PathParam("propertyName") String propertyName,
    		Property property);

    /*
    @GET
    @Path(PROD_COMP)
    Response getComponent(@Form ReadOperation op, @Form ReadRequest req);
    */
}
