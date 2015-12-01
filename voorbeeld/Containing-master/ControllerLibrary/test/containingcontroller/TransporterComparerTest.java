/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingcontroller;

import java.util.Calendar;
import java.util.Date;
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
public class TransporterComparerTest {
    
    public TransporterComparerTest() {
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
     * Test of compare method, of class TransporterComparer.
     */
    @Test
    public void testCompare() {
        System.out.println("compare");
        
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2004);
        cal.set(Calendar.MONTH, Calendar.DECEMBER);
        cal.set(Calendar.DAY_OF_MONTH, 22);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        date.setTime(cal.getTimeInMillis());
        
        Transporter o1 = new Transporter(0, date);
        Transporter o2 = new Transporter(0, date);
        TransporterComparer instance = new TransporterComparer();
        int expResult = 0;
        int result = instance.compare(o1, o2);
        assertEquals(expResult, result);
        
        
    }
}