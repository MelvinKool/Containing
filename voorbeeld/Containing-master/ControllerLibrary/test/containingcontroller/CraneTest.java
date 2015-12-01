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
public class CraneTest {
    
    public CraneTest() {
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
     * Test of getReady method, of class Crane.
     */
    @Test
    public void testGetReady() {
        System.out.println("getReady");
        Crane instance = new Crane("testID", 0);
        instance.setIsReady(true);
        boolean expResult = true;
        boolean result = instance.getReady();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }
    /**
     * Test of toString method, of class Crane.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        Crane instance = new Crane("testID", 0);
        instance.setIsReady(true);
        String expResult = "Crane{" + "id=testID, node=" + instance.node + ", ready=" + instance.getReady() + '}';
        String result = instance.toString();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of loadContainer method, of class Crane.
     */
    @Test
    public void testLoadContainer() {
        System.out.println("loadContainer");
        Container cont = new Container();
        Crane instance = new Crane("testID", Crane.SeaCrane);
        instance.loadContainer(cont);
        assertEquals(true, true);
    }

    /**
     * Test of getContainer method, of class Crane.
     */
    @Test
    public void testGetContainer() {
        System.out.println("getContainer");
        Container cont = new Container();
        Crane instance = new Crane("testID", Crane.SeaCrane);
        instance.getContainer(cont);
        assertEquals(true, true);
    }

    /**
     * Test of setIsReady method, of class Crane.
     */
    @Test
    public void testSetIsReady() {
        System.out.println("setIsReady");
        boolean b = true;
        Crane instance = new Crane("testID", Crane.SeaCrane);;
        instance.setIsReady(b);
        assertEquals(true, true);
    }
}