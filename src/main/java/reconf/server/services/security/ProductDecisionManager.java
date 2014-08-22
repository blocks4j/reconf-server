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
import java.util.regex.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.access.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.*;
import org.springframework.security.web.*;
import org.springframework.stereotype.Component;
import reconf.server.*;
import reconf.server.domain.*;
import reconf.server.domain.security.*;
import reconf.server.repository.*;

@Component
public class ProductDecisionManager implements AccessDecisionManager {

    @Autowired UserProductRepository userProducts;
    @Autowired ProductRepository products;
    private final Pattern crudProductPattern = Pattern.compile(ReConfConstants.CRUD_ROOT + "/product/(\\w+).*");
    private final Pattern crudUserPattern = Pattern.compile(ReConfConstants.CRUD_ROOT + "/user[/]?");

    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        if (!(object instanceof FilterInvocation)) {
            return;
        }
        if (authentication == null) {
            return;
        }
        FilterInvocation filterInvocation = (FilterInvocation) object;

        Matcher matcher = crudProductPattern.matcher(filterInvocation.getRequestUrl());
        if (matcher.matches()) {
            Product product = new Product(matcher.group(1));
            if (isAuthorized(authentication, product.getName())) {
                return;
            }
            throw new AccessDeniedException("Forbidden");
        }

        matcher = crudUserPattern.matcher(filterInvocation.getRequestUrl());
        if (matcher.matches()) {
            if (ApplicationSecurity.isRoot(authentication)) {
                return;
            }
        }
        throw new AccessDeniedException("Forbidden");
    }

    private boolean isAuthorized(Authentication auth, String productId) {
        if (ApplicationSecurity.isRoot(auth)) {
            return true;
        }
        if (!products.exists(productId)) {
            return true;
        }
        return userProducts.exists(new UserProductKey(auth.getName(), productId));
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

}
