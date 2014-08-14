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
import javax.script.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.transaction.annotation.*;
import org.springframework.web.bind.annotation.*;
import reconf.server.*;
import reconf.server.domain.*;
import reconf.server.domain.result.*;
import reconf.server.repository.*;
import com.fasterxml.jackson.databind.*;


@RestController
@RequestMapping(value="/",
    produces = ReConfConstants.MT_PROTOCOL_V1,
    consumes={ReConfConstants.MT_PROTOCOL_V1, ReConfConstants.MT_TEXT_PLAIN, ReConfConstants.MT_ALL})
public class ReadPropertyService {

    @Autowired PropertyRepository properties;
    private static final String JS = "var patt = eval(regexp); var result = patt.test(instance);";
    private static ObjectMapper mapper = new ObjectMapper();

    @RequestMapping(value="/{prod}/{comp}/{prop}", method=RequestMethod.GET)
    @Transactional(readOnly=true)
    public ResponseEntity<String> doIt(
            @PathVariable("prod") String product,
            @PathVariable("comp") String component,
            @PathVariable("prop") String property,
            @RequestParam(value="instance", required=false, defaultValue="unknown") String instance) {

        PropertyKey key = new PropertyKey(product, component, property);
        List<String> errors = DomainValidator.checkForErrors(key);
        Property fromRequest = new Property(key);

        HttpHeaders headers = new HttpHeaders();
        if (!errors.isEmpty()) {
            addErrorHeader(headers, errors, fromRequest);
            return new ResponseEntity<String>(headers, HttpStatus.BAD_REQUEST);
        }

        List<Property> fromDB = properties.findByKeyProductAndKeyComponentAndKeyNameOrderByRulePriorityDesc(key.getProduct(), key.getComponent(), key.getName());
        for (Property each : fromDB) {
            if (isMatch(instance, each)) {
                addRuleHeader(headers, each);
                return new ResponseEntity<String>(each.getValue(), headers, HttpStatus.OK);
            }
        }
        return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
    }

    private boolean isMatch(String instance, Property each) {
        try {
            ScriptEngineManager factory = new ScriptEngineManager();
            ScriptEngine engine = factory.getEngineByName("JavaScript");
            engine.put("regexp", each.getRuleRegexp());
            engine.put("instance", instance);
            engine.eval(JS);
            return (boolean) engine.get("result");
        } catch (Exception e) {
        }
        return false;
    }

    private HttpHeaders addErrorHeader(HttpHeaders headers, List<String> errors, Property fromRequest) {

        try {
            headers.add(ReConfConstants.H_RESPONSE_RESULT, mapper.writeValueAsString(new PropertyResult(fromRequest, errors)));
        } catch (Exception ignored) {
        }
        return headers;
    }

    private HttpHeaders addRuleHeader(HttpHeaders headers, Property property) {

        try {
            headers.add(ReConfConstants.H_RESPONSE_RULE, mapper.writeValueAsString(new Rule(property)));
        } catch (Exception ignored) {
        }
        return headers;
    }
}
