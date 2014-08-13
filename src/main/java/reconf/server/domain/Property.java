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
import java.util.*;
import javax.persistence.*;

@Entity
@Table(name="reconf_property_v1")
public class Property implements Serializable {

    public static final String NAME_MESSAGE = "property name must match [a-zA-Z_0-9]{3,256}";
    public static final String VALUE_MESSAGE = "property value must not be empty";
    public static final List<String> NOT_FOUND = Collections.singletonList("property not found");

    private static final long serialVersionUID = 1L;
    private PropertyKey key;
    private String value;
    private String description;

    public Property() { }

    public Property(PropertyKey key) {
        this.key = key;
    }

    public Property(PropertyKey key, String value) {
        this.key = key;
        this.value = value;
    }

    public Property(PropertyKey key, String value, String description) {
        this.key = key;
        this.value = value;
        this.description = description;
    }

    @EmbeddedId
    public PropertyKey getKey() {
        return key;
    }
    public void setKey(PropertyKey key) {
        this.key = key;
    }

    @Column(nullable=false, name="property_value", length=Integer.MAX_VALUE)
    @Lob
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }

    @Column(nullable=true, length=4096, name="property_desc")
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
        if(obj == null || !(obj instanceof Property)) {
            return false;
        }
        Property rhs = (Property) obj;
        if (rhs.key == null) {
            return false;
        }
        return key.equals(rhs.key);
    }
}
