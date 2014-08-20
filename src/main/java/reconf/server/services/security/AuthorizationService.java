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

import org.apache.commons.lang3.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.core.*;
import org.springframework.security.provisioning.*;
import org.springframework.stereotype.*;
import reconf.server.*;
import reconf.server.domain.security.*;
import reconf.server.repository.*;

@Service
public class AuthorizationService {

    @Autowired JdbcUserDetailsManager userDetailsManager;
    @Autowired UserProductRepository userProducts;

    public boolean isAuthorized(Authentication auth, String productId) {
        if (isRoot(auth)) {
            return true;
        }
        return userProducts.exists(new UserProductKey(auth.getName(), productId));
    }

    public boolean userExists(String user) {
        return userDetailsManager.userExists(user);
    }

    public boolean isRoot(Authentication auth) {
        return auth != null && StringUtils.equals(auth.getName(), ApplicationSecurity.SERVER_ROOT_USER);
    }

    public boolean isRoot(String user) {
        return StringUtils.equals(user, ApplicationSecurity.SERVER_ROOT_USER);
    }
}
