package reconf.server.domain;

import java.io.*;
import java.util.*;
import javax.xml.bind.annotation.*;


/**
 * @author mbroinizi
 */
@XmlRootElement
public class Product implements Serializable{

    private static final long serialVersionUID = 1L;

    private String name;
    private String description;
    private Set<Component> components = new HashSet<>();
    private Collection<Relation> relations = new ArrayList<>();

    public Product(Product p) {
        this.name = p.name;
        this.description = p.description;
        this.components.clear();
        this.components.addAll(p.components);
    }

    public Product() {
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

    @XmlElement(name="components")
    public Set<Component> getComponents() {
        return components;
    }
    public void setComponents(Set<Component> components) {
        if (components != null) {
            this.components.clear();
            this.components.addAll(components);
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
        String s = this.name;
        return s.hashCode();
    }


    @Override
    public boolean equals(Object obj) {
        if(! (obj instanceof Product)){
            return false;
        }
        Product p = (Product) obj;
        if(!this.name.equals(p.name)){
            return false;
        }
        return true;
    }

}
