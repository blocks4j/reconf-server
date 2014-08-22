/*
 *    Copyright 2013-2014 ReConf Team
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
package reconf.server.services.security;

import java.util.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.security.core.*;
import org.springframework.security.provisioning.*;
import org.springframework.transaction.annotation.*;
import org.springframework.web.bind.annotation.*;
import reconf.server.*;
import reconf.server.domain.security.*;
import reconf.server.repository.*;

@RestController
@RequestMapping(value=ReConfConstants.CRUD_ROOT,
    produces=ReConfConstants.MT_APPLICATION_JSON,
    consumes={ReConfConstants.MT_TEXT_PLAIN, ReConfConstants.MT_ALL, ReConfConstants.MT_APPLICATION_JSON})
public class DeleteUserService {

    @Autowired JdbcUserDetailsManager userDetailsManager;
    @Autowired UserProductRepository userProducts;

    private static final List<String> cannotDeleteRoot = Collections.singletonList("cannot delete reconf user");
    private static final List<String> userNotFound = Collections.singletonList("user not found");

    @RequestMapping(value="/user/{user}", method=RequestMethod.DELETE)
    @Transactional
    public ResponseEntity<Client> doIt(@PathVariable("user") String user,
            Authentication authentication) {

        if (ApplicationSecurity.isRoot(user)) {
            return new ResponseEntity<Client>(new Client(user, cannotDeleteRoot), HttpStatus.BAD_REQUEST);
        }

        if (!userDetailsManager.userExists(user)) {
            return new ResponseEntity<Client>(new Client(user, userNotFound), HttpStatus.NOT_FOUND);
        }

        userProducts.deleteByKeyUsername(user);
        userDetailsManager.deleteUser(user);
        return new ResponseEntity<Client>(HttpStatus.OK);
    }
}
