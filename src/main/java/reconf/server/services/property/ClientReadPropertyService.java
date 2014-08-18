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
import org.apache.commons.lang3.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.transaction.annotation.*;
import org.springframework.web.bind.annotation.*;
import reconf.server.*;
import reconf.server.domain.*;
import reconf.server.domain.result.*;
import reconf.server.repository.*;
import reconf.server.services.*;
import com.fasterxml.jackson.databind.*;


@RestController
@RequestMapping(value="/",
    produces = ReConfConstants.MT_PROTOCOL_V1,
    consumes={ReConfConstants.MT_PROTOCOL_V1, ReConfConstants.MT_TEXT_PLAIN, ReConfConstants.MT_ALL})
public class ClientReadPropertyService {

    private static final String JS = "var patt = eval(regexp); var result = patt.test(instance);";
    private static ObjectMapper mapper = new ObjectMapper();
    private static final Logger log = LoggerFactory.getLogger(ClientReadPropertyService.class);

    @Autowired PropertyRepository properties;
    @Autowired JavaScriptEngine engine;

    @RequestMapping(value="/{prod}/{comp}/{prop}", method=RequestMethod.GET)
    @Transactional(readOnly=true)
    public ResponseEntity<String> doIt(
            @PathVariable("prod") String product,
            @PathVariable("comp") String component,
            @PathVariable("prop") String property,
            @RequestParam(value="instance", required=false, defaultValue="unknown") String instance) {

        PropertyKey key = new PropertyKey(product, component, property);
        List<String> errors = DomainValidator.checkForErrors(key);
        Property reqProperty = new Property(key);

        HttpHeaders headers = new HttpHeaders();
        if (!errors.isEmpty()) {
            addErrorHeader(headers, errors, reqProperty);
            return new ResponseEntity<String>(headers, HttpStatus.BAD_REQUEST);
        }

        List<Property> dbProperties = properties.findByKeyProductAndKeyComponentAndKeyNameOrderByRulePriorityDescKeyRuleNameAsc(key.getProduct(), key.getComponent(), key.getName());
        for (Property dbProperty : dbProperties) {
            try {
                if (isMatch(instance, dbProperty)) {
                    addRuleHeader(headers, dbProperty);
                    return new ResponseEntity<String>(dbProperty.getValue(), headers, HttpStatus.OK);
                }
            } catch (Exception e) {
                log.error("error applying rule", e);
                addRuleHeader(headers, dbProperty);
                addErrorHeader(headers, Collections.singletonList("rule error"), reqProperty);
                return new ResponseEntity<String>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
    }

    private boolean isMatch(String instance, Property each) {
        Map<String, Object> params = new HashMap<>();
        params.put("regexp", StringUtils.defaultString(each.getRuleRegexp()));
        params.put("instance", StringUtils.lowerCase(instance));
        return (boolean) engine.eval(JS, params, "result");
    }

    private void addErrorHeader(HttpHeaders headers, List<String> errors, Property fromRequest) {
        addHeader(headers, ReConfConstants.H_RESPONSE_RESULT, new PropertyResult(fromRequest, errors));
    }

    private void addRuleHeader(HttpHeaders headers, Property property) {
        addHeader(headers, ReConfConstants.H_RESPONSE_RULE, new Rule(property));
    }

    private HttpHeaders addHeader(HttpHeaders headers, String name, Object value) {
        try {
            headers.add(name, mapper.writeValueAsString(value));
        } catch (Exception ignored) {
        }
        return headers;
    }
}
