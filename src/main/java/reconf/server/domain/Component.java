package reconf.server.domain;

import java.util.*;
import javax.xml.bind.annotation.*;
import org.jboss.resteasy.annotations.providers.jaxb.json.*;
import org.jboss.resteasy.links.*;

@Mapped(namespaceMap = @XmlNsMap(jsonName = "atom", namespace = "http://www.w3.org/2005/Atom"))
@XmlRootElement
public class Component {

    private String product;
    private String name;
    private String description;
    private List<Property> properties = new ArrayList<>();
    private RESTServiceDiscovery rest;

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

	@XmlElementWrapper(name="properties")
	@XmlElement(name="property")
	public Collection<Property> getProperties() {
		return properties;
	}
	public void setProperties(Collection<Property> properties) {
	    if (properties != null) {
	        this.properties.clear();
	        this.properties.addAll(properties);
	    }
	}

	@XmlElementRef
	public RESTServiceDiscovery getRest() {
		return rest;
	}
	public void setRest(RESTServiceDiscovery rest) {
		this.rest = rest;
	}

}