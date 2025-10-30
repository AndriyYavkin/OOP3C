package firearm.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TTCType", namespace = "http://www.firearm.com/schema", propOrder = {
    "firingRange",
    "aimRange",
    "magazine",
    "optics"
})
public class TTC {

    @XmlElement(name = "FiringRange", namespace = "http://www.firearm.com/schema", required = true)
    private FiringRange firingRange;
    
    @XmlElement(name = "AimRange", namespace = "http://www.firearm.com/schema", required = true)
    private int aimRange;
    
    @XmlElement(name = "Magazine", namespace = "http://www.firearm.com/schema")
    private boolean magazine;
    
    @XmlElement(name = "Optics", namespace = "http://www.firearm.com/schema")
    private boolean optics;

    public FiringRange getFiringRange() {
        return firingRange;
    }

    public void setFiringRange(FiringRange firingRange) {
        this.firingRange = firingRange;
    }

    public int getAimRange() {
        return aimRange;
    }

    public void setAimRange(int aimRange) {
        this.aimRange = aimRange;
    }

    public boolean isMagazine() {
        return magazine;
    }

    public void setMagazine(boolean magazine) {
        this.magazine = magazine;
    }

    public boolean isOptics() {
        return optics;
    }

    public void setOptics(boolean optics) {
        this.optics = optics;
    }

    @Override
    public String toString() {
        return "TTC{" +
                "firingRange=" + firingRange +
                ", aimRange=" + aimRange +
                ", magazine=" + magazine +
                ", optics=" + optics +
                '}';
    }
}