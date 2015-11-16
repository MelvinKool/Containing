/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingcontroller;

import java.util.ArrayList;
import java.util.List;
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
public class PathFinderTest {
    
    public PathFinderTest() {
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
     * Test of getShortestPath method, of class PathFinder.
     */
    @Test
    public void testGetShortestPath_PathNode_PathNode() throws Exception {
        System.out.println("getShortestPath");
        
        PathFinder instance = new PathFinder();
        instance.createMap();
        List<PathNode> map = instance.getMapCSE();
        PathNode srce = map.get(0);
        PathNode dest = map.get(1);
        
        List<PathNode> res = new ArrayList();
        res.add(srce);
        res.add(dest);
        
        List expResult = res;
        List result = instance.getShortestPath(srce, dest);
        assertEquals(expResult, result);
        
        
    }

    /**
     * Test of getShortestPath method, of class PathFinder.
     */
    @Test
    public void testGetShortestPath_3args() throws Exception {
        System.out.println("getShortestPath");
        
        PathFinder instance = new PathFinder();
        instance.createMap();
        List<PathNode> map = instance.getMapCSE();
        PathNode srce = map.get(0);
        PathNode dest = map.get(1);
        boolean optimize = true;
        
        List<PathNode> res = new ArrayList();
        res.add(srce);
        res.add(dest);
        
        List expResult = res;
        List result = instance.getShortestPath(srce, dest, optimize);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of createMap method, of class PathFinder.
     */
    @Test
    public void testCreateMap() {
        System.out.println("createMap");
        PathFinder instance = new PathFinder();
        instance.createMap();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of optimize method, of class PathFinder.
     */
    @Test
    public void testOptimize() {
        System.out.println("optimize");
        List<PathNode> shortestRoute = null;
        PathFinder instance = new PathFinder();
        instance.optimize(shortestRoute);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMinimum method, of class PathFinder.
     */
    @Test
    public void testGetMinimum() {
        System.out.println("getMinimum");
        List<PathNode> neighbours = null;
        PathFinder instance = new PathFinder();
        PathNode expResult = null;
        PathNode result = instance.getMinimum(neighbours);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMap method, of class PathFinder.
     */
    @Test
    public void testGetMap() {
        System.out.println("getMap");
        PathFinder instance = new PathFinder();
        List expResult = null;
        List result = instance.getMap();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMapCBA method, of class PathFinder.
     */
    @Test
    public void testGetMapCBA() {
        System.out.println("getMapCBA");
        PathFinder instance = new PathFinder();
        List expResult = null;
        List result = instance.getMapCBA();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMapCTR method, of class PathFinder.
     */
    @Test
    public void testGetMapCTR() {
        System.out.println("getMapCTR");
        PathFinder instance = new PathFinder();
        List expResult = null;
        List result = instance.getMapCTR();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMapCLO method, of class PathFinder.
     */
    @Test
    public void testGetMapCLO() {
        System.out.println("getMapCLO");
        PathFinder instance = new PathFinder();
        List expResult = null;
        List result = instance.getMapCLO();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMapMR method, of class PathFinder.
     */
    @Test
    public void testGetMapMR() {
        System.out.println("getMapMR");
        PathFinder instance = new PathFinder();
        List expResult = null;
        List result = instance.getMapMR();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMapBA method, of class PathFinder.
     */
    @Test
    public void testGetMapBA() {
        System.out.println("getMapBA");
        PathFinder instance = new PathFinder();
        List expResult = null;
        List result = instance.getMapBA();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMapBB method, of class PathFinder.
     */
    @Test
    public void testGetMapBB() {
        System.out.println("getMapBB");
        PathFinder instance = new PathFinder();
        List expResult = null;
        List result = instance.getMapBB();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMapCSE method, of class PathFinder.
     */
    @Test
    public void testGetMapCSE() {
        System.out.println("getMapCSE");
        PathFinder instance = new PathFinder();
        List expResult = null;
        List result = instance.getMapCSE();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}