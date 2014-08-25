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
package reconf.server;

import org.springframework.http.*;

public final class ReConfConstants {

    /**
     * Media Type
     */
    public static final String MT_PROTOCOL_V1 = "application/vnd.reconf-v1+text";
    public static final String MT_APPLICATION_JSON = MediaType.APPLICATION_JSON_VALUE;
    public static final String MT_TEXT_PLAIN = MediaType.TEXT_PLAIN_VALUE;
    public static final String MT_ALL = MediaType.ALL_VALUE;

    /**
     * Headers
     */
    public static final String H_RESPONSE_RESULT = "X-ReConf-Result";
    public static final String H_RESPONSE_RULE = "X-ReConf-Rule";
    public static final String H_REQUEST_URL = "X-ReConf-URL";

    /**
     * Security
     */
    public static final String SERVER_ROOT_USER = "reconf";

    /**
     * Rest
     */
    public static final String CRUD_ROOT = "/crud";

    /**
     * Flyway
     */
    public static final int DB_VERSION = 2;
}
