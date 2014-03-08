package reconf.server.domain;

import java.net.*;
import javax.xml.bind.annotation.*;

/**
 * @author mbroinizi
 */

public class Relation {


    public enum Type {SELF, LIST, UPDATE, REMOVE, ADD;
        @Override
        public String toString() {
            return name().toLowerCase();
        };
    }

    private Type type;
    private URI uri;

    public Relation(){

    }
    public Relation(Type type, URI href){
        this.type = type;
        this.uri = href;
    }

    @XmlTransient
    public Type getType() {
        return type;
    }
    public void setType(Type type) {
        this.type = type;
    }

    @XmlTransient
    public URI getURI() {
        return uri;
    }
    public void setURI(URI href) {
        this.uri = href;
    }

    @XmlAttribute
    public String getRel(){
        if(type == null){
            return "";
        }
        return type.toString();
    }

    @XmlAttribute
    public String getHref(){
        if(uri == null){
            return "";
        }
        return uri.toString();
    }

}
