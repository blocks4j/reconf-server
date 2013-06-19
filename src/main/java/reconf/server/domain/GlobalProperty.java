/*
 *    Copyright 1996-2013 UOL Inc
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
package reconf.server.domain;

import java.io.*;
import javax.persistence.*;
import org.apache.commons.lang3.*;

@Entity @Table(name="global_property")
public class GlobalProperty implements Serializable {

    private static final long serialVersionUID = 1L;
    private String component;
    private String product;
    private String name;
    private String description;
    private String value;
    private Integer version;

    public String getComponent() {
        return component;
    }
    public void setComponent(String component) {
        this.component = StringUtils.lowerCase(component);
    }

    public String getProduct() {
        return product;
    }
    public void setProduct(String product) {
        this.product = StringUtils.lowerCase(product);
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = StringUtils.lowerCase(name);
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }

    @Version
    public Integer getVersion() {
        return version;
    }
    public void setVersion(Integer version) {
        this.version = version;
    }
}
