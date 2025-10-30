package firearm.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Firearms", namespace = "http://www.firearm.com/schema")
public class Firearms {

    @XmlElement(name = "Gun", namespace = "http://www.firearm.com/schema")
    private List<Gun> guns = new ArrayList<>();

    public List<Gun> getGuns() {
        return guns;
    }

    public void setGuns(List<Gun> guns) {
        this.guns = guns;
    }

    @Override
    public String toString() {
        return "Firearms{" + "guns=" + guns + '}';
    }
}