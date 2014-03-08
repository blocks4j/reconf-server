package reconf.server.domain;

import java.util.*;
import javax.xml.bind.annotation.*;


/**
 * @author mbroinizi
 */
@XmlRootElement
public class Product {

    private String name;
    private String description;
    private Set<Component> components = new HashSet<>();

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

    @XmlElementWrapper(name="components")
    @XmlElement(name="component")
    public Set<Component> getComponents() {
        return components;
    }
    public void setComponents(Set<Component> components) {
        if (components != null) {
            this.components.clear();
            this.components.addAll(components);
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
