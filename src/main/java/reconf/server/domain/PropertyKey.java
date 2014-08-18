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
package reconf.server.domain;

import java.io.*;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.apache.commons.lang3.*;
import org.apache.commons.lang3.builder.*;
import org.hibernate.validator.constraints.*;

@Embeddable
public class PropertyKey implements Serializable {

    private static final long serialVersionUID = 1L;
    private String name;
    private String product;
    private String component;
    private String ruleName;

    public PropertyKey() {
    }

    public PropertyKey(String product, String component, String name) {
        this(product, component, name, Property.DEFAULT_RULE_NAME);
    }

    public PropertyKey(String product, String component, String name, String ruleName) {
        setProduct(product);
        setComponent(component);
        setName(name);
        setRuleName(ruleName);
    }

    @Column(length=256, name="property_name")
    @NotBlank(message=Property.NAME_MESSAGE)
    @NotNull(message=Property.NAME_MESSAGE)
    @Size(min=3, max=256, message=Property.NAME_MESSAGE)
    @Pattern(regexp="\\w*", message=Property.NAME_MESSAGE)
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = StringUtils.lowerCase(StringUtils.defaultString(name));
    }

    @Column(length=256, name="product_name")
    @NotBlank(message=Product.NAME_MESSAGE)
    @NotNull(message=Product.NAME_MESSAGE)
    @Size(min=3, max=256, message=Product.NAME_MESSAGE)
    @Pattern(regexp="\\w*", message=Product.NAME_MESSAGE)
    public String getProduct() {
        return product;
    }
    public void setProduct(String product) {
        this.product = StringUtils.lowerCase(StringUtils.defaultString(product));
    }

    @Column(length=256, name="component_name")
    @NotBlank(message=Component.NAME_MESSAGE)
    @NotNull(message=Component.NAME_MESSAGE)
    @Size(min=3, max=256, message=Component.NAME_MESSAGE)
    @Pattern(regexp="\\w*", message=Component.NAME_MESSAGE)
    public String getComponent() {
        return component;
    }
    public void setComponent(String component) {
        this.component = StringUtils.lowerCase(StringUtils.defaultString(component));
    }

    @Column(length=256, name="rule_name")
    @NotBlank(message=Property.RULE_NAME_MESSAGE)
    @NotNull(message=Property.RULE_NAME_MESSAGE)
    @Size(min=1, max=256, message=Property.RULE_NAME_MESSAGE)
    @Pattern(regexp="\\w*", message=Property.RULE_NAME_MESSAGE)
    public String getRuleName() {
        return ruleName;
    }
    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    @Override
    public int hashCode() {
        if (product == null || component == null || name == null) {
            return super.hashCode();
        }
        return new HashCodeBuilder()
            .append(product)
            .append(component)
            .append(name)
            .append(ruleName)
            .hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof PropertyKey)) {
            return false;
        }
        PropertyKey rhs = (PropertyKey) obj;
        return new EqualsBuilder()
            .append(product, rhs.product)
            .append(component, rhs.component)
            .append(name, rhs.name)
            .append(ruleName, ruleName)
            .isEquals();
    }
}
