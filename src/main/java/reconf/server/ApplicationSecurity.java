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
import org.flywaydb.core.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.core.*;
import org.springframework.core.annotation.*;
import org.springframework.security.config.annotation.authentication.builders.*;
import org.springframework.security.config.annotation.web.builders.*;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.config.http.*;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.provisioning.*;

@Configuration
@EnableWebSecurity
@Order(Ordered.LOWEST_PRECEDENCE - 1000)
// http://justinrodenbostel.com/2014/05/30/part-5-integrating-spring-security-with-spring-boot-web/
public class ApplicationSecurity extends WebSecurityConfigurerAdapter {

    public static final String SERVER_ROOT_USER = "reconf";

    @Autowired JdbcUserDetailsManager userDetailsManager;
    @Autowired @Qualifier("rootUserPassword") String rootUserPassword;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests().antMatchers("/crud/product/**").hasRole("USER").and().httpBasic();
        http.authorizeRequests().antMatchers("/crud/user").hasRole("ROOT").and().httpBasic();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        //createTableUsers();
        //createTableAuthorities();
        setUpDB();

        auth.userDetailsService(userDetailsManager);
        auth.jdbcAuthentication().dataSource(userDetailsManager.getDataSource());

        if (userDetailsManager.userExists(SERVER_ROOT_USER)) {
            userDetailsManager.deleteUser(SERVER_ROOT_USER);
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROOT"));
        authorities.add(new SimpleGrantedAuthority("USER"));
        User user = new User(SERVER_ROOT_USER, rootUserPassword, authorities);
        userDetailsManager.createUser(user);
    }

    /**
     * Flyway must be called manually
     * SpringSecurity starts BEFORE Flyway
     * This causes errors during because SpringSecurity tables are created after SpringSecurity startup
     */
    public void setUpDB() {
        try {
            Flyway flyway = new Flyway();
            flyway.setInitOnMigrate(true);
            flyway.setSqlMigrationPrefix("V");
            flyway.setSqlMigrationSuffix(".sql");
            flyway.setInitVersion("1");
            flyway.setLocations("./");
            flyway.setDataSource(userDetailsManager.getDataSource());
            flyway.migrate();
        } catch (Exception e) {
            throw new Error(e);
        }
    }
}
