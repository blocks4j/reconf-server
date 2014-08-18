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
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.transaction.annotation.*;
import org.springframework.web.bind.annotation.*;
import reconf.server.domain.*;
import reconf.server.domain.result.*;
import reconf.server.repository.*;
import reconf.server.services.*;

@CrudService
public class UpsertRestrictedPropertyService {

    @Autowired ProductRepository products;
    @Autowired ComponentRepository components;
    @Autowired PropertyRepository properties;

    @RequestMapping(value="/product/{prod}/component/{comp}/property/{prop}/rule/{rule}", method=RequestMethod.PUT)
    @Transactional
    public ResponseEntity<PropertyRuleResult> restricted(
            @PathVariable("prod") String product,
            @PathVariable("comp") String component,
            @PathVariable("prop") String property,
            @PathVariable("rule") String ruleName,
            @RequestBody String value,
            @RequestParam(value="desc", required=false) String description,
            @RequestParam(value="rpriority", required=false) Integer rulePriority,
            @RequestParam(value="rexpr", required=false) String ruleRegexp,
            HttpServletRequest request) {

        PropertyKey key = new PropertyKey(product, component, property, ruleName);
        Property fromRequest = new Property(key, value, description, rulePriority, ruleRegexp);
        List<String> errors = DomainValidator.checkForErrors(fromRequest);

        if (!errors.isEmpty()) {
            return new ResponseEntity<PropertyRuleResult>(new PropertyRuleResult(fromRequest, errors), HttpStatus.BAD_REQUEST);
        }
        if (!products.exists(key.getProduct())) {
            return new ResponseEntity<PropertyRuleResult>(new PropertyRuleResult(fromRequest, Product.NOT_FOUND), HttpStatus.NOT_FOUND);
        }
        if (!components.exists(new ComponentKey(key.getProduct(), key.getComponent()))) {
            return new ResponseEntity<PropertyRuleResult>(new PropertyRuleResult(fromRequest, Component.NOT_FOUND), HttpStatus.NOT_FOUND);
        }

        HttpStatus status = null;
        Property target = properties.findOne(key);
        if (target != null) {
            target.setValue(value);
            target.setDescription(description);
            status = HttpStatus.OK;

        } else {
            target = fromRequest;
            properties.save(target);
            status = HttpStatus.CREATED;
        }
        return new ResponseEntity<PropertyRuleResult>(new PropertyRuleResult(target, CrudServiceUtils.getBaseUrl(request)), status);
    }
}
