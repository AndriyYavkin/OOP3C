package firearm.parser;

import firearm.model.FiringRange;
import firearm.model.Firearms;
import firearm.model.Gun;
import firearm.model.Handy;
import firearm.model.TTC;
import java.util.ArrayList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SaxHandler extends DefaultHandler {

    private Firearms firearms;
    private Gun currentGun;
    private TTC currentTTC;
    private StringBuilder elementValue;
    
    private static final String NS_URI = "http://www.firearm.com/schema";

    @Override
    public void startDocument() throws SAXException {
        firearms = new Firearms();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        elementValue = new StringBuilder();
        if (!uri.equals(NS_URI)) {
            return;
        }

        switch (localName) {
            case "Gun":
                currentGun = new Gun();
                currentGun.setId(attributes.getValue("id"));
                currentGun.setTtcs(new ArrayList<>());
                break;
            case "TTC":
                currentTTC = new TTC();
                break;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (elementValue != null) {
            elementValue.append(ch, start, length);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (!uri.equals(NS_URI)) {
            return;
        }

        String value = "";
        if (elementValue != null) {
             value = elementValue.toString().trim();
        }

        switch (localName) {
            case "Gun":
                firearms.getGuns().add(currentGun);
                break;
            case "Model":
                currentGun.setModel(value);
                break;
            case "Handy":
                currentGun.setHandy(Handy.valueOf(value));
                break;
            case "Origin":
                currentGun.setOrigin(value);
                break;
            case "Material":
                currentGun.setMaterial(value);
                break;
            case "TTC":
                currentGun.getTtcs().add(currentTTC);
                break;
            case "FiringRange":
                currentTTC.setFiringRange(FiringRange.valueOf(value));
                break;
            case "AimRange":
                currentTTC.setAimRange(Integer.parseInt(value));
                break;
            case "Magazine":
                currentTTC.setMagazine(Boolean.parseBoolean(value));
                break;
            case "Optics":
                currentTTC.setOptics(Boolean.parseBoolean(value));
                break;
        }
    }

    public Firearms getFirearms() {
        return firearms;
    }
}