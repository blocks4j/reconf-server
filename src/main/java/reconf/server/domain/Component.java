package reconf.server.domain;

import java.io.*;
import java.util.*;
import javax.xml.bind.annotation.*;


@XmlRootElement
public class Component implements Serializable {

    private static final long serialVersionUID = 1L;

    private String product;
    private String name;
    private String description;
    private Set<Property> properties = new HashSet<>();
    private Collection<Relation> relations = new ArrayList<>();

    public Component(Component p) {
    	this.product = p.product;
		this.name = p.name;
		this.description = p.description;
		this.properties.clear();
		this.properties.addAll(p.properties);
	}

	public Component() {
	}

	@XmlElement
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@XmlElement
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	@XmlElement
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}

	@XmlElement(name="properties")
	public Set<Property> getProperties() {
		return properties;
	}
	public void setProperties(Set<Property> properties) {
	    if (properties != null) {
	        this.properties.clear();
	        this.properties.addAll(properties);
	    }
	}

    @XmlElement(name="atom.link")
    public Collection<Relation> getRelations() {
        return relations;
    }
    public void setRelations(Collection<Relation> relations) {
        if (relations != null) {
            this.relations.clear();
            this.relations.addAll(relations);
        }
    }

    @Override
    public int hashCode() {
        String s = this.product + ":" + this.name;
        return s.hashCode();
    }


    @Override
    public boolean equals(Object obj) {
        if(! (obj instanceof Component)){
            return false;
        }
        Component p = (Component) obj;
        if(!this.product.equals(p.product)){
            return false;
        }
        if(!this.name.equals(p.name)){
            return false;
        }
        return true;
    }

}