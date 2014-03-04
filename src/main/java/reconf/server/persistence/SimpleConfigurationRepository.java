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

import java.util.*;
import org.apache.commons.lang3.*;
import reconf.server.domain.*;

public class SimpleConfigurationRepository implements ConfigurationRepository {

	private Map<String, Map<String, Map<String,Property>>> map = new HashMap<>();

	private Map<String, Map<String, Component>> mapComponent = new HashMap<>();

//	private Map<String, Product> mapProduct = new HashMap<>();



	{
		this.insert("teste");
		Component comp = new Component();
		comp.setName("a");
		this.insert("teste","a",comp);
		Property p = new Property();
		p.setName("c");
		p.setValue("funciona");
		this.upsert("teste", "a", "c", p);
	}

    @Override
    public void upsert(String productName, String componentName, String propertyName, Property value) {
    	Property p = new Property(value);
    	p.setDescription(propertyName);
        p.setComponent(componentName);
        p.setProduct(productName);

    	if(!map.containsKey(productName)){
    		throw new RuntimeException("NOT FOUND 1");
    	}
    	Map<String, Map<String,Property>>  mapComp = map.get(productName);

    	if(!mapComp.containsKey(componentName))	{
    		throw new RuntimeException("NOT FOUND 2");
    	}
    	Map<String, Property>  mapConf = mapComp.get(componentName);

		mapConf.put(propertyName, p);

    }

    @Override
    public void insert(String product, String component, Component comp) {
    	if(!map.containsKey(product) || !mapComponent.containsKey(product)){
    		throw new RuntimeException("NOT FOUND 1");
    	}
    	Map<String, Map<String,Property>>  mapComp = map.get(product);
    	Map<String, Component> mapComponentObj = mapComponent.get(product);
    	if(!mapComp.containsKey(component) && !mapComponentObj.containsKey(component)){
    		mapComp.put(component, new HashMap<String, Property>());
    		mapComponentObj.put(component, comp);
    	}
    }

    @Override
    public void insert(String product) {
    	if(!map.containsKey(product) && !mapComponent.containsKey(product) ){
    		mapComponent.put(product, new HashMap<String, Component>());
    		map.put(product, new HashMap<String, Map<String, Property>>());

    	}
    }

    @Override
    public Property get(String product, String component, String configuration) {

    	Map<String, Map<String,Property>>  mapComp = map.get(product);
    	if(mapComp == null){
    		throw new RuntimeException("NOT FOUND");
    	}

    	Map<String, Property>  mapConf = mapComp.get(component);
    	if(mapConf == null){
    		throw new RuntimeException("NOT FOUND");
    	}

        return mapConf.get(configuration);
    }

    @Override
    public Component get(String product, String component) {

    	Map<String, Map<String,Property>>  mapComp = map.get(product);
    	Map<String, Component> mapComponentObj = mapComponent.get(product);
    	if(mapComp == null || mapComponentObj == null){
    		throw new RuntimeException("NOT FOUND");
    	}

    	Map<String, Property>  mapConf = mapComp.get(component);
    	if(mapConf == null){
    		throw new RuntimeException("NOT FOUND");
    	}

    	Component result = mapComponentObj.get(component);
    	if(result == null){
    		return null;
    	}

    	result.setProperties(mapConf.values());

        return result;
    }

    @Override
    public String get(String product) {
        return StringUtils.EMPTY;
    }

    @Override
    public void delete(String product, String component, String configuration) {
    }

    @Override
    public void delete(String product, String component) {
    }

    @Override
    public void delete(String product) {
    }

}
