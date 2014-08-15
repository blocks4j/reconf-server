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
package reconf.server.services;

import java.util.*;
import java.util.Map.Entry;
import javax.script.*;
import org.springframework.stereotype.*;

@Service
public class JavaScriptEngine {


    public Object eval(String js, Map<String, Object> params, String resultVariableName) {
        try {
            ScriptEngineManager factory = new ScriptEngineManager();
            ScriptEngine engine = factory.getEngineByName("JavaScript");
            for (Entry<String, Object> each : params.entrySet()) {
                engine.put(each.getKey(), each.getValue());
            }
            engine.eval(js);
            return engine.get(resultVariableName);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
