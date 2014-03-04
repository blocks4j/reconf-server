package reconf.server.domain;

import javax.xml.bind.annotation.*;
import org.jboss.resteasy.annotations.providers.jaxb.json.*;
import org.jboss.resteasy.links.*;

@Mapped(namespaceMap = @XmlNsMap(jsonName = "atom", namespace = "http://www.w3.org/2005/Atom"))
@XmlRootElement
public class Property {

    private String name;
    private String product;
    private String component;
    private String value;
    private String description;
    private RESTServiceDiscovery rest = new RESTServiceDiscovery();

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

    @XmlElementRef
    public RESTServiceDiscovery getRest() {
		return rest;
	}
	public void setRest(RESTServiceDiscovery rest) {
		this.rest = rest;
	}
}
