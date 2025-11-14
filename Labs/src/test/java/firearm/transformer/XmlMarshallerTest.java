package firearm.transformer;

import firearm.model.FiringRange;
import firearm.model.Firearms;
import firearm.model.Gun;
import firearm.model.Handy;
import firearm.model.TTC;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;

class XmlMarshallerTest {

    @Test
    void marshal_ShouldProduceCorrectXmlFile(@TempDir Path tempDir) throws IOException {
        Path outputFile = tempDir.resolve("marshal_output.xml");

        Firearms firearms = new Firearms();
        firearms.getGuns().add(createTestGun());

        XmlMarshaller marshaller = new XmlMarshaller();
        marshaller.marshal(firearms, outputFile.toString());

        assertTrue(Files.exists(outputFile), "Output file was not created.");

        String content = Files.readString(outputFile);

        assertTrue(content.contains("http://www.firearm.com/schema"),
            "Content should define the namespace 'http://www.firearm.com/schema'.");
        assertTrue(content.matches("(?s).*<([a-zA-Z0-9]+:)?Gun id=\"gTest\">.*"),
            "Content should contain the Gun tag with id 'gTest', regardless of prefix.");
        
        assertTrue(content.matches("(?s).*<([a-zA-Z0-9]+:)?Model>Test Pistol</([a-zA-Z0-9]+:)?Model>.*"),
            "Content should contain the Model tag, regardless of prefix.");

        assertTrue(content.matches("(?s).*<([a-zA-Z0-9]+:)?AimRange>25</([a-zA-Z0-9]+:)?AimRange>.*"),
            "Content should contain the AimRange tag, regardless of prefix.");
    }

    private Gun createTestGun() {
        Gun gun = new Gun();
        gun.setId("gTest");
        gun.setModel("Test Pistol");
        gun.setHandy(Handy.ONE_HANDED);
        gun.setOrigin("Testland");
        gun.setMaterial("TestPolymer");
        
        TTC ttc = new TTC();
        ttc.setFiringRange(FiringRange.CLOSE);
        ttc.setAimRange(25);
        ttc.setMagazine(true);
        ttc.setOptics(false);
        
        gun.setTtcs(new ArrayList<>());
        gun.getTtcs().add(ttc);
        return gun;
    }
}