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

import javax.servlet.http.*;
import org.apache.commons.lang3.*;
import reconf.server.*;

public class CrudServiceUtils {

    public static String getBaseUrl(HttpServletRequest req) {
        if (StringUtils.isNotBlank(req.getHeader(ReConfConstants.H_REQUEST_URL))) {
            return req.getHeader(ReConfConstants.H_REQUEST_URL) + ReConfConstants.CRUD_ROOT;
        }

        String url = req.getRequestURL().toString();
        return StringUtils.replace(url, StringUtils.substringAfter(url, ReConfConstants.CRUD_ROOT), "");
    }
}
