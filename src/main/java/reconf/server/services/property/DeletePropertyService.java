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
public class DeletePropertyService {

    @Autowired PropertyRepository properties;

    @RequestMapping(value="/product/{prod}/component/{comp}/property/{prop}", method=RequestMethod.DELETE)
    @Transactional
    public ResponseEntity<PropertyResult> global(
            @PathVariable("prod") String product,
            @PathVariable("comp") String component,
            @PathVariable("prop") String property,
            Authentication auth) {

        PropertyKey key = new PropertyKey(product, component, property);
        Property reqProperty = new Property(key, null);

        List<String> errors = DomainValidator.checkForErrors(key);
        if (!errors.isEmpty()) {
            return new ResponseEntity<PropertyResult>(new PropertyResult(reqProperty, errors), HttpStatus.BAD_REQUEST);
        }

        if (!properties.exists(key)) {
            return new ResponseEntity<PropertyResult>(new PropertyResult(reqProperty, Property.NOT_FOUND), HttpStatus.NOT_FOUND);
        }
        properties.delete(key);
        return new ResponseEntity<PropertyResult>(HttpStatus.OK);
    }

    @RequestMapping(value="/product/{prod}/component/{comp}/property/{prop}/rule/{rule}", method=RequestMethod.DELETE)
    @Transactional
    public ResponseEntity<PropertyResult> resticted(
            @PathVariable("prod") String product,
            @PathVariable("comp") String component,
            @PathVariable("prop") String property,
            @PathVariable("rule") String rule) {

        PropertyKey key = new PropertyKey(product, component, property, rule);
        Property fromRequest = new Property(key, null);

        List<String> errors = DomainValidator.checkForErrors(key);
        if (!errors.isEmpty()) {
            return new ResponseEntity<PropertyResult>(new PropertyResult(fromRequest, errors), HttpStatus.BAD_REQUEST);
        }

        if (!properties.exists(key)) {
            return new ResponseEntity<PropertyResult>(new PropertyResult(fromRequest, Property.NOT_FOUND), HttpStatus.NOT_FOUND);
        }
        properties.delete(key);
        return new ResponseEntity<PropertyResult>(HttpStatus.OK);
    }
}
