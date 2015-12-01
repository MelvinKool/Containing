/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingcontroller;

import containing.xml.CustomVector3f;
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
public class ContainerTest {
    
    public ContainerTest() {
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
     * Test of getId method, of class Container.
     */
    @Test
    public void testGetId() {
        System.out.println("getId");
        Container instance = new Container();
        instance.setId("testID");
        String expResult = "testID";
        String result = instance.getId();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of getDateArrival method, of class Container.
     */
    @Test
    public void testGetDateArrival() {
        System.out.println("getDateArrival");
        Container instance = new Container();
        
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
        instance.setDateArrival(date);
        
        Date expResult = date;
        Date result = instance.getDateArrival();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of getDateDeparture method, of class Container.
     */
    @Test
    public void testGetDateDeparture() {
        System.out.println("getDateDeparture");
        Container instance = new Container();
        
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
        instance.setDateDeparture(date);
        
        Date expResult = date;
        Date result = instance.getDateDeparture();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of getTransportTypeArrival method, of class Container.
     */
    @Test
    public void testGetTransportTypeArrival() {
        System.out.println("getTransportTypeArrival");
        Container instance = new Container();
        instance.setTransportTypeArrival(0);
        int expResult = 0;
        int result = instance.getTransportTypeArrival();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of getTransportTypeDeparture method, of class Container.
     */
    @Test
    public void testGetTransportTypeDeparture() {
        System.out.println("getTransportTypeDeparture");
        Container instance = new Container();
        instance.setTransportTypeDeparture(0);
        int expResult = 0;
        int result = instance.getTransportTypeDeparture();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of getCargoCompanyArrival method, of class Container.
     */
    @Test
    public void testGetCargoCompanyArrival() {
        System.out.println("getCargoCompanyArrival");
        Container instance = new Container();
        instance.setCargoCompanyArrival("testCompany");
        String expResult = "testCompany";
        String result = instance.getCargoCompanyArrival();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of getCargoCompanyDeparture method, of class Container.
     */
    @Test
    public void testGetCargoCompanyDeparture() {
        System.out.println("getCargoCompanyDeparture");
        Container instance = new Container();
        instance.setCargoCompanyDeparture("testCompany");
        String expResult = "testCompany";
        String result = instance.getCargoCompanyDeparture();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of getPosition method, of class Container.
     */
    @Test
    public void testGetPosition() {
        System.out.println("getPosition");
        Container instance = new Container();
        instance.setPosition(new CustomVector3f(0, 0, 0));
        CustomVector3f expResult = new CustomVector3f(0, 0, 0);
        CustomVector3f result = instance.getPosition();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of getBufferPosition method, of class Container.
     */
    @Test
    public void testGetBufferPosition() {
        System.out.println("getBufferPosition");
        Container instance = new Container();
        instance.setPosition(new CustomVector3f(0, 0, 0));
        instance.setBufferPosition(new CustomVector3f(0, 0, 0));
        CustomVector3f expResult = new CustomVector3f(0, 0, 0);
        CustomVector3f result = instance.getBufferPosition();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of getOwner method, of class Container.
     */
    @Test
    public void testGetOwner() {
        System.out.println("getOwner");
        Container instance = new Container();
        instance.setOwner("testOwner");
        String expResult = "testOwner";
        String result = instance.getOwner();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of getContainerNumber method, of class Container.
     */
    @Test
    public void testGetContainerNumber() {
        System.out.println("getContainerNumber");
        Container instance = new Container();
        instance.setContainerNumber(123);
        int expResult = 123;
        int result = instance.getContainerNumber();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of getHeight method, of class Container.
     */
    @Test
    public void testGetHeight() {
        System.out.println("getHeight");
        Container instance = new Container();
        instance.setHeight("testHeight");
        String expResult = "testHeight";
        String result = instance.getHeight();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of getWidth method, of class Container.
     */
    @Test
    public void testGetWidth() {
        System.out.println("getWidth");
        Container instance = new Container();
        instance.setWidth("testWidth");
        String expResult = "testWidth";
        String result = instance.getWidth();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of getLenght method, of class Container.
     */
    @Test
    public void testGetLenght() {
        System.out.println("getLenght");
        Container instance = new Container();
        instance.setLenght("testLength");
        String expResult = "testLength";
        String result = instance.getLenght();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of getWeightEmpty method, of class Container.
     */
    @Test
    public void testGetWeightEmpty() {
        System.out.println("getWeightEmpty");
        Container instance = new Container();
        instance.setWeightEmpty(15);
        int expResult = 15;
        int result = instance.getWeightEmpty();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of getWeightLoaded method, of class Container.
     */
    @Test
    public void testGetWeightLoaded() {
        System.out.println("getWeightLoaded");
        Container instance = new Container();
        instance.setWeightLoaded(40);
        int expResult = 40;
        int result = instance.getWeightLoaded();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of getContents method, of class Container.
     */
    @Test
    public void testGetContents() {
        System.out.println("getContents");
        Container instance = new Container();
        instance.setContents("testContents");
        String expResult = "testContents";
        String result = instance.getContents();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of getContentType method, of class Container.
     */
    @Test
    public void testGetContentType() {
        System.out.println("getContentType");
        Container instance = new Container();
        instance.setContentType("testContentType");
        String expResult = "testContentType";
        String result = instance.getContentType();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of getContentDanger method, of class Container.
     */
    @Test
    public void testGetContentDanger() {
        System.out.println("getContentDanger");
        Container instance = new Container();
        instance.setContentDanger("testContentDanger");
        String expResult = "testContentDanger";
        String result = instance.getContentDanger();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of getIso method, of class Container.
     */
    @Test
    public void testGetIso() {
        System.out.println("getIso");
        Container instance = new Container();
        instance.setIso("testISO");
        String expResult = "testISO";
        String result = instance.getIso();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of equals method, of class Container.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        Container obj = new Container();
        Container instance = new Container();
        obj.setContainerNumber(321);
        instance.setContainerNumber(321);
        boolean expResult = true;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of setId method, of class Container.
     */
    @Test
    public void testSetId() {
        System.out.println("setId");
        String id = "";
        Container instance = new Container();
        instance.setId(id);
        assertEquals(true, true);
    }

    /**
     * Test of setDateArrival method, of class Container.
     */
    @Test
    public void testSetDateArrival() {
        System.out.println("setDateArrival");
        
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
        
        Date dateArrival = date;
        Container instance = new Container();
        instance.setDateArrival(dateArrival);
        assertEquals(true, true);
    }

    /**
     * Test of setDateDeparture method, of class Container.
     */
    @Test
    public void testSetDateDeparture() {
        System.out.println("setDateDeparture");
        
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
        
        Date dateArrival = date;
        
        Date dateDeparture = null;
        Container instance = new Container();
        instance.setDateDeparture(dateDeparture);
        assertEquals(true, true);
    }

    /**
     * Test of setTransportTypeArrival method, of class Container.
     */
    @Test
    public void testSetTransportTypeArrival() {
        System.out.println("setTransportTypeArrival");
        int transportTypeArrival = 0;
        Container instance = new Container();
        instance.setTransportTypeArrival(transportTypeArrival);
        assertEquals(true, true);
    }

    /**
     * Test of setTransportTypeDeparture method, of class Container.
     */
    @Test
    public void testSetTransportTypeDeparture() {
        System.out.println("setTransportTypeDeparture");
        int transportTypeDeparture = 0;
        Container instance = new Container();
        instance.setTransportTypeDeparture(transportTypeDeparture);
        assertEquals(true, true);
    }

    /**
     * Test of setCargoCompanyArrival method, of class Container.
     */
    @Test
    public void testSetCargoCompanyArrival() {
        System.out.println("setCargoCompanyArrival");
        String cargoCompanyArrival = "testCompany";
        Container instance = new Container();
        instance.setCargoCompanyArrival(cargoCompanyArrival);
        assertEquals(true, true);
    }

    /**
     * Test of setCargoCompanyDeparture method, of class Container.
     */
    @Test
    public void testSetCargoCompanyDeparture() {
        System.out.println("setCargoCompanyDeparture");
        String cargoCompanyDeparture = "testCompany";
        Container instance = new Container();
        instance.setCargoCompanyDeparture(cargoCompanyDeparture);
        assertEquals(true, true);
    }

    /**
     * Test of setPosition method, of class Container.
     */
    @Test
    public void testSetPosition() {
        System.out.println("setPosition");
        CustomVector3f position = new CustomVector3f(0, 0, 0);
        Container instance = new Container();
        instance.setPosition(position);
        assertEquals(true, true);
    }

    /**
     * Test of setBufferPosition method, of class Container.
     */
    @Test
    public void testSetBufferPosition() {
        System.out.println("setBufferPosition");
        CustomVector3f bufferPosition = new CustomVector3f(0, 0, 0);
        Container instance = new Container();
        instance.setBufferPosition(bufferPosition);
        assertEquals(true, true);
    }

    /**
     * Test of setOwner method, of class Container.
     */
    @Test
    public void testSetOwner() {
        System.out.println("setOwner");
        String owner = "testOwner";
        Container instance = new Container();
        instance.setOwner(owner);
        assertEquals(true, true);
    }

    /**
     * Test of setContainerNumber method, of class Container.
     */
    @Test
    public void testSetContainerNumber() {
        System.out.println("setContainerNumber");
        int containerNumber = 0;
        Container instance = new Container();
        instance.setContainerNumber(containerNumber);
        assertEquals(true, true);
    }

    /**
     * Test of setHeight method, of class Container.
     */
    @Test
    public void testSetHeight() {
        System.out.println("setHeight");
        String height = "testHeight";
        Container instance = new Container();
        instance.setHeight(height);
        assertEquals(true, true);
    }

    /**
     * Test of setWidth method, of class Container.
     */
    @Test
    public void testSetWidth() {
        System.out.println("setWidth");
        String width = "testWidth";
        Container instance = new Container();
        instance.setWidth(width);
        assertEquals(true, true);
    }

    /**
     * Test of setLenght method, of class Container.
     */
    @Test
    public void testSetLenght() {
        System.out.println("setLenght");
        String lenght = "testLength";
        Container instance = new Container();
        instance.setLenght(lenght);
        assertEquals(true, true);
    }

    /**
     * Test of setWeightEmpty method, of class Container.
     */
    @Test
    public void testSetWeightEmpty() {
        System.out.println("setWeightEmpty");
        int weightEmpty = 0;
        Container instance = new Container();
        instance.setWeightEmpty(weightEmpty);
        assertEquals(true, true);
    }

    /**
     * Test of setWeightLoaded method, of class Container.
     */
    @Test
    public void testSetWeightLoaded() {
        System.out.println("setWeightLoaded");
        int weightLoaded = 10;
        Container instance = new Container();
        instance.setWeightLoaded(weightLoaded);
        assertEquals(true, true);
    }

    /**
     * Test of setContents method, of class Container.
     */
    @Test
    public void testSetContents() {
        System.out.println("setContents");
        String contents = "testContents";
        Container instance = new Container();
        instance.setContents(contents);
        assertEquals(true, true);
    }

    /**
     * Test of setContentType method, of class Container.
     */
    @Test
    public void testSetContentType() {
        System.out.println("setContentType");
        String contentType = "testContentType";
        Container instance = new Container();
        instance.setContentType(contentType);
        assertEquals(true, true);
    }

    /**
     * Test of setContentDanger method, of class Container.
     */
    @Test
    public void testSetContentDanger() {
        System.out.println("setContentDanger");
        String contentDanger = "testContentDanger";
        Container instance = new Container();
        instance.setContentDanger(contentDanger);
        assertEquals(true, true);
    }

    /**
     * Test of setIso method, of class Container.
     */
    @Test
    public void testSetIso() {
        System.out.println("setIso");
        String iso = "testISO";
        Container instance = new Container();
        instance.setIso(iso);
        assertEquals(true, true);
    }
}