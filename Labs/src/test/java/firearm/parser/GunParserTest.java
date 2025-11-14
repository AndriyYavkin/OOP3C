package firearm.parser;

import firearm.model.Firearms;
import firearm.model.Gun;
import firearm.model.Handy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import java.net.URL;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GunParserTest {

    private String xmlPath;

    @BeforeEach
    void setUp() {
        URL resource = getClass().getClassLoader().getResource("guns.xml");
        assertNotNull(resource, "Test file guns.xml not found in resources.");
        xmlPath = resource.getPath();
    }

    static Stream<GunParser> parserProvider() {
        return Stream.of(
             new DomParser(),
             new SaxParser(),
             new StaxParser()
        );
    }

    @ParameterizedTest(name = "Test parser: {0}")
    @MethodSource("parserProvider")
    void testAllParsers_ShouldParseCorrectly(GunParser parser) throws Exception {
        Firearms firearms = parser.parse(xmlPath);

        assertNotNull(firearms);
        assertNotNull(firearms.getGuns());
        assertEquals(5, firearms.getGuns().size(), 
        "Parser " + parser.getClass().getSimpleName() + " should parse 5 guns.");

        Gun firstGun = firearms.getGuns().stream()
             .filter(g -> g.getId().equals("g001"))
             .findFirst()
             .orElse(null);

        assertNotNull(firstGun);
        assertEquals("AK-47", firstGun.getModel());
        assertEquals(Handy.TWO_HANDED, firstGun.getHandy());
        assertEquals("USSR", firstGun.getOrigin());
        assertEquals("Wood/Steel", firstGun.getMaterial());
        assertEquals(1, firstGun.getTtcs().size());
        assertEquals(800, firstGun.getTtcs().get(0).getAimRange());
        assertEquals(true, firstGun.getTtcs().get(0).isMagazine());
    }
}