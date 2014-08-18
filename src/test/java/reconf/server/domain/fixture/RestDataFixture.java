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
package reconf.server.domain.fixture;

import org.apache.commons.lang3.*;
import reconf.server.domain.*;

public class RestDataFixture {

    public static final String PROPERTY_KEY_PRODUCT = "sample_prod";
    public static final String PROPERTY_KEY_COMPONENT = "sample_comp";
    public static final String PROPERTY_KEY_NAME = "sample_prop";
    public static final String PROPERTY_VALUE = "'sample value'";
    public static final String EMPTY_VALUE = StringUtils.EMPTY;

    public static PropertyKey standardPropertyKey() {
        PropertyKey propertyKey = new PropertyKey();

        propertyKey.setProduct(PROPERTY_KEY_PRODUCT);
        propertyKey.setComponent(PROPERTY_KEY_COMPONENT);
        propertyKey.setName(PROPERTY_KEY_NAME);
        propertyKey.setRuleName(Property.DEFAULT_RULE_NAME);
        return propertyKey;
    }

    public static Property standardProperty() {
        Property property = new Property();

        property.setKey(standardPropertyKey());
        property.setValue(PROPERTY_VALUE);
        property.setRulePriority(Property.DEFAULT_RULE_PRIORITY_INT);
        property.setRuleRegexp(Property.DEFAULT_RULE_REGEXP);

        return property;
    }
}
