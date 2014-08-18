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
package reconf.server.services.product;

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
public class UpsertProductService {

    @Autowired ProductRepository products;

    @RequestMapping(value="/product/{prod}", method=RequestMethod.PUT)
    @Transactional
    public ResponseEntity<ProductResult> doIt(
            @PathVariable("prod") String product,
            @RequestParam(value="desc", required=false) String description,
            HttpServletRequest request) {

        Product fromRequest = new Product(product, description);
        List<String> errors = DomainValidator.checkForErrors(fromRequest);
        if (!errors.isEmpty()) {
            return new ResponseEntity<ProductResult>(new ProductResult(fromRequest, errors), HttpStatus.BAD_REQUEST);
        }

        HttpStatus status = null;
        Product target = products.findOne(fromRequest.getName());
        if (target != null) {
            target.setDescription(description);
            status = HttpStatus.OK;

        } else {
            target = new Product(fromRequest.getName(), fromRequest.getDescription());
            target.setDescription(description);
            products.save(target);
            status = HttpStatus.CREATED;
        }
        return new ResponseEntity<ProductResult>(new ProductResult(target, CrudServiceUtils.getBaseUrl(request)), status);
    }
}
