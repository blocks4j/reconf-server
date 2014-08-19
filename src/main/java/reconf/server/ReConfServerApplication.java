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

import org.springframework.boot.autoconfigure.*;
import org.springframework.boot.builder.*;
import org.springframework.context.annotation.*;

@Configuration
@ComponentScan
@EnableAutoConfiguration
// mvn spring-boot:run
public class ReConfServerApplication {

    public static final String CRUD_ROOT = "/crud";
    public static final String SECURITY_ROOT = "/security";
    public static final String SERVER_ROOT_USER = "reconf";

    public static void main(String[] args) {
        new SpringApplicationBuilder()
        .showBanner(false)
        .sources(ReConfServerApplication.class)
        .run(args);
    }
}
