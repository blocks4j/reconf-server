/*
 *    Copyright 1996-2014 UOL Inc
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
package reconf.server.services.property;

import javax.validation.*;
import reconf.server.domain.*;

public class PropertyValidator {

    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public static boolean containsErrors(PropertyKey key) {
        if (key == null) {
            return true;
        }
        return validator.validate(key).size() > 0;
    }
}
