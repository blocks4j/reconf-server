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
package reconf.server.services.component;

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
public class ReadAllComponentsService {

    @Autowired ComponentRepository components;
    @Autowired ProductRepository products;

    @RequestMapping(value="/product/{prod}/component", method=RequestMethod.GET)
    @Transactional(readOnly=true)
    public ResponseEntity<AllComponentsResult> doIt(@PathVariable("prod") String productId,
            HttpServletRequest request,
            Authentication auth) {

        Product fromRequest = new Product(productId);

        List<String> errors = DomainValidator.checkForErrors(fromRequest);
        if (!errors.isEmpty()) {
            return new ResponseEntity<AllComponentsResult>(new AllComponentsResult(productId, errors), HttpStatus.BAD_REQUEST);
        }

        Product dbProduct = products.findOne(fromRequest.getName());

        if (dbProduct == null) {
            return new ResponseEntity<AllComponentsResult>(new AllComponentsResult(productId, Product.NOT_FOUND), HttpStatus.NOT_FOUND);
        }

        List<ComponentResult> result = new ArrayList<>();
        for (Component component : components.findByKeyProductOrderByKeyNameAsc(dbProduct.getName())) {
            result.add(new ComponentResult(component, CrudServiceUtils.getBaseUrl(request)));
        }

        return new ResponseEntity<AllComponentsResult>(new AllComponentsResult(dbProduct, result, CrudServiceUtils.getBaseUrl(request)), HttpStatus.OK);
    }
}
