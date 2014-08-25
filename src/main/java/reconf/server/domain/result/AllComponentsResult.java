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
import reconf.server.domain.*;

@XmlRootElement(name="components")
@XmlType(propOrder={"product", "components", "links", "errors"})
public class AllComponentsResult {

    private String product;
    private List<ComponentResult> components;
    private List<Link> links;
    private List<String> errors;

    public AllComponentsResult() { }

    public AllComponentsResult(Product product, List<ComponentResult> components, String baseUrl) {
        this.product = product.getName();
        this.components = components;
        this.links = new ArrayList<>();
        this.links.add(new Link(URI.create(baseUrl + getUri()), "self"));
    }

    public AllComponentsResult(String product, List<String> errors) {
        this.product = product;
        this.errors = errors;
    }

    private String getUri() {
        return "/product/" + product + "/component/";
    }

    @XmlElement(name="product")
    public String getProduct() {
        return product;
    }

    @XmlElement(name="component")
    public List<ComponentResult> getComponents() {
        return components;
    }

    @XmlElementWrapper(name="links") @XmlElement(name="link")
    public List<Link> getLinks() {
        return links;
    }

    @XmlElementWrapper(name="errors") @XmlElement(name="error")
    public List<String> getErrors() {
        return errors;
    }
}
