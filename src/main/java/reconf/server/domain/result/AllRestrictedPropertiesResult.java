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
public class AllRestrictedPropertiesResult {

    private String product;
    private String component;
    private String property;
    private List<Rule> rules;
    private List<Link> links;
    private List<String> errors;

    public AllRestrictedPropertiesResult(PropertyKey key, List<Rule> rules, String baseUrl) {
        this.product = key.getProduct();
        this.component = key.getComponent();
        this.property = key.getName();

        this.rules = rules;
        this.links = new ArrayList<>();
        this.links.add(new Link(URI.create(baseUrl + getUri()), "self"));

        for (Rule rule : rules) {
            rule.addLink(URI.create(baseUrl + getUri() + "/" + rule.getName()), "self");
        }
    }

    public AllRestrictedPropertiesResult(PropertyKey key, List<String> errors) {
        this.product = key.getProduct();
        this.component = key.getComponent();
        this.property = key.getName();
        this.errors = errors;
    }

    private String getUri() {
        return "/product/" + product + "/component/" + component + "/property/" + property + "/rule";
    }

    public String getProduct() {
        return product;
    }

    public String getComponent() {
        return component;
    }

    public String getProperty() {
        return property;
    }

    public List<Rule> getRules() {
        return rules;
    }

    public List<Link> getLinks() {
        return links;
    }

    public List<String> getErrors() {
        return errors;
    }
}
