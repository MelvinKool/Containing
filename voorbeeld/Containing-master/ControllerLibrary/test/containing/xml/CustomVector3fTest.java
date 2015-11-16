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
public class CustomVector3fTest {
    
    public CustomVector3fTest() {
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
     * Test of hashCode method, of class CustomVector3f.
     */
    @Test
    public void testHashCode() {
        System.out.println("hashCode");
        CustomVector3f instance = new CustomVector3f();
        int expResult = 0;
        int result = instance.hashCode();
        assertEquals(true, true);
        // TODO review the generated test code and remove the default call to fail.
      
    }

    /**
     * Test of set method, of class CustomVector3f.
     */
    @Test
    public void testSet_3args() {
        System.out.println("set");
        float x = 0.0F;
        float y = 4.0F;
        float z = 0.0F;
        CustomVector3f instance = new CustomVector3f();
        CustomVector3f expResult =  new CustomVector3f(0,4,0);
        CustomVector3f result = instance.set(x, y, z);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
      
    }

    /**
     * Test of set method, of class CustomVector3f.
     */
    @Test
    public void testSet_CustomVector3f() {
        System.out.println("set");
        CustomVector3f vect = new CustomVector3f(1,6,5);
        CustomVector3f instance = new CustomVector3f();
        CustomVector3f expResult = instance.set(new CustomVector3f(1,6,5));
        CustomVector3f result = instance.set(vect);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of equals method, of class CustomVector3f.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        Object obj = null;
        CustomVector3f instance = new CustomVector3f();
        boolean expResult = false;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
 
    }

    /**
     * Test of toString method, of class CustomVector3f.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        CustomVector3f instance = new CustomVector3f(1,2,0);
        String expResult = "Vector3f{x=1.0, y=2.0, z=0.0}";
        String result = instance.toString();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
     
    }
}