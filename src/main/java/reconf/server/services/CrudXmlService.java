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
package reconf.server.services;

import java.lang.annotation.*;
import org.springframework.web.bind.annotation.*;
import reconf.server.*;

@RestController
@RequestMapping(value=ReConfConstants.CRUD_ROOT,
    produces=ReConfConstants.MT_APPLICATION_XML,
    consumes={ReConfConstants.MT_TEXT_PLAIN, ReConfConstants.MT_ALL})
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CrudXmlService {
}
