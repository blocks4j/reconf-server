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
import org.apache.commons.lang3.*;

@Entity
@Table(name="authorities")
public class Authority implements Serializable {

    private static final long serialVersionUID = 1L;
    private User user;
    private String authority;

    @Id
    @OneToOne @JoinColumn(name="username", referencedColumnName="username")
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    @Id @Column(name="authority")
    public String getAuthority() {
        return authority;
    }
    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public int hashCode() {
        if (user == null || authority == null) {
            return super.hashCode();
        }
        return user.hashCode() + authority.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null || !(obj instanceof Authority)) {
            return false;
        }
        Authority rhs = (Authority) obj;
        if (rhs.user == null) {
            return false;
        }
        return this.user.equals(rhs.user) && StringUtils.equalsIgnoreCase(authority, rhs.authority);
    }
}
