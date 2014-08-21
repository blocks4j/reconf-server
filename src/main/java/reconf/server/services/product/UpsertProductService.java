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
import org.apache.commons.collections4.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.security.core.*;
import org.springframework.transaction.annotation.*;
import org.springframework.web.bind.annotation.*;
import reconf.server.*;
import reconf.server.domain.*;
import reconf.server.domain.result.*;
import reconf.server.domain.security.*;
import reconf.server.repository.*;
import reconf.server.services.*;

@CrudService
public class UpsertProductService {

    @Autowired ProductRepository products;
    @Autowired UserProductRepository userProducts;
    @Autowired ApplicationSecurity appSecurity;

    @RequestMapping(value="/product/{prod}", method=RequestMethod.PUT)
    @Transactional
    public ResponseEntity<ProductResult> doIt(
            @PathVariable("prod") String product,
            @RequestParam(value="user", required=false) List<String> users,
            @RequestParam(value="desc", required=false) String description,
            HttpServletRequest request,
            Authentication auth) {

        if (!ApplicationSecurity.isRoot(auth)) {
            return new ResponseEntity<ProductResult>(HttpStatus.FORBIDDEN);
        }

        Product reqProduct = new Product(product, description);
        ResponseEntity<ProductResult> errorResponse = checkForErrors(auth, reqProduct, users);
        if (errorResponse != null) {
            return errorResponse;
        }

        HttpStatus status = null;
        Product dbProduct = products.findOne(reqProduct.getName());
        if (dbProduct != null) {
            userProducts.deleteByKeyProduct(reqProduct.getName());
            dbProduct.setDescription(description);
            status = HttpStatus.OK;

        } else {
            dbProduct = new Product(reqProduct.getName(), reqProduct.getDescription());
            dbProduct.setDescription(description);
            products.save(dbProduct);
            status = HttpStatus.CREATED;
        }

        dbProduct.setUsers(users);
        users = CollectionUtils.isEmpty(users) ? Collections.EMPTY_LIST : users;
        for (String user : users) {
            if (ApplicationSecurity.isRoot(user)) {
                continue;
            }
            userProducts.save(new UserProduct(new UserProductKey(user, reqProduct.getName())));
        }
        return new ResponseEntity<ProductResult>(new ProductResult(dbProduct, CrudServiceUtils.getBaseUrl(request)), status);
    }

    private ResponseEntity<ProductResult> checkForErrors(Authentication auth, Product reqProduct, List<String> users) {
        List<String> errors = DomainValidator.checkForErrors(reqProduct);

        if (CollectionUtils.isNotEmpty(users)) {
            for (String user : users) {
                if (!appSecurity.userExists(user)) {
                    errors.add("user " + user + " does not exist");
                }
            }
        }
        if (!errors.isEmpty()) {
            return new ResponseEntity<ProductResult>(new ProductResult(reqProduct, errors), HttpStatus.BAD_REQUEST);
        }
        return null;
    }
}
