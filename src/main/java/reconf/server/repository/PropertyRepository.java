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
package reconf.server.repository;

import java.util.*;
import org.springframework.data.repository.*;
import reconf.server.domain.*;

public interface PropertyRepository extends CrudRepository<Property, PropertyKey> {

    List<Property> deleteByKeyProduct(String product);
    List<Property> deleteByKeyProductAndKeyComponent(String product, String component);
    List<Property> findByKeyProductAndKeyComponent(String product, String component);
    List<Property> findByKeyProductAndKeyComponentAndKeyNameOrderByRulePriorityDescKeyRuleNameAsc(String product, String component, String name);
}
