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
public class ReadAllProductsService {

    @Autowired ProductRepository products;
    @Autowired UserProductRepository userProducts;

    @RequestMapping(value="/product", method=RequestMethod.GET)
    @Transactional(readOnly=true)
    public ResponseEntity<AllProductsResult> doIt(HttpServletRequest request, Authentication auth) {

        String baseUrl = CrudServiceUtils.getBaseUrl(request);
        List<ProductResult> result = new ArrayList<>();

        if (ApplicationSecurity.isRoot(auth)) {
            for (Product product : products.findAll()) {
                ProductResult productResult = new ProductResult(product, baseUrl);
                for (UserProduct userProduct : userProducts.findByKeyProduct(product.getName())) {
                    productResult.addUser(userProduct.getKey().getUsername());
                    result.add(productResult);
                }
            }
        } else {
            for(UserProduct userProduct : userProducts.findByKeyUsername(auth.getName())) {
                result.add(new ProductResult(products.findOne(userProduct.getKey().getProduct()), baseUrl));
            }
        }

        return new ResponseEntity<AllProductsResult>(new AllProductsResult(result, baseUrl), HttpStatus.OK);
    }

}
