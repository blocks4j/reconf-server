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
package reconf.server.services.property;

import java.util.*;
import javax.servlet.http.*;
import org.apache.commons.collections4.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.security.core.*;
import org.springframework.transaction.annotation.*;
import org.springframework.web.bind.annotation.*;
import reconf.server.domain.*;
import reconf.server.domain.result.*;
import reconf.server.repository.*;
import reconf.server.services.*;

@CrudService
public class ReadAllRestrictedPropertiesService {

    @Autowired ProductRepository products;
    @Autowired ComponentRepository components;
    @Autowired PropertyRepository properties;

    @RequestMapping(value="/product/{prod}/component/{comp}/property/{prop}/rule", method=RequestMethod.GET)
    @Transactional(readOnly=true)
    public ResponseEntity<AllRestrictedPropertiesResult> restricted(
            @PathVariable("prod") String product,
            @PathVariable("comp") String component,
            @PathVariable("prop") String property,
            HttpServletRequest request,
            Authentication auth) {

        PropertyKey key = new PropertyKey(product, component, property);

        if (!products.exists(key.getProduct())) {
            return new ResponseEntity<AllRestrictedPropertiesResult>(new AllRestrictedPropertiesResult(key, Product.NOT_FOUND), HttpStatus.NOT_FOUND);
        }
        if (!components.exists(new ComponentKey(key.getProduct(), key.getComponent()))) {
            return new ResponseEntity<AllRestrictedPropertiesResult>(new AllRestrictedPropertiesResult(key, Component.NOT_FOUND), HttpStatus.NOT_FOUND);
        }
        List<Property> dbProperties = properties.findByKeyProductAndKeyComponentAndKeyNameOrderByRulePriorityDescKeyRuleNameAsc(key.getProduct(), key.getComponent(), key.getName());
        if (CollectionUtils.isEmpty(dbProperties)) {
            return new ResponseEntity<AllRestrictedPropertiesResult>(new AllRestrictedPropertiesResult(key, Property.NOT_FOUND), HttpStatus.NOT_FOUND);
        }
        List<Rule> rules = new ArrayList<>();
        for (int i = 0; i < dbProperties.size() - 1; i++) {
            rules.add(new Rule(dbProperties.get(i)));
        }

        return new ResponseEntity<AllRestrictedPropertiesResult>(new AllRestrictedPropertiesResult(key, rules, CrudServiceUtils.getBaseUrl(request)), HttpStatus.OK);
    }
}
