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

import javax.servlet.http.*;
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
public class ReadPropertyService {

    @Autowired ProductRepository products;
    @Autowired ComponentRepository components;
    @Autowired PropertyRepository properties;

    @RequestMapping(value="/product/{prod}/component/{comp}/property/{prop}", method=RequestMethod.GET)
    @Transactional(readOnly=true)
    public ResponseEntity<PropertyResult> global(
            @PathVariable("prod") String product,
            @PathVariable("comp") String component,
            @PathVariable("prop") String property,
            HttpServletRequest request,
            Authentication auth) {


        PropertyKey key = new PropertyKey(product, component, property);
        Property reqProperty = new Property(key);

        if (!products.exists(key.getProduct())) {
            return new ResponseEntity<PropertyResult>(new PropertyResult(reqProperty, Product.NOT_FOUND), HttpStatus.NOT_FOUND);
        }
        if (!components.exists(new ComponentKey(key.getProduct(), key.getComponent()))) {
            return new ResponseEntity<PropertyResult>(new PropertyResult(reqProperty, Component.NOT_FOUND), HttpStatus.NOT_FOUND);
        }

        Property dbProperty = properties.findOne(key);
        if (dbProperty == null) {
            return new ResponseEntity<PropertyResult>(new PropertyResult(reqProperty, Property.NOT_FOUND), HttpStatus.NOT_FOUND);
        }

        PropertyResult result = new PropertyResult(dbProperty, CrudServiceUtils.getBaseUrl(request));
        result.addSelfUri(CrudServiceUtils.getBaseUrl(request));
        return new ResponseEntity<PropertyResult>(result, HttpStatus.OK);
    }

    @RequestMapping(value="/product/{prod}/component/{comp}/property/{prop}/rule/{rule}", method=RequestMethod.GET)
    @Transactional(readOnly=true)
    public ResponseEntity<PropertyRuleResult> restricted(
            @PathVariable("prod") String product,
            @PathVariable("comp") String component,
            @PathVariable("prop") String property,
            @PathVariable("rule") String rule,
            HttpServletRequest request) {

        PropertyKey key = new PropertyKey(product, component, property, rule);
        Property fromRequest = new Property(key);

        if (!products.exists(key.getProduct())) {
            return new ResponseEntity<PropertyRuleResult>(new PropertyRuleResult(fromRequest, Product.NOT_FOUND), HttpStatus.NOT_FOUND);
        }
        if (!components.exists(new ComponentKey(key.getProduct(), key.getComponent()))) {
            return new ResponseEntity<PropertyRuleResult>(new PropertyRuleResult(fromRequest, Component.NOT_FOUND), HttpStatus.NOT_FOUND);
        }

        Property target = properties.findOne(key);
        if (target == null) {
            return new ResponseEntity<PropertyRuleResult>(new PropertyRuleResult(fromRequest, Property.NOT_FOUND), HttpStatus.NOT_FOUND);
        }
        PropertyRuleResult result = new PropertyRuleResult(target, CrudServiceUtils.getBaseUrl(request));
        result.addSelfUri(CrudServiceUtils.getBaseUrl(request));
        return new ResponseEntity<PropertyRuleResult>(result, HttpStatus.OK);
    }
}
