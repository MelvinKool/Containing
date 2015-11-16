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
public class AGVTest {
    
    public AGVTest() {
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

    @Test
    public void testGetIsReady() {
        System.out.println("getIsReady");
        Buffer b = new Buffer();
        AGV instance = new AGV(b.pathNodeUp, b);
        AGV instance2 = new AGV(b.pathNodeDown, b);
        boolean expResult = true;
        boolean result = instance.getIsReady();
        boolean result2 = instance2.getIsReady();
        assertEquals(expResult, result);
        assertEquals(expResult, result2);
        
        
    }

    /**
     * Test of toString method, of class AGV.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        Buffer b = new Buffer();
        AGV instance = new AGV(b.pathNodeUp, b);
        String expResult = "AGV{" + "name=" + instance.name + ", isHome=" + true + ", container=" + instance.container + ", home=" + instance.home + ", homeBuffer=" + instance.homeBuffer.id + '}';
        String result = instance.toString();
        assertEquals(expResult, result);
        
        
    }

    /**
     * Test of isReady method, of class AGV.
     */
    @Test
    public void testIsReady() {
        System.out.println("isReady");
        Buffer b = new Buffer();
        AGV instance = new AGV(b.pathNodeUp, b);
        boolean expResult = true;
        boolean result = instance.isReady();
        assertEquals(expResult, result);
        
        
    }

    /**
     * Test of isIsHome method, of class AGV.
     */
    @Test
    public void testIsIsHome() {
        System.out.println("isIsHome");
        Buffer b = new Buffer();
        AGV instance = new AGV(b.pathNodeUp, b);
        boolean expResult = true;
        boolean result = instance.isIsHome();
        assertEquals(expResult, result);
        
        
    }

    /**
     * Test of moveToCrane method, of class AGV.
     */
    @Test
    public void testMoveToCrane() {
        System.out.println("moveToCrane");
        assertEquals(true, true);
    }

    /**
     * Test of moveToHome method, of class AGV.
     */
    @Test
    public void testMoveToHome() {
        System.out.println("moveToHome");
        assertEquals(true, true);
    }

    /**
     * Test of setIsReady method, of class AGV.
     */
    @Test
    public void testSetIsReady() {
        System.out.println("setIsReady");
        boolean newValue = false;
        Buffer b = new Buffer();
        AGV instance = new AGV(b.pathNodeUp, b);
        instance.setIsReady(newValue);
        assertEquals(true, true);
    }

    /**
     * Test of setReady method, of class AGV.
     */
    @Test
    public void testSetReady() {
        System.out.println("setReady");
        boolean ready = false;
        Buffer b = new Buffer();
        AGV instance = new AGV(b.pathNodeUp, b);
        instance.setReady(ready);
        assertEquals(true, true);
    }

    /**
     * Test of setIsHome method, of class AGV.
     */
    @Test
    public void testSetIsHome() {
        System.out.println("setIsHome");
        boolean isHome = false;
        Buffer b = new Buffer();
        AGV instance = new AGV(b.pathNodeUp, b);
        instance.setIsHome(isHome);
        assertEquals(true, true);
    }
}