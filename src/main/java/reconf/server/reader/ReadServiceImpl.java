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

import javax.ws.rs.core.*;
import org.jboss.resteasy.annotations.*;
import org.springframework.stereotype.*;
import reconf.server.auditing.*;
import reconf.server.persistence.*;

@Controller
public class ReadServiceImpl implements ReadService {

    @Override
    public Response getConfiguration(ReadOperation op, ReadRequest req) {
        String result = ConfigurationRepository.DEFAULT.get(req.getProduct(), req.getComponent(), req.getConfiguration());

        if (result == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok().entity(result).build();
    }

    @Override
    public Response putConfiguration(@Form ReadOperation op, @Form ReadRequest req) {
        return null;
    }

    @Override
    public Response getComponent(@Form ReadOperation op, @Form ReadRequest req) {
        return null;
    }

}
