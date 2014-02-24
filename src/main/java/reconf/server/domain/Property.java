package reconf.server.domain;

import javax.xml.bind.annotation.*;
import org.jboss.resteasy.annotations.providers.jaxb.json.Mapped;
import org.jboss.resteasy.annotations.providers.jaxb.json.XmlNsMap;
import org.jboss.resteasy.links.RESTServiceDiscovery;
  
@Mapped(namespaceMap = @XmlNsMap(jsonName = "atom", namespace = "http://www.w3.org/2005/Atom"))

@XmlRootElement

@XmlAccessorType(XmlAccessType.NONE)

public class Property {

    @XmlID
    @XmlAttribute
    private String name;
	
    @XmlAttribute
    private String product;
    
    @XmlAttribute
    private String component;
    
    @XmlAttribute
    private String value;

    @XmlElement
    private String description;

    
    public Property(Property p) {
    	this.product = p.product;
    	this.component = p.component;
		this.name = p.name;
		this.value = p.value;
		this.description = p.description;
	}

	public Property() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
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

	public String getComponent() {
		return component;
	}

	public void setComponent(String component) {
		this.component = component;
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