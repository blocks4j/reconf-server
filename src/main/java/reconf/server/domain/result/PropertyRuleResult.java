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
public class PropertyRuleResult {

    private String property;
    private String product;
    private String component;
    private String instance;
    private String desc;
    private Link link;
    private List<String> errors;
    private Rule rule;

    private PropertyRuleResult(Property arg) {
        this.product = arg.getKey().getProduct();
        this.component = arg.getKey().getComponent();
        this.property = arg.getKey().getName();
        this.desc = arg.getDescription();
        this.rule = new Rule(arg);
    }

    public PropertyRuleResult(Property arg, String baseURL) {
        this(arg);
        this.link = new Link(URI.create(baseURL + getUriOf(arg)), "alternate");
    }

    public PropertyRuleResult(Property arg, List<String> errors) {
        this(arg);
        this.errors = errors;
    }

    private static String getUriOf(Property property) {
        return "/" + property.getKey().getProduct() + "/" + property.getKey().getComponent() + "/" + property.getKey().getName();
    }

    public String getProperty() {
        return property;
    }

    public String getProduct() {
        return product;
    }

    public String getComponent() {
        return component;
    }

    public String getInstance() {
        return instance;
    }

    public Link getLink() {
        return link;
    }

    public String getDesc() {
        return desc;
    }

    public List<String> getErrors() {
        return errors;
    }

    public Rule getRule() {
        return rule;
    }
}
