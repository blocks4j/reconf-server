package reconf.server.persistence;

import java.io.*;
import java.util.*;
import reconf.server.domain.*;

public class SimpleConfigurationRepository implements ConfigurationRepository, Serializable {

	private static final long serialVersionUID = 1L;

    private Map<String, Map<String, Map<String,Property>>> map = new HashMap<>();

	private Map<String, Map<String, Component>> mapComponent = new HashMap<>();

	private Map<String, Product> mapProduct = new HashMap<>();

	{
	    this.load();
	}

	protected void save(){
	    try (
            OutputStream file = new FileOutputStream("repository.ser");
            OutputStream buffer = new BufferedOutputStream(file);
            ObjectOutput output = new ObjectOutputStream(buffer);
          )
          {
            output.writeObject(this);
          } catch(IOException ex) {
              System.err.println("Cannot save. " + ex);
          }
	}

	protected void load(){
	    try(
            InputStream file = new FileInputStream("repository.ser");
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream (buffer);
         ){
            SimpleConfigurationRepository loaded = (SimpleConfigurationRepository)input.readObject();
            this.map.clear();
            this.map.putAll(loaded.map);
            this.mapComponent.clear();
            this.mapComponent.putAll(loaded.mapComponent);
            this.mapProduct.clear();
            this.mapProduct.putAll(loaded.mapProduct);
         } catch(ClassNotFoundException ex){
            System.err.println("Cannot load. Class not found." + ex);
         } catch(IOException ex){
            System.err.println("Cannot perform input." + ex);
          }
	}


    @Override
    public Property upsert(String productName, String componentName, String propertyName, Property value) {
    	Property p = new Property(value);
    	p.setName(propertyName);
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

		if(!component.getProperties().add(p)){
		    component.getProperties().remove(p);
		    if(!component.getProperties().add(p)){
		        throw new RuntimeException("NOT CHANGED");
		    }

		}
		mapConf.put(propertyName, p);

		this.save();

		return p;

    }

    @Override
    public Component upsert(String product, String componentName, Component component) {
        Component comp = new Component(component);
        comp.setName(componentName);
        comp.setProduct(product);

    	if(!map.containsKey(product) || !mapProduct.containsKey(product) || !mapComponent.containsKey(product)){
    		return null;
    	}
    	Map<String, Map<String,Property>>  mapComp = map.get(product);
    	Product prod = mapProduct.get(product);
    	Map<String, Component> mapComponentObj = mapComponent.get(product);

    	Component previousComp = mapComponentObj.get(componentName);

    	if(previousComp != null ){
    	    for (Property p : previousComp.getProperties()) {
    	        try{
    	            deleted(product,componentName,p.getName());
    	        }catch(RuntimeException re){

    	        }
    	    }
    	    prod.getComponents().remove(previousComp);
    	}

		mapComp.put(componentName, new HashMap<String, Property>());
		mapComponentObj.put(componentName, comp);
        if(!prod.getComponents().add(comp)){
            throw new RuntimeException("NOT CHANGED");
        }

        Set<Property> properties = new HashSet<>();
        properties.addAll(comp.getProperties());
        comp.getProperties().clear();
		for (Property p : properties) {
		    try{
		        upsert(product, componentName, p.getName(), p);
		    }catch (RuntimeException re){

		    }
        }

		this.save();

		return comp;

    }

    @Override
    public Product upsert(String productName, Product value) {
        value.setName(productName);
    	//if(!map.containsKey(product) && !mapComponent.containsKey(product) && !mapProduct.containsKey(product)){
		Product previousProduct = mapProduct.get(productName);
        if(previousProduct != null){
            for (Component c : previousProduct.getComponents()) {
                try{
                    deleted(productName, c.getName());
                }catch(RuntimeException re){

                }
            }
        }
        mapComponent.put(productName, new HashMap<String, Component>());
		map.put(productName, new HashMap<String, Map<String, Property>>());
		mapProduct.put(productName, value);
		Collection<Component> components = new HashSet<>();
		components.addAll(value.getComponents());
		value.getComponents().clear();
		for (Component c : components) {
            try{
                System.out.println("upsert("+productName+","+c.getName()+","+c+")");
                upsert(productName,c.getName(),c);
            }catch(RuntimeException re){
            }
        }

		this.save();

		return value;

    }

    @Override
    public Property get(String product, String component, String configuration) {

    	Map<String, Map<String,Property>>  mapComp = map.get(product);
    	if(mapComp == null){
    	    System.out.println("NOT 0");
    		return null;
    	}

    	Map<String, Property>  mapConf = mapComp.get(component);
    	if(mapConf == null){
    	    System.out.println("NOT 1");
    		return null;
    	}


    	Property result = mapConf.get(configuration);
    	if(result == null){
            System.out.println("NOT 2");
        }

        return result;
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
    public Product get(String product) {

        Map<String, Map<String,Property>>  mapComp = map.get(product);
        Map<String, Component> mapComponentObj = mapComponent.get(product);
        Product prod = mapProduct.get(product);
        if(prod == null || mapComp == null || mapComponentObj == null){
            return null;
        }

        return prod;

    }

    @Override
    public Collection<Product> getProducts() {

        if(mapProduct == null || map == null || map.isEmpty() || mapComponent == null || mapComponent.isEmpty()){
            return null;
        }

        return mapProduct.values();

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

        this.save();

        return true;
    }

    @Override
    public boolean deleted(String productName, String componentName) {
        if(!map.containsKey(productName) || !mapProduct.containsKey(productName) || !mapComponent.containsKey(productName)){
            return false;
        }
        Product product = mapProduct.get(productName);
        Map<String, Map<String,Property>>  mapComp = map.get(productName);
        Map<String, Component> mapComponents = mapComponent.get(productName);

        if(!mapComp.containsKey(componentName) || !mapComponents.containsKey(componentName))    {
            return false;
        }
        Map<String, Property>  mapConf = mapComp.get(componentName);
        Component component = mapComponents.get(componentName);

        if(mapConf.size() > 0 || component.getProperties().size()>0){
            throw new RuntimeException("INVALID");
        }

        product.getComponents().remove(component);
        mapComp.remove(componentName);
        mapComponents.remove(componentName);
        mapComponent.remove(componentName);

        this.save();

        return true;
    }

    @Override
    public boolean deleted(String productName) {
        if(!map.containsKey(productName) || !mapProduct.containsKey(productName) || !mapComponent.containsKey(productName)){
            return false;
        }
        Product product = mapProduct.get(productName);
        Map<String, Map<String,Property>>  mapComp = map.get(productName);
        Map<String, Component> mapComponents = mapComponent.get(productName);

        if(mapComp.size() > 0 || mapComponents.size() > 0 || product.getComponents().size()>0){
            throw new RuntimeException("INVALID");
        }

        mapProduct.remove(productName);
        mapComponent.remove(productName);
        map.remove(productName);

        this.save();

        return true;
    }

}
