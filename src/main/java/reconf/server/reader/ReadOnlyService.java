/*
 *    Copyright 1996-2014 UOL Inc
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
package reconf.server.reader;

import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import reconf.server.domain.*;

@RestController
@RequestMapping(value="/", produces="application/vnd.reconf-v1+text", consumes={"application/json", "text/plain", "application/vnd.reconf-v1+text"})
public class ReadOnlyService {

    @Autowired private PropertyRepository properties;

    @RequestMapping(value="/{prod}/{comp}/{prop}", method=RequestMethod.GET)
    public ResponseEntity<String> read(@PathVariable("prod") String product, @PathVariable("comp") String component, @PathVariable("prop") String property, @RequestParam(value="instance", required=false) String hostName) {
        System.out.println("read!");
        Property fromDB = properties.findOne(new PropertyKey(product, component, property, hostName));
        if (fromDB != null) {
            return new ResponseEntity<String>(fromDB.getValue(), HttpStatus.OK);
        }
        return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
    }
}
