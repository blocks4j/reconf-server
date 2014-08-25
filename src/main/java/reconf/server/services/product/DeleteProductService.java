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
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.security.core.*;
import org.springframework.transaction.annotation.*;
import org.springframework.web.bind.annotation.*;
import reconf.server.*;
import reconf.server.domain.*;
import reconf.server.domain.result.*;
import reconf.server.repository.*;
import reconf.server.services.*;

@CrudService
public class DeleteProductService {

    @Autowired ProductRepository products;
    @Autowired ComponentRepository components;
    @Autowired PropertyRepository properties;
    @Autowired UserProductRepository userProducts;

    @RequestMapping(value="/product/{prod}", method=RequestMethod.DELETE)
    @Transactional
    public ResponseEntity<ProductResult> doIt(@PathVariable("prod") String product, Authentication auth) {

        if (!ApplicationSecurity.isRoot(auth)) {
            return new ResponseEntity<ProductResult>(HttpStatus.FORBIDDEN);
        }

        Product reqProduct = new Product(product, null);
        List<String> errors = DomainValidator.checkForErrors(reqProduct);

        if (!errors.isEmpty()) {
            return new ResponseEntity<ProductResult>(new ProductResult(reqProduct, errors), HttpStatus.BAD_REQUEST);
        }

        if (!products.exists(reqProduct.getName())) {
            return new ResponseEntity<ProductResult>(new ProductResult(reqProduct, Product.NOT_FOUND), HttpStatus.NOT_FOUND);
        }

        products.delete(reqProduct.getName());
        components.deleteByKeyProduct(reqProduct.getName());
        properties.deleteByKeyProduct(reqProduct.getName());
        userProducts.deleteByKeyProduct(reqProduct.getName());
        return new ResponseEntity<ProductResult>(HttpStatus.OK);
    }
}
