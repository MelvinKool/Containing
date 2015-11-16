/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containing.xml;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author User
 */
public class MessageTest {
    
    public MessageTest() {
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
     * Test of getCommand method, of class Message.
     */
    @Test
    public void testGetCommand() {
        System.out.println("getCommand");
        Message instance = new Message();
        int expResult = 0;
        int result = instance.getCommand();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
      
    }

    /**
     * Test of setCommand method, of class Message.
     */
    @Test
    public void testSetCommand() {
        System.out.println("setCommand");
        int command = 0;
        Message instance = new Message();
        instance.setCommand(command);
        // TODO review the generated test code and remove the default call to fail.
      
    }

    /**
     * Test of getParameters method, of class Message.
     */
    @Test
    public void testGetParameters() {
        System.out.println("getParameters");
        Message instance = new Message();
        Object[] expResult = null;
        Object[] result = instance.getParameters();
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
      
    }

    /**
     * Test of setParameters method, of class Message.
     */
    @Test
    public void testSetParameters() {
        System.out.println("setParameters");
        Object[] parameters = null;
        Message instance = new Message();
        instance.setParameters(parameters);
        // TODO review the generated test code and remove the default call to fail.
    
    }

    /**
     * Test of decodeMessage method, of class Message.
     */
    @Test
    public void testDecodeMessage() {
        System.out.println("decodeMessage");
        String XML = "";
        Message expResult = null;
        Message result = Message.decodeMessage(XML);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
      
    }

    /**
     * Test of encodeMessage method, of class Message.
     */
    @Test
    public void testEncodeMessage() {
        System.out.println("encodeMessage");
        Message instance = new Message(0,new Object[]{"test"});
        String expResult = "<message command=\"0\">   <parameters length=\"1\">      <parameter class=\"java.lang.String\">test</parameter>   </parameters></message>";
        String result = Message.encodeMessage(instance);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
      
    }

    /**
     * Test of toString method, of class Message.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        Message instance = new Message(3,new Object[]{"test"});
        String expResult = "Message{command=3, parameters=1}";
        String result = instance.toString();
        assertEquals(expResult,result);
        // TODO review the generated test code and remove the default call to fail.
  
    }
}