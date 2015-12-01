/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingcontroller;

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
public class TransportTypesTest {
    
    public TransportTypesTest() {
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
     * Test of getTransportType method, of class TransportTypes.
     */
    @Test
    public void testGetTransportType_String() {
        System.out.println("getTransportType");
        String type = "asdf";
        int expResult1 = 0;
        int result1 = TransportTypes.getTransportType(type);
        type = "zeeschip";
        int expResult2 = 0;
        int result2 = TransportTypes.getTransportType(type);
        type = "binnenschip";
        int expResult3 = 1;
        int result3 = TransportTypes.getTransportType(type);
        type = "vrachtauto";
        int expResult4 = 2;
        int result4 = TransportTypes.getTransportType(type);
        type = "trein";
        int expResult5 = 3;
        int result5 = TransportTypes.getTransportType(type);
        assertEquals(expResult1, result1);
        assertEquals(expResult2, result2);
        assertEquals(expResult3, result3);
        assertEquals(expResult4, result4);
        assertEquals(expResult5, result5);
        
        
    }

    /**
     * Test of getTransportType method, of class TransportTypes.
     */
    @Test
    public void testGetTransportType_int() {
        System.out.println("getTransportType");
        int type = 37;
        String expResult1 = "";
        String result1 = TransportTypes.getTransportType(type);
        type = 0;
        String expResult2 = "zeeschip";
        String result2 = TransportTypes.getTransportType(type);
        type = 1;
        String expResult3 = "binnenschip";
        String result3 = TransportTypes.getTransportType(type);
        assertEquals(expResult1, result1);
        type = 2;
        String expResult4 = "vrachtauto";
        String result4 = TransportTypes.getTransportType(type);
        type = 3;
        String expResult5 = "trein";
        String result5 = TransportTypes.getTransportType(type);
        assertEquals(expResult1, result1);
        assertEquals(expResult2, result2);
        assertEquals(expResult3, result3);
        assertEquals(expResult4, result4);
        assertEquals(expResult5, result5);
        
        
    }
}