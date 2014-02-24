/*
 *    Copyright 1996-2013 UOL Inc
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
package reconf.server.persistence;

import reconf.server.domain.Component;
import reconf.server.domain.Property;

public interface ConfigurationRepository {

    ConfigurationRepository DEFAULT = new SimpleConfigurationRepository();

    void upsert(String product, String component, String configuration, Property value);
    void insert(String product, String component, Component comp);
    void insert(String product);
    Property get(String product, String component, String configuration);
    Component get(String product, String component);
    String get(String product);
    void delete(String product, String component, String configuration);
    void delete(String product, String component);
    void delete(String product);
}
