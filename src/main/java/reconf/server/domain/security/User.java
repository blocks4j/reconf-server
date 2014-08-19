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
@Table(name="users")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    private String username;
    private String password;
    private boolean enabled;

    @Id @Column(name="username", length=256)
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name="password", length=256)
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name="enabled")
    public boolean isEnabled() {
        return enabled;
    }
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public int hashCode() {
        if (username == null) {
            return super.hashCode();
        }
        return username.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null || !(obj instanceof User)) {
            return false;
        }
        User rhs = (User) obj;
        if (rhs.username == null) {
            return false;
        }
        return StringUtils.equalsIgnoreCase(username, rhs.username);
    }

}
