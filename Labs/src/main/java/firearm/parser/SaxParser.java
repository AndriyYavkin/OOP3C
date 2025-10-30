package firearm.parser;

import firearm.model.Firearms;
import java.io.File;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class SaxParser implements GunParser {

    @Override
    public Firearms parse(String xmlFilePath) throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(true);
        SAXParser parser = factory.newSAXParser();
        
        SaxHandler handler = new SaxHandler();
        parser.parse(new File(xmlFilePath), handler);
        
        return handler.getFirearms();
    }
}