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
package reconf.server.domain.security;

import java.util.*;
import javax.validation.constraints.*;
import org.apache.commons.lang3.*;
import org.hibernate.validator.constraints.*;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Client {

    public static final String NAME_MESSAGE = "username must match [a-zA-Z_0-9]{3,50}";
    public static final String PASS_MESSAGE = "password must match [.*]{3,50}";
    private String username;
    private String password;
    private List<String> errors;

    public Client() { }

    public Client(String username, List<String> errors) {
        this.username = username;
        this.errors = errors;
    }

    public Client(Client request) {
        this.username = request.getUsername();
        this.password = StringUtils.repeat("*", StringUtils.length(request.getPassword()));
    }

    public Client(Client request, List<String> errors) {
        this(request);
        this.errors = errors;
    }

    @NotBlank(message=Client.NAME_MESSAGE)
    @NotNull(message=Client.NAME_MESSAGE)
    @Size(min=3, max=50, message=Client.NAME_MESSAGE)
    @Pattern(regexp="\\w*", message=Client.NAME_MESSAGE)
    public String getUsername() {
        return StringUtils.lowerCase(username);
    }
    public void setUsername(String username) {
        this.username = StringUtils.lowerCase(username);
    }

    @NotBlank(message=Client.PASS_MESSAGE)
    @NotNull(message=Client.PASS_MESSAGE)
    @Size(min=3, max=50, message=Client.PASS_MESSAGE)
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getErrors() {
        return errors;
    }
    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
