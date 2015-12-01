package org.nhl.containing.communication;

import org.nhl.containing.communication.messages.Message;
import org.nhl.containing.communication.messages.CreateMessage;
import org.junit.*;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

/**
 * Basic empty test template for unit tests.
 */
public class XmlTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @BeforeClass
    public static void setUpClass() throws Exception {
        // Code executed before the first test method
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        // Code executed after the last test method
    }

    @Before
    public void setUp() throws Exception {
        // Code executed before each test
    }

    @Test
    public void testParseXmlMessageCreate() {
        try {
            Message result = Xml.parseXmlMessage("<Controller>"
                    + "<id>1</id>"
                    + "<Create>"
                    + "<Transporter "
                    + "type=\"vrachtauto\""
                    + " identifier=\"1\">"
                    + "<Container>"
                    + "<containerNr>1</containerNr>"
                    + "<owner>b</owner>"
                    + "<xLoc>0</xLoc>"
                    + "<yLoc>0</yLoc>"
                    + "<zLoc>0</zLoc>"
                    + "</Container>"
                    + "</Transporter>"
                    + "</Create>"
                    + "</Controller>");
            assertEquals(1, result.getId());
            CreateMessage createResult = (CreateMessage) result;
            assertEquals(1, createResult.getTransporterIdentifier());
            assertEquals("vrachtauto", createResult.getTransporterType());
            ContainerBean containerBeanResult = createResult.getContainerBeans().get(0);
            assertEquals(1, containerBeanResult.getContainerNr());
            assertEquals("b", containerBeanResult.getOwner());
            assertEquals(0, containerBeanResult.getxLoc());
            assertEquals(0, containerBeanResult.getyLoc());
            assertEquals(0, containerBeanResult.getzLoc());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() throws Exception {
        // Code executed after each test
    }
}
