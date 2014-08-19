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

import java.sql.*;
import java.util.*;
import javax.sql.*;
import org.slf4j.*;
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

@Configuration
@EnableWebSecurity
// http://justinrodenbostel.com/2014/05/30/part-5-integrating-spring-security-with-spring-boot-web/
public class ApplicationSecurity extends WebSecurityConfigurerAdapter {

    private static final Logger log = LoggerFactory.getLogger(ApplicationSecurity.class);
    private static final String SERVER_ROOT_USER = "reconf";

    @Autowired DataSource dataSource;
    @Value("${reconf.user.password}") String rootUserPassword;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests().antMatchers("/crud/**").hasRole("ROOT").and().httpBasic();
        http.authorizeRequests().antMatchers("/security/user").hasRole("ROOT").and().httpBasic();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        createTableUsers();
        createTableAuthorities();

        JdbcUserDetailsManager userDetailsManager = getJdbcUserDetailsManager(dataSource);

        auth.userDetailsService(userDetailsManager);
        auth.jdbcAuthentication().dataSource(dataSource);

        if (userDetailsManager.userExists(SERVER_ROOT_USER)) {
            userDetailsManager.deleteUser(SERVER_ROOT_USER);
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROOT"));
        User user = new User(SERVER_ROOT_USER, rootUserPassword, authorities);
        userDetailsManager.createUser(user);
    }

    private void createTableUsers() {
        execute("create table users ( username varchar(50) not null primary key, password varchar(255) not null, enabled boolean not null)");
    }

    private void createTableAuthorities() {
        execute("create table authorities ( username varchar(50) not null, authority varchar(50) not null, foreign key (username) references users (username) )");
    }

    //http://docs.spring.io/spring-security/site/docs/3.0.x/reference/appendix-schema.html
    private void execute(String sql) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = dataSource.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.execute();

        } catch (Exception e) {
            log.warn("error creating spring-security tables", e);

        } finally {
            try {
                stmt.close();
            } catch (Exception ignored1) { }
            try {
                conn.close();
            } catch (Exception ignored2) { }
        }
    }

    public static JdbcUserDetailsManager getJdbcUserDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager();
        userDetailsManager.setDataSource(dataSource);
        userDetailsManager.setUsersByUsernameQuery("SELECT username, password,enabled FROM users WHERE username=?");
        userDetailsManager.setAuthoritiesByUsernameQuery("SELECT username, authority FROM authorities WHERE username = ?");
        userDetailsManager.setRolePrefix("ROLE_");
        return userDetailsManager;
    }
}
