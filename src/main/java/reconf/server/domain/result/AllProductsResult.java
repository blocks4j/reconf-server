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
package reconf.server.domain.result;

import java.net.*;
import java.util.*;
import javax.xml.bind.annotation.*;

@XmlRootElement(name="products")
@XmlType(propOrder={"products", "links"})
public class AllProductsResult {

    private List<ProductResult> products;
    private List<Link> links;

    public AllProductsResult() { }

    public AllProductsResult(List<ProductResult> arg, String baseUrl) {
        this.products = arg;
        this.links = new ArrayList<>();
        this.links.add(new Link(URI.create(baseUrl + "/product"), "self"));
    }

    @XmlElement(name="product")
    public List<ProductResult> getProducts() {
        return products;
    }

    @XmlElementWrapper(name="links") @XmlElement(name="link")
    public List<Link> getLinks() {
        return links;
    }
}
