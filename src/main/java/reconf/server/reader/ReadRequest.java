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
package reconf.server.reader;

import javax.ws.rs.*;
import org.apache.commons.lang3.*;
import org.apache.commons.lang3.builder.*;

public class ReadRequest {

    private String product;
    private String component;
    private String property;
    private String value;
    private String instance;
    private String pool;

    public String getProduct() {
        return product;
    }
    @PathParam("product")
    public void setProduct(String product) {
        this.product = StringUtils.lowerCase(product);
    }

    public String getComponent() {
        return component;
    }
    @PathParam("component")
    public void setComponent(String component) {
        this.component = StringUtils.lowerCase(component);
    }

    public String getInstance() {
        return instance;
    }
    @QueryParam("instance")
    public void setInstance(String instance) {
        this.instance = StringUtils.lowerCase(instance);
    }

    public String getProperty() {
        return property;
    }
    @PathParam("property")
    public void setProperty(String property) {
        this.property = StringUtils.lowerCase(property);
    }

    public String getValue() {
		return value;
	}    
    public void setValue(String value) {
        this.value = StringUtils.lowerCase(value);
    }
    
    public String getPool() {
        return pool;
    }
    public void setPool(String pool) {
        this.pool = StringUtils.lowerCase(pool);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("product", product)
        .append("component", component)
        .append("property", property)
        .append("value", value)
        .append("instance", instance)
        .append("pool", pool)
        .toString();
    }
	
}
