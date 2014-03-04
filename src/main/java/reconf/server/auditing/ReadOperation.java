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
package reconf.server.auditing;

import java.util.*;
import javax.servlet.http.*;
import javax.ws.rs.core.*;
import org.apache.commons.collections.*;
import org.apache.commons.lang3.*;

public class ReadOperation {

    private String requestUser;
    private HttpServletRequest request;

    public String getRequestUser() {
        return requestUser;
    }

    public void setRequestUser(String requestUser) {
        this.requestUser = StringUtils.defaultString(StringUtils.trim(requestUser));
    }

    public String getRequestIp() {
        return request.getRemoteAddr();
    }

    public Map<String, String> getRequestHeaders() {
        Map<String, String> result = new LinkedHashMap<>();
        List<String> headers = EnumerationUtils.toList(request.getHeaderNames());
        for (String header : headers) {
            result.put(header, request.getHeader(header));
        }
        return result;
    }

    public HttpServletRequest getRequest() {
        return request;
    }
    @Context
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }
}
