package firearm.parser;

import firearm.model.FiringRange;
import firearm.model.Firearms;
import firearm.model.Gun;
import firearm.model.Handy;
import firearm.model.TTC;
import java.io.File;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DomParser implements GunParser {
    
    private static final String NAMESPACE_URI = "http://www.firearm.com/schema";

    @Override
    public Firearms parse(String xmlFilePath) throws Exception {
        Firearms firearms = new Firearms();
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File(xmlFilePath));

        Element root = document.getDocumentElement();
        NodeList gunNodes = root.getElementsByTagNameNS(NAMESPACE_URI, "Gun");

        for (int i = 0; i < gunNodes.getLength(); i++) {
            if (gunNodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
                Element gunElement = (Element) gunNodes.item(i);
                Gun gun = buildGun(gunElement);
                firearms.getGuns().add(gun);
            }
        }
        return firearms;
    }

    private Gun buildGun(Element gunElement) {
        Gun gun = new Gun();
        gun.setId(gunElement.getAttribute("id"));
        gun.setModel(getTagTextContent(gunElement, "Model"));
        gun.setHandy(Handy.valueOf(getTagTextContent(gunElement, "Handy")));
        gun.setOrigin(getTagTextContent(gunElement, "Origin"));
        gun.setMaterial(getTagTextContent(gunElement, "Material"));
        gun.setTtcs(new ArrayList<>());

        NodeList ttcNodes = gunElement.getElementsByTagNameNS(NAMESPACE_URI, "TTC");
        for (int i = 0; i < ttcNodes.getLength(); i++) {
            if (ttcNodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
                Element ttcElement = (Element) ttcNodes.item(i);
                TTC ttc = buildTTC(ttcElement);
                gun.getTtcs().add(ttc);
            }
        }
        return gun;
    }

    private TTC buildTTC(Element ttcElement) {
        TTC ttc = new TTC();
        ttc.setFiringRange(FiringRange.valueOf(getTagTextContent(ttcElement, "FiringRange")));
        ttc.setAimRange(Integer.parseInt(getTagTextContent(ttcElement, "AimRange")));
        ttc.setMagazine(Boolean.parseBoolean(getTagTextContent(ttcElement, "Magazine")));
        ttc.setOptics(Boolean.parseBoolean(getTagTextContent(ttcElement, "Optics")));
        return ttc;
    }

    private String getTagTextContent(Element parent, String tagName) {
        NodeList nodeList = parent.getElementsByTagNameNS(NAMESPACE_URI, tagName);
        if (nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        }
        return "";
    }
}