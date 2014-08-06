/*
 *    Copyright 1996-2014 UOL Inc
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
import org.apache.commons.lang3.*;
import reconf.server.domain.*;

public class PropertyResult {

    private String property;
    private String product;
    private String component;
    private String hostName;
    private Link link;

    public PropertyResult(Property arg, String baseURL) {
        this.product = arg.getKey().getProduct();
        this.component = arg.getKey().getComponent();
        this.property = arg.getKey().getName();
        this.hostName = arg.getKey().getHostName();
        this.link = new Link(URI.create(baseURL + getUriOf(arg)), "alternate");
    }

    private static String getUriOf(Property property) {
        String result = "/" + property.getKey().getProduct() + "/" + property.getKey().getComponent() + "/" + property.getKey().getName();
        if (StringUtils.isNotBlank(property.getKey().getHostName())) {
            result += "?hostname=" + property.getKey().getHostName();
        }
        return result;
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

    public String getHostName() {
        return hostName;
    }

    public Link getLink() {
        return link;
    }
}
