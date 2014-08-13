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
import org.springframework.web.bind.annotation.*;
import reconf.server.*;
import reconf.server.domain.*;
import reconf.server.domain.result.*;
import reconf.server.repository.*;
import com.fasterxml.jackson.databind.*;


@RestController
@RequestMapping(value="/",
    produces = ReConfMediaType.PROTOCOL_V1,
    consumes={ReConfMediaType.PROTOCOL_V1, ReConfMediaType.TEXT_PLAIN, ReConfMediaType.ALL})
public class ReadPropertyService {

    @Autowired PropertyRepository properties;
    private static ObjectMapper mapper = new ObjectMapper();

    @RequestMapping(value="/{prod}/{comp}/{prop}", method=RequestMethod.GET)
    public ResponseEntity<String> doIt(
            @PathVariable("prod") String product,
            @PathVariable("comp") String component,
            @PathVariable("prop") String property) {

        PropertyKey key = new PropertyKey(product, component, property);
        List<String> errors = DomainValidator.checkForErrors(key);
        Property fromRequest = new Property(key);

        if (!errors.isEmpty()) {
            HttpHeaders headers = getErrorHeader(errors, fromRequest);
            return new ResponseEntity<String>(headers, HttpStatus.BAD_REQUEST);
        }

        Property fromDB = properties.findOne(key);
        if (fromDB != null) {
            return new ResponseEntity<String>(fromDB.getValue(), HttpStatus.OK);
        }
        return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
    }

    private HttpHeaders getErrorHeader(List<String> errors, Property fromRequest) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("X-ReConf-Result", mapper.writeValueAsString(new PropertyResult(fromRequest, errors)));
        } catch (Exception ignored) {
        }
        return headers;
    }
}
