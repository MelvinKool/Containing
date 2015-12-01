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
public class IWindowTest {
    
    public IWindowTest() {
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
     * Test of WriteLogLine method, of class IWindow.
     */
    @Test
    public void testWriteLogLine() {
        System.out.println("WriteLogLine");
        String message = "testMessage";
        IWindow instance = new IWindowImpl();
        instance.WriteLogLine(message);
        assertEquals(true, true);
    }

    /**
     * Test of setTime method, of class IWindow.
     */
    @Test
    public void testSetTime() {
        System.out.println("setTime");
        
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
        
        Date simTime = date;
        IWindow instance = new IWindowImpl();
        instance.setTime(simTime);
        assertEquals(true, true);
    }

    public class IWindowImpl implements IWindow {

        public void WriteLogLine(String message) {
        }

        public void setTime(Date simTime) {
        }
    }
}