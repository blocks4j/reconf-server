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
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.access.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.*;
import org.springframework.security.provisioning.*;
import org.springframework.security.web.*;
import org.springframework.stereotype.Component;
import org.springframework.util.*;
import reconf.server.*;
import reconf.server.domain.*;
import reconf.server.domain.security.*;
import reconf.server.repository.*;

@Component
public class SecurityAccessDecisionManager implements AccessDecisionManager {

    @Autowired UserProductRepository userProducts;
    @Autowired ProductRepository products;
    @Autowired JdbcUserDetailsManager userDetailsManager;

    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        if (!(object instanceof FilterInvocation)) {
            return;
        }
        if (authentication == null) {
            return;
        }
        FilterInvocation filterInvocation = (FilterInvocation) object;

        String url = filterInvocation.getRequestUrl();
        if (url.endsWith("/")) {
            url = StringUtils.substringBeforeLast(url, "/");
        }

        AntPathMatcher antMatcher = new AntPathMatcher();
        if (antMatcher.match("/crud/product", url)) {
            if (userDetailsManager.userExists(authentication.getName())) {
                return;
            }
        }
        if (antMatcher.match("/crud/product/{product}", url)) {
            if (continueToProduct(authentication, antMatcher, "/crud/product/{product}", url)) {
               return;
            }
        }
        if (antMatcher.match("/crud/product/{product}/**", url)) {
            if (continueToProduct(authentication, antMatcher, "/crud/product/{product}/**", url)) {
                return;
             }
        }
        if (antMatcher.match("/crud/user", url)) {
            if (userDetailsManager.userExists(authentication.getName())) {
                return;
            }
        }
        if (antMatcher.match("/crud/user/**", url)) {
            if (ApplicationSecurity.isRoot(authentication)) {
                return;
            }
        }
        throw new AccessDeniedException("Forbidden");
    }

    private boolean continueToProduct(Authentication authentication, AntPathMatcher antMatcher, String pattern, String url) {
        Map<String, String> parameters = antMatcher.extractUriTemplateVariables(pattern, url);
        Product product = new Product(parameters.get("product"));
        return isAuthorized(authentication, product.getName());
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
