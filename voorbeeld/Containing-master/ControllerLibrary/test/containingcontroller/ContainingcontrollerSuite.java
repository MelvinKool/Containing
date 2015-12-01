/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingcontroller;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author Wessel
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({PathFinderTest.class, ControllerTest.class, ContainerDepartureComparerTest.class, TransporterComparerTest.class, BufferTest.class, TransporterTest.class, TransportTypesTest.class, ServerTest.class, AGVTest.class, PathNodeTest.class, ContainerTest.class, ContainerComparerTest.class, PreferedAGVTest.class, CraneTest.class, IWindowTest.class, WaypointTest.class, ServerClientTest.class})
public class ContainingcontrollerSuite {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }
    
}