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
package reconf.server.domain.security;

import java.io.*;
import javax.persistence.*;
import org.apache.commons.lang3.builder.*;

@Embeddable
public class UserProductKey implements Serializable {

    private static final long serialVersionUID = 1L;
    private String username;
    private String product;

    public UserProductKey() {
    }

    public UserProductKey(String username, String product) {
        this.username = username;
        this.product = product;
    }

    @Column(name="username", length=50)
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name="product_name", length=256)
    public String getProduct() {
        return product;
    }
    public void setProduct(String product) {
        this.product = product;
    }

    @Override
    public int hashCode() {
        if (product == null || username == null) {
            return super.hashCode();
        }
        return new HashCodeBuilder()
            .append(product)
            .append(username)
            .hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof UserProductKey)) {
            return false;
        }
        UserProductKey rhs = (UserProductKey) obj;
        return new EqualsBuilder()
            .append(product, rhs.product)
            .append(username, rhs.username)
            .isEquals();
    }
}
