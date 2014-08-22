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
import javax.validation.*;
import javax.validation.constraints.*;
import org.hibernate.validator.constraints.*;

@Entity
@Table(name="reconf_property")
public class Property implements Serializable {

    public static final List<String> GLOBAL_NOT_FOUND = Collections.singletonList("global property not found");
    public static final List<String> GLOBAL_UPDATE = Collections.singletonList("global property cannot be updated using this method");
    public static final String NAME_MESSAGE = "property name must match [a-zA-Z_0-9]{3,256}";
    public static final String VALUE_MESSAGE = "property value must not be empty";
    public static final String DESC_MESSAGE = "property desc must be a valid string with a maximum size of 4096 characters";
    public static final String RULE_NAME_MESSAGE = "rule name must match [a-zA-Z_0-9]{1,256}";
    public static final String RULE_REGEXP_MESSAGE = "rule expression must be a valid string with a maximum size of 256 characters";
    public static final String RULE_PRIORITY_MESSAGE = "rule priority must be an integer bigger than 0 and lower than 2^31-1";
    public static final String DEFAULT_RULE_NAME = "global";
    public static final String DEFAULT_RULE_PRIORITY = "0";
    public static final int DEFAULT_RULE_PRIORITY_INT = Integer.parseInt(DEFAULT_RULE_PRIORITY);
    public static final String DEFAULT_RULE_REGEXP = "/.*/";

    public static final List<String> NOT_FOUND = Collections.singletonList("property not found");

    private static final long serialVersionUID = 1L;
    private PropertyKey key;
    private String value;
    private String ruleRegexp;
    private Integer rulePriority;
    private String description;

    public Property() { }

    public Property(PropertyKey key) {
        this(key, null, null, Integer.parseInt(DEFAULT_RULE_PRIORITY), DEFAULT_RULE_REGEXP);
    }

    public Property(PropertyKey key, String value) {
        this(key, value, null, Integer.parseInt(DEFAULT_RULE_PRIORITY), DEFAULT_RULE_REGEXP);
    }

    public Property(PropertyKey key, String value, String description) {
        this(key, value, description, Integer.parseInt(DEFAULT_RULE_PRIORITY), DEFAULT_RULE_REGEXP);
    }

    public Property(PropertyKey key, String value, String description, Integer rulePriority, String ruleRegexp) {
        this.key = key;
        this.value = value;
        this.description = description;
        this.rulePriority = rulePriority;
        this.ruleRegexp = ruleRegexp;
    }

    @EmbeddedId
    @Valid
    public PropertyKey getKey() {
        return key;
    }
    public void setKey(PropertyKey key) {
        this.key = key;
    }

    @Column(nullable=false, name="property_value", length=Integer.MAX_VALUE) @Lob
    @NotNull(message=Property.VALUE_MESSAGE)
    @NotBlank(message=Property.VALUE_MESSAGE)
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }

    @Column(nullable=true, length=4096, name="property_desc")
    @Size(max=4096, message=Property.DESC_MESSAGE)
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    @Column(length=256, name="rule_regexp")
    @NotBlank(message=Property.RULE_REGEXP_MESSAGE)
    @NotNull(message=Property.RULE_REGEXP_MESSAGE)
    @Size(min=1, max=256, message=Property.RULE_REGEXP_MESSAGE)
    public String getRuleRegexp() {
        return ruleRegexp;
    }
    public void setRuleRegexp(String ruleRegexp) {
        this.ruleRegexp = ruleRegexp;
    }

    @Column(name="rule_priority")
    @NotNull(message=Property.RULE_PRIORITY_MESSAGE)
    @Min(value=0, message=Property.RULE_PRIORITY_MESSAGE)
    @Max(value=Integer.MAX_VALUE, message=RULE_PRIORITY_MESSAGE)
    public Integer getRulePriority() {
        return rulePriority;
    }
    public void setRulePriority(Integer rulePriority) {
        this.rulePriority = rulePriority;
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
