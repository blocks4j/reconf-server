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
import org.apache.commons.lang3.*;
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
public class RealAllPropertiesService {

    @Autowired ProductRepository products;
    @Autowired ComponentRepository components;
    @Autowired PropertyRepository properties;

    @RequestMapping(value="/product/{prod}/component/{comp}/property", method=RequestMethod.GET)
    @Transactional(readOnly=true)
    public ResponseEntity<AllPropertiesResult> global(
            @PathVariable("prod") String product,
            @PathVariable("comp") String component,
            HttpServletRequest request,
            Authentication auth) {

        ComponentKey key = new ComponentKey(product, component);

        if (!products.exists(key.getProduct())) {
            return new ResponseEntity<AllPropertiesResult>(new AllPropertiesResult(key, Product.NOT_FOUND), HttpStatus.NOT_FOUND);
        }
        if (!components.exists(new ComponentKey(key.getProduct(), key.getName()))) {
            return new ResponseEntity<AllPropertiesResult>(new AllPropertiesResult(key, Component.NOT_FOUND), HttpStatus.NOT_FOUND);
        }

        List<PropertyRuleResult> result = new ArrayList<>();
        for (Property dbProperty : properties.findByKeyProductAndKeyComponent(key.getProduct(), key.getName())) {
            PropertyRuleResult toAdd = new PropertyRuleResult(dbProperty, CrudServiceUtils.getBaseUrl(request));
            if (StringUtils.equalsIgnoreCase(dbProperty.getKey().getRuleName(), Property.DEFAULT_RULE_NAME)) {
                toAdd.clearRule();
            }
            toAdd.addSelfUri(CrudServiceUtils.getBaseUrl(request));
            result.add(toAdd);
        }

        return new ResponseEntity<AllPropertiesResult>(new AllPropertiesResult(key, result, CrudServiceUtils.getBaseUrl(request)), HttpStatus.OK);
    }

}
