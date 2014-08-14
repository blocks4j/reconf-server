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
import org.springframework.transaction.annotation.*;
import org.springframework.web.bind.annotation.*;
import reconf.server.*;
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
    public ResponseEntity<PropertyResult> doIt(
            @PathVariable("prod") String product,
            @PathVariable("comp") String component,
            @PathVariable("prop") String property,
            @RequestBody(required=true) String value,
            @RequestParam(required=false, value="description") String description,
            HttpServletRequest request) {

        PropertyKey key = new PropertyKey(product, component, property);
        Property fromRequest = new Property(key, value, description);
        List<String> errors = DomainValidator.checkForErrors(key);
        if (StringUtils.isEmpty(value)) {
            errors.add(Property.VALUE_MESSAGE);
        }
        if (!errors.isEmpty()) {
            return new ResponseEntity<PropertyResult>(new PropertyResult(fromRequest, errors), HttpStatus.BAD_REQUEST);
        }
        if (!products.exists(key.getProduct())) {
            return new ResponseEntity<PropertyResult>(new PropertyResult(fromRequest, Product.NOT_FOUND), HttpStatus.NOT_FOUND);
        }
        if (!components.exists(new ComponentKey(key.getProduct(), key.getComponent()))) {
            return new ResponseEntity<PropertyResult>(new PropertyResult(fromRequest, Component.NOT_FOUND), HttpStatus.NOT_FOUND);
        }

        HttpStatus status = null;
        Property target = properties.findOne(key);
        if (target != null) {
            target.setValue(value);
            target.setDescription(description);
            status = HttpStatus.OK;

        } else {
            target = new Property(key, value);
            target.setDescription(description);
            properties.save(target);
            status = HttpStatus.CREATED;
        }
        return new ResponseEntity<PropertyResult>(new PropertyResult(target, getBaseUrl(request)), status);
    }

    private String getBaseUrl(HttpServletRequest req) {
        return StringUtils.substringBefore(req.getRequestURL().toString(), ReConfServerApplication.CRUD_ROOT);
    }
}
