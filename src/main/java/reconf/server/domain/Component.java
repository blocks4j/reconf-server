package reconf.server.domain;

import java.util.Collection;
import java.util.HashSet;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;

import org.jboss.resteasy.annotations.providers.jaxb.json.Mapped;
import org.jboss.resteasy.annotations.providers.jaxb.json.XmlNsMap;
import org.jboss.resteasy.links.RESTServiceDiscovery;
  
@Mapped(namespaceMap = @XmlNsMap(jsonName = "atom", namespace = "http://www.w3.org/2005/Atom"))

@XmlRootElement

@XmlAccessorType(XmlAccessType.NONE)

public class Component {

	@XmlAttribute
    private String product;
	
	@XmlID
    @XmlAttribute
    private String name;
    
    @XmlElement
    private String description;
    
    @XmlAttribute
    Collection<Property> properties = new HashSet<>();

    
    public Component(Component p) {
    	this.product = p.product;    	
		this.name = p.name;		
		this.description = p.description;
		this.properties.clear();
		this.properties.addAll(p.properties);
	}

	public Component() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public Collection<Property> getProperties() {
		return properties;
	}

	public void setProperties(Collection<Property> properties) {
		this.properties = properties;
	}

	@XmlElementRef
    private RESTServiceDiscovery rest;
    
	
	public RESTServiceDiscovery getRest() {
		return rest;
	}

	public void setRest(RESTServiceDiscovery rest) {
		this.rest = rest;
	}
	
}