package firearm.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "HandyType", namespace = "http://www.firearm.com/schema")
@XmlEnum
public enum Handy {
    @XmlEnumValue("ONE_HANDED")
    ONE_HANDED,
    
    @XmlEnumValue("TWO_HANDED")
    TWO_HANDED;
}