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
import reconf.server.domain.*;

@Path(ReadService.ROOT)
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN, "application/vnd.reconf-v1+text", "application/vnd.reconf-v2+json"})
@Produces({"application/vnd.reconf-v1+text", "application/vnd.reconf-v2+json"})
public interface ReadService {

    final String ROOT = "/";

    final String PROD = "product/{productName}";
    final String PROD_COMP = PROD + "/component/{componentName}";
    final String PROD_COMP_PROPS = PROD_COMP + "/property";
    final String PROD_COMP_PROP = PROD_COMP_PROPS + "/{propertyName}";


    final String PRODv1 = "{productName}";
    final String PROD_COMPv1 = PRODv1 + "/{componentName}";
    final String PROD_COMP_PROPv1 = PROD_COMPv1 + "/{propertyName}";



    @GET
    @Path(PROD_COMP)
    Component getComponent(@PathParam("productName") String product,
    		@PathParam("componentName") String component);

    /* V1 */


    /*/property */

    @GET
    @Path(PROD_COMP_PROPS)
    @Consumes({MediaType.TEXT_PLAIN, "application/vnd.reconf-v1+text"})
    @Produces({"application/vnd.reconf-v1+text"})
    Response getPropertiesV1apiV2(@PathParam("productName") String product, @PathParam("componentName") String component);


    /*/property/{propertyName} */

    @GET
    @Path(PROD_COMP_PROPv1)
    @Consumes({MediaType.TEXT_PLAIN, "application/vnd.reconf-v1+text"})
    @Produces({"application/vnd.reconf-v1+text"})
    Response getPropertyV1(@PathParam("productName") String product, @PathParam("componentName") String component, @PathParam("propertyName") String property);

    @GET
    @Path(PROD_COMP_PROP)
    @Consumes({MediaType.TEXT_PLAIN, "application/vnd.reconf-v1+text"})
    @Produces({"application/vnd.reconf-v1+text"})
    Response getPropertyV1apiV2(@PathParam("productName") String product, @PathParam("componentName") String component, @PathParam("propertyName") String property);

    /*
    @DELETE
    @Path(PROD_COMP_PROPv1)
    @Consumes({MediaType.TEXT_PLAIN, "application/vnd.reconf-v1+text"})
    @Produces({"application/vnd.reconf-v1+text"})
    Response deletePropertyV1(@PathParam("productName") String product, @PathParam("componentName") String component, @PathParam("propertyName") String property);

    @DELETE
    @Path(PROD_COMP_PROPv1)
    @Consumes({MediaType.TEXT_PLAIN, "application/vnd.reconf-v1+text"})
    @Produces({"application/vnd.reconf-v1+text"})
    Response deletePropertyV1apiV2(@PathParam("productName") String product, @PathParam("componentName") String component, @PathParam("propertyName") String property);
    */


    /* V2 */


    /*/property */

    @GET
    @Path(PROD_COMP_PROPS)
    @Consumes({MediaType.APPLICATION_JSON, "application/vnd.reconf-v2+json"})
    @Produces({"application/vnd.reconf-v2+json"})
    Response getPropertiesV2(@PathParam("productName") String product, @PathParam("componentName") String component);



    /*/property/{propertyName} */

    @GET
    @Path(PROD_COMP_PROP)
    @Consumes({MediaType.APPLICATION_JSON, "application/vnd.reconf-v2+json"})
    @Produces({"application/vnd.reconf-v2+json"})
    Response getPropertyV2(@PathParam("productName") String product, @PathParam("componentName") String component, @PathParam("propertyName") String property);

    @PUT
    @Path(PROD_COMP_PROP)
    @Consumes({MediaType.APPLICATION_JSON, "application/vnd.reconf-v2+json"})
    @Produces({"application/vnd.reconf-v2+json"})
    Response putPropertyV2(@PathParam("productName") String product, @PathParam("componentName") String component, @PathParam("propertyName") String propertyName, Property property);


    @DELETE
    @Path(PROD_COMP_PROP)
    @Consumes({MediaType.APPLICATION_JSON, "application/vnd.reconf-v2+json"})
    @Produces({"application/vnd.reconf-v2+json"})
    Response deletePropertyV2(@PathParam("productName") String product, @PathParam("componentName") String component, @PathParam("propertyName") String property);


//    @PUT
//    @Path(PROD_COMP_PROP)
//    public void putProperty(@PathParam("productName") String product,
//			@PathParam("componentName") String component,
//			@PathParam("propertyName") String propertyName,
//    		Property property);

    /*
    @GET
    @Path(PROD_COMP)
    Response getComponent(@Form ReadOperation op, @Form ReadRequest req);
    */
}
