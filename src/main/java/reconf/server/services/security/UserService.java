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
import org.springframework.security.core.authority.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.provisioning.*;
import org.springframework.transaction.annotation.*;
import org.springframework.web.bind.annotation.*;
import reconf.server.*;
import reconf.server.domain.*;
import reconf.server.domain.security.*;

@RestController
@RequestMapping(value=ReConfConstants.CRUD_ROOT,
    produces=ReConfConstants.MT_APPLICATION_JSON,
    consumes={ReConfConstants.MT_TEXT_PLAIN, ReConfConstants.MT_ALL, ReConfConstants.MT_APPLICATION_JSON})
public class UserService {

    @Autowired JdbcUserDetailsManager userDetailsManager;

    @RequestMapping(value="/user", method=RequestMethod.PUT)
    @Transactional
    public ResponseEntity<Client> doIt(@RequestBody Client client) {

        List<String> errors = DomainValidator.checkForErrors(client);
        if (!errors.isEmpty()) {
            return new ResponseEntity<Client>(new Client(client, errors), HttpStatus.BAD_REQUEST);
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("USER"));

        HttpStatus status = null;
        User user = new User(client.getUsername(), client.getPassword(), authorities);
        if (userDetailsManager.userExists(client.getUsername())) {
            userDetailsManager.updateUser(user);
            status = HttpStatus.OK;
        } else {
            userDetailsManager.createUser(user);
            status = HttpStatus.CREATED;
        }

        return new ResponseEntity<Client>(new Client(client), status);
    }
}
