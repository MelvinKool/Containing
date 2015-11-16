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
public class SimContainerTest {
    
    public SimContainerTest() {
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
     * Test of getId method, of class SimContainer.
     */
    @Test
    public void testGetId() {
        System.out.println("getId");
        SimContainer instance = new SimContainer();
        String expResult = "Test";
        instance.setId(expResult);
        String result = instance.getId();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of setId method, of class SimContainer.
     */
    @Test
    public void testSetId() {
        System.out.println("setId");
        String id = "";
        SimContainer instance = new SimContainer();
        instance.setId(id);
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of getIndexPosition method, of class SimContainer.
     */
    @Test
    public void testGetIndexPosition() {
        System.out.println("getIndexPosition");
        SimContainer instance = new SimContainer();
        CustomVector3f expResult = null;
        CustomVector3f result = instance.getIndexPosition();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of setIndexPosition method, of class SimContainer.
     */
    @Test
    public void testSetIndexPosition() {
        System.out.println("setIndexPosition");
        CustomVector3f indexPosition = null;
        SimContainer instance = new SimContainer();
        instance.setIndexPosition(indexPosition);
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of getBedrijf method, of class SimContainer.
     */
    @Test
    public void testGetBedrijf() {
        System.out.println("getBedrijf");
        SimContainer instance = new SimContainer();
        String expResult = "Test";
        instance.setBedrijf(expResult);
        String result = instance.getBedrijf();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of setBedrijf method, of class SimContainer.
     */
    @Test
    public void testSetBedrijf() {
        System.out.println("setBedrijf");
        String bedrijf = "Test";
        SimContainer instance = new SimContainer();
        instance.setBedrijf(bedrijf);
         assertEquals(bedrijf, instance.getBedrijf());
        // TODO review the generated test code and remove the default call to fail.
      
    }
}