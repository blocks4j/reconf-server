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
import org.springframework.security.core.*;
import org.springframework.transaction.annotation.*;
import org.springframework.web.bind.annotation.*;
import reconf.server.domain.*;
import reconf.server.domain.result.*;
import reconf.server.repository.*;
import reconf.server.services.*;

@CrudService
public class UpsertPropertyService {

    @Autowired ProductRepository products;
    @Autowired ComponentRepository components;
    @Autowired PropertyRepository properties;

    @RequestMapping(value="/product/{prod}/component/{comp}/property/{prop}", method=RequestMethod.PUT)
    @Transactional
    public ResponseEntity<PropertyResult> global(
            @PathVariable("prod") String product,
            @PathVariable("comp") String component,
            @PathVariable("prop") String property,
            @RequestBody String value,
            @RequestParam(value="desc", required=false) String description,
            HttpServletRequest request,
            Authentication auth) {

        PropertyKey key = new PropertyKey(product, component, property);
        Property reqProperty = new Property(key, value, description);

        List<String> errors = DomainValidator.checkForErrors(reqProperty);

        if (!errors.isEmpty()) {
            return new ResponseEntity<PropertyResult>(new PropertyResult(reqProperty, errors), HttpStatus.BAD_REQUEST);
        }
        if (!products.exists(key.getProduct())) {
            return new ResponseEntity<PropertyResult>(new PropertyResult(reqProperty, Product.NOT_FOUND), HttpStatus.NOT_FOUND);
        }
        if (!components.exists(new ComponentKey(key.getProduct(), key.getComponent()))) {
            return new ResponseEntity<PropertyResult>(new PropertyResult(reqProperty, Component.NOT_FOUND), HttpStatus.NOT_FOUND);
        }

        HttpStatus status = null;
        Property dbProperty = properties.findOne(key);
        if (dbProperty != null) {
            dbProperty.setValue(value);
            dbProperty.setDescription(description);
            status = HttpStatus.OK;

        } else {
            dbProperty = reqProperty;
            properties.save(dbProperty);
            status = HttpStatus.CREATED;
        }
        return new ResponseEntity<PropertyResult>(new PropertyResult(dbProperty, CrudServiceUtils.getBaseUrl(request)), status);
    }
}
