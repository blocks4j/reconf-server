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
    private String cluster;
    private String instance;

    public PropertyKey() {
    }

    public PropertyKey(String product, String component, String name, String instance, String cluster) {
        setProduct(product);
        setComponent(component);
        setName(name);
        setInstance(instance);
        setCluster(cluster);
    }

    public PropertyKey(String product, String component, String name, String instance) {
        setProduct(product);
        setComponent(component);
        setName(name);
        setInstance(instance);
        setCluster(StringUtils.EMPTY);
    }

    public PropertyKey(String product, String component, String name) {
        setProduct(product);
        setComponent(component);
        setName(name);
        setInstance(StringUtils.EMPTY);
        setCluster(StringUtils.EMPTY);
    }


    @Column(length=256, name="property_name")
    @NotBlank @NotNull @Size(min=1, max=256)
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = StringUtils.lowerCase(StringUtils.defaultString(name));
    }

    @Column(length=256, name="product_name")
    @NotBlank @NotNull @Size(min=1, max=256)
    public String getProduct() {
        return product;
    }
    public void setProduct(String product) {
        this.product = StringUtils.lowerCase(StringUtils.defaultString(product));
    }

    @Column(length=256, name="component_name")
    @NotBlank @NotNull @Size(min=1, max=256)
    public String getComponent() {
        return component;
    }
    public void setComponent(String component) {
        this.component = StringUtils.lowerCase(StringUtils.defaultString(component));
    }

    @Column(length=256, name="cluster_name")
    @Size(min=0, max=256)
    public String getCluster() {
        return cluster;
    }
    public void setCluster(String cluster) {
        this.cluster = StringUtils.lowerCase(StringUtils.defaultString(cluster));
    }

    @Column(length=256, name="instance_name")
    @Size(min=0, max=256)
    public String getInstance() {
        return instance;
    }
    public void setInstance(String instance) {
        this.instance = StringUtils.lowerCase(StringUtils.defaultString(instance));
    }

    @Override
    public int hashCode() {
        if (product == null || component == null | name == null) {
            return super.hashCode();
        }
        return new HashCodeBuilder()
            .append(product)
            .append(component)
            .append(name)
            .append(cluster)
            .append(instance)
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
            .append(cluster, rhs.cluster)
            .append(instance, rhs.instance)
            .isEquals();
    }
}
