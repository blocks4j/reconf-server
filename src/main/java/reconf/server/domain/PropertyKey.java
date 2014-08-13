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
    private Scope scope = Scope.global;
    private String target = StringUtils.EMPTY;

    public PropertyKey() {
    }

    public PropertyKey(String product, String component, String name) {
        setProduct(product);
        setComponent(component);
        setName(name);
    }

    @Column(length=256, name="property_name")
    @NotBlank @NotNull @Size(min=1, max=256) @Pattern(regexp="\\w*")
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = StringUtils.lowerCase(StringUtils.defaultString(name));
    }

    @Column(length=256, name="product_name")
    @NotBlank @NotNull @Size(min=1, max=256) @Pattern(regexp="\\w*")
    public String getProduct() {
        return product;
    }
    public void setProduct(String product) {
        this.product = StringUtils.lowerCase(StringUtils.defaultString(product));
    }

    @Column(length=256, name="component_name")
    @NotBlank @NotNull @Size(min=1, max=256) @Pattern(regexp="\\w*")
    public String getComponent() {
        return component;
    }
    public void setComponent(String component) {
        this.component = StringUtils.lowerCase(StringUtils.defaultString(component));
    }

    @Column(length=128, name="scope") @Enumerated(EnumType.STRING)
    @NotNull
    public Scope getScope() {
        return scope;
    }
    public void setScope(Scope scope) {
        this.scope = scope;
    }

    @Column(length=256, name="target_name")
    public String getTarget() {
        return target;
    }
    public void setTarget(String target) {
        this.target = target;
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
            .isEquals();
    }
}
