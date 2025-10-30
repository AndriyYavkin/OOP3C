package firearm.parser;

import firearm.model.FiringRange;
import firearm.model.Firearms;
import firearm.model.Gun;
import firearm.model.Handy;
import firearm.model.TTC;
import java.io.FileInputStream;
import java.util.ArrayList;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class StaxParser implements GunParser {

    @Override
    public Firearms parse(String xmlFilePath) throws Exception {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLEventReader eventReader = factory.createXMLEventReader(new FileInputStream(xmlFilePath));

        Firearms firearms = new Firearms();
        Gun currentGun = null;
        TTC currentTTC = null;

        while (eventReader.hasNext()) {
            XMLEvent event = eventReader.nextEvent();

            if (event.isStartElement()) {
                StartElement startElement = event.asStartElement();
                String localName = startElement.getName().getLocalPart();

                switch (localName) {
                    case "Gun":
                        currentGun = new Gun();
                        Attribute idAttr = startElement.getAttributeByName(new QName("id"));
                        currentGun.setId(idAttr.getValue());
                        currentGun.setTtcs(new ArrayList<>());
                        break;
                    case "Model":
                        event = eventReader.nextEvent();
                        currentGun.setModel(event.asCharacters().getData());
                        break;
                    case "Handy":
                        event = eventReader.nextEvent();
                        currentGun.setHandy(Handy.valueOf(event.asCharacters().getData()));
                        break;
                    case "Origin":
                        event = eventReader.nextEvent();
                        currentGun.setOrigin(event.asCharacters().getData());
                        break;
                    case "Material":
                        event = eventReader.nextEvent();
                        currentGun.setMaterial(event.asCharacters().getData());
                        break;
                    case "TTC":
                        currentTTC = new TTC();
                        break;
                    case "FiringRange":
                        event = eventReader.nextEvent();
                        currentTTC.setFiringRange(FiringRange.valueOf(event.asCharacters().getData()));
                        break;
                    case "AimRange":
                        event = eventReader.nextEvent();
                        currentTTC.setAimRange(Integer.parseInt(event.asCharacters().getData()));
                        break;
                    case "Magazine":
                        event = eventReader.nextEvent();
                        currentTTC.setMagazine(Boolean.parseBoolean(event.asCharacters().getData()));
                        break;
                    case "Optics":
                        event = eventReader.nextEvent();
                        currentTTC.setOptics(Boolean.parseBoolean(event.asCharacters().getData()));
                        break;
                }
            }

            if (event.isEndElement()) {
                EndElement endElement = event.asEndElement();
                String localName = endElement.getName().getLocalPart();
                if (localName.equals("Gun")) {
                    firearms.getGuns().add(currentGun);
                } else if (localName.equals("TTC")) {
                    currentGun.getTtcs().add(currentTTC);
                }
            }
        }
        return firearms;
    }
}