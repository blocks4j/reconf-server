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
package reconf.server.domain.result;

import java.net.*;

public class Link {

    private String rel;
    private URI href;

    public Link() { }

    public Link(URI href, String rel) {
        this.href = href;
        this.rel = rel;
    }

    public URI getHref() {
        return href;
    }
    public void setHref(URI href) {
        this.href = href;
    }

    public String getRel() {
        return rel;
    }
    public void setRel(String rel) {
        this.rel = rel;
    }
}
