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
package reconf.server.domain;

import java.io.*;
import javax.persistence.*;
import org.apache.commons.lang3.*;
import org.apache.commons.lang3.builder.*;

@Embeddable
public class PropertyKey implements Serializable {

    private static final long serialVersionUID = 1L;
    private String name;
    private String product;
    private String component;
    private String cluster;
    private String hostName;

    public PropertyKey() {
    }

    public PropertyKey(String product, String component, String name, String hostName, String cluster) {
        setProduct(product);
        setComponent(component);
        setName(name);
        setHostName(hostName);
        setCluster(cluster);
    }

    public PropertyKey(String product, String component, String name, String hostName) {
        setProduct(product);
        setComponent(component);
        setName(name);
        setHostName(hostName);
        setCluster(StringUtils.EMPTY);
    }

    public PropertyKey(String product, String component, String name) {
        setProduct(product);
        setComponent(component);
        setName(name);
        setHostName(StringUtils.EMPTY);
        setCluster(StringUtils.EMPTY);
    }


    @Column(length=256, name="property_name")
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = StringUtils.lowerCase(StringUtils.defaultString(name));
    }

    @Column(length=256, name="product_name")
    public String getProduct() {
        return product;
    }
    public void setProduct(String product) {
        this.product = StringUtils.lowerCase(StringUtils.defaultString(product));
    }

    @Column(length=256, name="component_name")
    public String getComponent() {
        return component;
    }
    public void setComponent(String component) {
        this.component = StringUtils.lowerCase(StringUtils.defaultString(component));
    }

    @Column(length=256, name="cluster_name")
    public String getCluster() {
        return cluster;
    }
    public void setCluster(String cluster) {
        this.cluster = StringUtils.lowerCase(StringUtils.defaultString(cluster));
    }

    @Column(length=256, name="host_name")
    public String getHostName() {
        return hostName;
    }
    public void setHostName(String hostName) {
        this.hostName = StringUtils.lowerCase(StringUtils.defaultString(hostName));
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(product)
            .append(component)
            .append(name)
            .append(cluster)
            .append(hostName)
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
            .append(hostName, rhs.hostName)
            .isEquals();
    }
}
