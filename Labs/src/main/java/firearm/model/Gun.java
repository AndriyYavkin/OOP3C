package firearm.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GunType", namespace = "http://www.firearm.com/schema", propOrder = {
    "model",
    "handy",
    "origin",
    "ttcs",
    "material"
})
public class Gun {

    @XmlElement(name = "Model", namespace = "http://www.firearm.com/schema", required = true)
    private String model;
    
    @XmlElement(name = "Handy", namespace = "http://www.firearm.com/schema", required = true)
    private Handy handy;
    
    @XmlElement(name = "Origin", namespace = "http://www.firearm.com/schema", required = true)
    private String origin;
    
    @XmlElementWrapper(name = "TTCs", namespace = "http://www.firearm.com/schema", required = true)
    @XmlElement(name = "TTC", namespace = "http://www.firearm.com/schema")
    private List<TTC> ttcs = new ArrayList<>();
    
    @XmlElement(name = "Material", namespace = "http://www.firearm.com/schema", required = true)
    private String material;
    
    @XmlAttribute(name = "id", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    private String id;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Handy getHandy() {
        return handy;
    }

    public void setHandy(Handy handy) {
        this.handy = handy;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public List<TTC> getTtcs() {
        return ttcs;
    }

    public void setTtcs(List<TTC> ttcs) {
        this.ttcs = ttcs;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "\nGun [id=" + id + ", model=" + model + ", handy=" + handy + 
               ", origin=" + origin + ", material=" + material + ", ttcs=" + ttcs + "]";
    }
}