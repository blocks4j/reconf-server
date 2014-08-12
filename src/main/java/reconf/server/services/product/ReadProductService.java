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

import javax.servlet.http.*;
import org.apache.commons.lang3.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.transaction.annotation.*;
import org.springframework.web.bind.annotation.*;
import reconf.server.*;
import reconf.server.domain.*;
import reconf.server.domain.result.*;
import reconf.server.repository.*;
import reconf.server.services.*;

public class ReadProductService implements CrudService {

    @Autowired ProductRepository products;

    @RequestMapping(value="/product/{prod}", method=RequestMethod.GET)
    @Transactional
    public ResponseEntity<ProductResult> doIt(
            @PathVariable("prod") String product,
            HttpServletRequest request) {

        Product fromRequest = new Product(product, "");
        if (DomainValidator.containsErrors(fromRequest)) {
            return new ResponseEntity<ProductResult>(HttpStatus.BAD_REQUEST);
        }

        Product target = products.findOne(fromRequest.getName());
        if (target == null) {
            return new ResponseEntity<ProductResult>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<ProductResult>(new ProductResult(target, getBaseUrl(request)), HttpStatus.OK);
    }

    private String getBaseUrl(HttpServletRequest req) {
        String url = req.getRequestURL().toString();
        return StringUtils.replace(url, StringUtils.substringAfter(url, ReConfServerApplication.CRUD_ROOT), "");
    }
}
