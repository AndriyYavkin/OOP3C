package firearm.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "FiringRangeType", namespace = "http://www.firearm.com/schema")
@XmlEnum
public enum FiringRange {
    @XmlEnumValue("CLOSE")
    CLOSE,
    
    @XmlEnumValue("MEDIUM")
    MEDIUM,
    
    @XmlEnumValue("LONG")
    LONG;
}