package reconf.server.domain;

import java.io.*;
import java.util.*;
import javax.persistence.*;
import javax.xml.bind.annotation.*;

@XmlRootElement
@Access(AccessType.PROPERTY)
public class Property implements Serializable{

    private static final long serialVersionUID = 1L;

    private String name;
    private String product;
    private String component;
    private String value;
    private String description;
    private Collection<Relation> relations = new ArrayList<>();

    public Property(Property p) {
    	this.product = p.product;
    	this.component = p.component;
		this.name = p.name;
		this.value = p.value;
		this.description = p.description;
	}

	public Property() {
	}

    @XmlElement
    @XmlID
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@XmlElement
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

    @XmlElement
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

    @XmlElement
    @XmlID
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}

    @XmlElement
    @XmlID
	public String getComponent() {
		return component;
	}
	public void setComponent(String component) {
		this.component = component;
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
        String s = this.product + ":" + this.component + ":" + this.name;
        return s.hashCode();
    }


    @Override
    public boolean equals(Object obj) {
        if(! (obj instanceof Property)){
            return false;
        }
        Property p = (Property) obj;
        if(!this.product.equals(p.product)){
            return false;
        }
        if(!this.component.equals(p.component)){
            return false;
        }
        if(!this.name.equals(p.name)){
            return false;
        }
        return true;
    }

}
