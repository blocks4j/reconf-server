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
package reconf.server.domain.result;

import java.util.*;
import reconf.server.domain.*;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class PropertyRuleResult extends PropertyResult {

    private Rule rule;

    private PropertyRuleResult(Property arg) {
        super(arg);
        this.rule = new Rule(arg);
    }

    public PropertyRuleResult(Property arg, String baseURL) {
        super(arg, baseURL);
        this.rule = new Rule(arg);
    }

    public PropertyRuleResult(Property arg, List<String> errors) {
        super(arg, errors);
        this.rule = new Rule(arg);
    }

    public Rule getRule() {
        return rule;
    }

    protected String getSelfUri() {
        if (rule != null) {
            return super.getSelfUri() + "/rule/" + rule.getName();
        }
        return super.getSelfUri();
    }

    public void clearRule() {
        this.rule = null;
    }
}
