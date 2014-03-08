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

	private Map<String, Product> mapProduct = new HashMap<>();

	{
	    Product prod = new Product();
	    prod.setName("teste");
	    prod.setDescription("teste desc");
		this.insert("teste", prod);

		Component comp = new Component();
		comp.setName("a");
		this.insert("teste","a",comp);

		Property p = new Property();
		p.setName("c");
		p.setValue("funciona");
		this.upsert("teste", "a", "c", p);

        p = new Property();
        p.setName("p1");
        p.setValue("testando");
        this.upsert("teste", "a", "p1", p);


	}

    @Override
    public Property upsert(String productName, String componentName, String propertyName, Property value) {
    	Property p = new Property(value);
    	p.setDescription(propertyName);
        p.setComponent(componentName);
        p.setProduct(productName);

    	if(!map.containsKey(productName) || !mapProduct.containsKey(productName) || !mapComponent.containsKey(productName)){
    		return null;
    	}
    	Map<String, Map<String,Property>>  mapComp = map.get(productName);
    	Map<String, Component> mapComponents = mapComponent.get(productName);

    	if(!mapComp.containsKey(componentName) || !mapComponents.containsKey(componentName))	{
    		return null;
    	}
    	Map<String, Property>  mapConf = mapComp.get(componentName);
    	Component component = mapComponents.get(componentName);

		mapConf.put(propertyName, p);
		component.getProperties().add(p);
		return p;

    }

    @Override
    public void insert(String product, String component, Component comp) {
    	if(!map.containsKey(product) || !mapProduct.containsKey(product) || !mapComponent.containsKey(product)){
    		throw new RuntimeException("NOT FOUND 1");
    	}
    	Map<String, Map<String,Property>>  mapComp = map.get(product);
    	Product prod = mapProduct.get(product);
    	Map<String, Component> mapComponentObj = mapComponent.get(product);

    	if(!mapComp.containsKey(component) && !mapComponentObj.containsKey(component)){
    		mapComp.put(component, new HashMap<String, Property>());
    		mapComponentObj.put(component, comp);
    		prod.getComponents().add(comp);
    	}
    }

    @Override
    public void insert(String product, Product value) {
    	if(!map.containsKey(product) && !mapComponent.containsKey(product) && !mapProduct.containsKey(product)){
    		mapComponent.put(product, new HashMap<String, Component>());
    		map.put(product, new HashMap<String, Map<String, Property>>());
    		mapProduct.put(product, value);
    	}
    }

    @Override
    public Property get(String product, String component, String configuration) {

    	Map<String, Map<String,Property>>  mapComp = map.get(product);
    	if(mapComp == null){
    		return null;
    	}

    	Map<String, Property>  mapConf = mapComp.get(component);
    	if(mapConf == null){
    		return null;
    	}

        return mapConf.get(configuration);
    }

    @Override
    public Component get(String product, String component) {

    	Map<String, Map<String,Property>>  mapComp = map.get(product);
    	Map<String, Component> mapComponentObj = mapComponent.get(product);
    	if(mapComp == null || mapComponentObj == null){
    		return null;
    	}

    	Map<String, Property>  mapConf = mapComp.get(component);
    	if(mapConf == null){
    		return null;
    	}

    	Component result = mapComponentObj.get(component);
    	return result;

    }

    @Override
    public String get(String product) {
        return StringUtils.EMPTY;
    }

    @Override
    public boolean deleted(String productName, String componentName, String propertyName) {

        if(!map.containsKey(productName) || !mapProduct.containsKey(productName) || !mapComponent.containsKey(productName)){
            return false;
        }
        Map<String, Map<String,Property>>  mapComp = map.get(productName);
        Map<String, Component> mapComponents = mapComponent.get(productName);

        if(!mapComp.containsKey(componentName) || !mapComponents.containsKey(componentName))    {
            return false;
        }
        Map<String, Property>  mapConf = mapComp.get(componentName);
        Component component = mapComponents.get(componentName);

        if(!mapConf.containsKey(propertyName)){
            return false;
        }

        Property p = mapConf.get(propertyName);

        component.getProperties().remove(p);
        mapConf.remove(propertyName);

        return true;
    }

    @Override
    public boolean deleted(String product, String component) {
        return false;
    }

    @Override
    public boolean deleted(String product) {
        return false;
    }

}
