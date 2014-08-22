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
package reconf.server;

import java.util.*;
import org.apache.commons.lang3.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.authentication.builders.*;
import org.springframework.security.config.annotation.web.builders.*;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.config.http.*;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.provisioning.*;
import reconf.server.services.security.*;

@Configuration
@EnableWebSecurity
// http://justinrodenbostel.com/2014/05/30/part-5-integrating-spring-security-with-spring-boot-web/
public class ApplicationSecurity extends WebSecurityConfigurerAdapter {

    @Autowired JdbcUserDetailsManager userDetailsManager;
    @Autowired SecurityAccessDecisionManager decisionManager;
    @Value("${reconf.user.password}") String rootUserPassword;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests()
        .antMatchers("/crud/**").fullyAuthenticated()
        .accessDecisionManager(decisionManager)
        .and().httpBasic();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsManager);
        auth.jdbcAuthentication().dataSource(userDetailsManager.getDataSource());

        if (userDetailsManager.userExists(ReConfConstants.SERVER_ROOT_USER)) {
            userDetailsManager.deleteUser(ReConfConstants.SERVER_ROOT_USER);
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROOT"));
        authorities.add(new SimpleGrantedAuthority("USER"));
        User user = new User(ReConfConstants.SERVER_ROOT_USER, rootUserPassword, authorities);
        userDetailsManager.createUser(user);
    }

    public static boolean isRoot(Authentication auth) {
        return auth != null && StringUtils.equals(auth.getName(), ReConfConstants.SERVER_ROOT_USER);
    }

    public static boolean isRoot(String user) {
        return StringUtils.equals(user, ReConfConstants.SERVER_ROOT_USER);
    }

    public boolean userExists(String user) {
        return userDetailsManager.userExists(user);
    }
}
