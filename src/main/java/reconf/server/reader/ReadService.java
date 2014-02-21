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
import org.jboss.resteasy.annotations.*;
import reconf.server.auditing.*;

@Path(ReadService.ROOT)
public interface ReadService {

    final String ROOT = "/";
    final String PROD_COMP_PROP = "{product}/{component}/{configuration}";
    final String PROD_COMP = "{product}/{component}";

    @GET
    @Path(PROD_COMP_PROP)
    @GZIP
    Response getConfiguration(@Form ReadOperation op, @Form ReadRequest req);

    @PUT
    @Path(PROD_COMP_PROP)
    @GZIP
    Response putConfiguration(@Form ReadOperation op, @Form ReadRequest req);

    @GET
    @Path(PROD_COMP)
    @GZIP
    Response getComponent(@Form ReadOperation op, @Form ReadRequest req);
}
