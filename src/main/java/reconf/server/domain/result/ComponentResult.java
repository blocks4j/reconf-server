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

@XmlRootElement(name="component")
@XmlType(propOrder={"product", "component", "desc", "links", "errors"})
public class ComponentResult {

    private String product;
    private String component;
    private String desc;
    private List<Link> links;
    private List<String> errors;

    public ComponentResult() { }

    private ComponentResult(Component arg) {
        this.product = arg.getKey().getProduct();
        this.component = arg.getKey().getName();
        this.desc = arg.getDescription();
    }

    public ComponentResult(Component arg, String baseURL) {
        this(arg);
        this.links = new ArrayList<>();
        this.links.add(new Link(URI.create(baseURL + getUriOf(arg)), "self"));
    }

    public ComponentResult(Component arg, List<String> errors) {
        this(arg);
        this.errors = errors;
    }

    private static String getUriOf(Component arg) {
        return "/product/" + arg.getKey().getProduct() + "/component/" + arg.getKey().getName();
    }

    @XmlElement(name="product")
    public String getProduct() {
        return product;
    }

    @XmlElement(name="name")
    public String getComponent() {
        return component;
    }

    @XmlElement(name="desc")
    public String getDesc() {
        return desc;
    }

    @XmlElementWrapper(name="errors") @XmlElement(name="error")
    public List<String> getErrors() {
        return errors;
    }

    @XmlElementWrapper(name="links") @XmlElement(name="link")
    public List<Link> getLinks() {
        return links;
    }
}
