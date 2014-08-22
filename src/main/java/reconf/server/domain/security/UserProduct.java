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

import java.util.*;
import javax.persistence.*;

@Entity
@Table(name="user_product")
public class UserProduct {

    private UserProductKey key;
    private Date date;

    public UserProduct() { }

    public UserProduct(UserProductKey key) {
        this.key = key;
        this.date = new Date();
    }

    @EmbeddedId
    public UserProductKey getKey() {
        return key;
    }
    public void setKey(UserProductKey key) {
        this.key = key;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="insertion_date")
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public int hashCode() {
        if (key == null) {
            return super.hashCode();
        }
        return key.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null || !(obj instanceof UserProduct)) {
            return false;
        }
        UserProduct rhs = (UserProduct) obj;
        if (rhs.key == null) {
            return false;
        }
        return key.equals(rhs.key);
    }
}
