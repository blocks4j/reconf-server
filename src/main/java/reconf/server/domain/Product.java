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

import java.util.*;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.apache.commons.lang3.*;
import org.apache.commons.lang3.builder.*;
import org.hibernate.validator.constraints.*;

@Entity
@Table(name="reconf_product")
public class Product {

    public static final String ROOT_MESSAGE = "reconf user required";
    public static final String NAME_MESSAGE = "product must match [a-zA-Z_0-9]{3,256}";
    public static final List<String> NOT_FOUND = Collections.singletonList("product not found");
    private String name;
    private String description;
    private List<String> users;

    public Product() { }

    public Product(String name) {
        this.name = name;
    }

    public Product(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Id @Column(length=256, name="product_name")
    @NotBlank(message=Product.NAME_MESSAGE)
    @NotNull(message=Product.NAME_MESSAGE)
    @Size(min=3, max=256, message=Product.NAME_MESSAGE)
    @Pattern(regexp="\\w*", message=Product.NAME_MESSAGE)
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = StringUtils.lowerCase(StringUtils.defaultString(name));
    }

    @Column(nullable=true, length=4096, name="product_desc")
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    @Transient
    public List<String> getUsers() {
        return users;
    }
    public void setUsers(List<String> users) {
        this.users = users;
    }
    public void addUser(String user) {
        if (this.users == null) {
            this.users = new ArrayList<>();
        }
        this.users.add(user);
    }

    @Override
    public int hashCode() {
        if (name == null) {
            return super.hashCode();
        }
        return new HashCodeBuilder()
            .append(name)
            .hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Product)) {
            return false;
        }
        Product rhs = (Product) obj;
        return new EqualsBuilder()
            .append(name, rhs.name)
            .isEquals();
    }
}
