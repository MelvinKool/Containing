/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingcontroller;

import containing.xml.Message;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Wessel
 */
public class ServerTest {
    
    public ServerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of Run method, of class Server.
     */
    @Test
    public void testRun() {
        System.out.println("Run");
        Server instance = null;
        instance.Run();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of createTransporter method, of class Server.
     */
    @Test
    public void testCreateTransporter() {
        System.out.println("createTransporter");
        Server instance = null;
        instance.createTransporter();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of sendCommand method, of class Server.
     */
    @Test
    public void testSendCommand() {
        System.out.println("sendCommand");
        Message mes = null;
        Server instance = null;
        boolean expResult = false;
        boolean result = instance.sendCommand(mes);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of MessageRecieved method, of class Server.
     */
    @Test
    public void testMessageRecieved() {
        System.out.println("MessageRecieved");
        String ln = "";
        Server instance = null;
        instance.MessageRecieved(ln);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}