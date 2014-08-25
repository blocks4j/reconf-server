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
import reconf.server.domain.*;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class AllComponentsResult {

    private String product;
    private String description;
    private List<ComponentResult> components;
    private List<Link> links;
    private List<String> errors;

    public AllComponentsResult(Product product, List<ComponentResult> components, String baseUrl) {
        this.product = product.getName();
        this.description = product.getDescription();
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

    public String getProduct() {
        return product;
    }

    public String getDescription() {
        return description;
    }

    public List<ComponentResult> getComponents() {
        return components;
    }

    public List<Link> getLinks() {
        return links;
    }

    public List<String> getErrors() {
        return errors;
    }


}
