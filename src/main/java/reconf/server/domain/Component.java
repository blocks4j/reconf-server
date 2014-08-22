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

@Entity
@Table(name="reconf_component")
public class Component {

    public static final String NAME_MESSAGE = "component must match [a-zA-Z_0-9]{3,256}";
    public static final List<String> NOT_FOUND = Collections.singletonList("component not found");

    private ComponentKey key;
    private String description;

    public Component() { }

    public Component(ComponentKey key) {
        this.key = key;
    }

    public Component(ComponentKey key, String description) {
        this.key = key;
        this.description = description;
    }

    @EmbeddedId
    public ComponentKey getKey() {
        return key;
    }
    public void setKey(ComponentKey key) {
        this.key = key;
    }

    @Column(nullable=true, length=4096, name="component_desc")
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
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
        if(obj == null || !(obj instanceof Component)) {
            return false;
        }
        Component rhs = (Component) obj;
        if (rhs.key == null) {
            return false;
        }
        return key.equals(rhs.key);
    }
}
