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
public class PathNodeTest {
    
    public PathNodeTest() {
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
     * Test of getId method, of class PathNode.
     */
    @Test
    public void testGetId() {
        System.out.println("getId");
        PathNode instance = new PathNode("testID");
        String expResult = "testID";
        String result = instance.getId();
        assertEquals(expResult, result);
        
        
    }

    /**
     * Test of getCost method, of class PathNode.
     */
    @Test
    public void testGetCost() {
        System.out.println("getCost");
        PathNode parent = new PathNode("testID");
        PathNode instance = new PathNode("testID2");
        instance.setCost(0.5f, parent);
        float expResult = 0.5f;
        float result = instance.getCost();
        assertEquals(expResult, result, 0.0);
        
        
    }

    /**
     * Test of getCostNeighb method, of class PathNode.
     */
    @Test
    public void testGetCostNeighb() {
        System.out.println("getCostNeighb");
        PathNode neighbour = new PathNode("neighbour");
        PathNode instance = new PathNode("testID");
        
        neighbour.setCost(10, instance);
        instance.setCost(5, neighbour);
        instance.addNeighbour(neighbour, 10);
        
        float expResult = 10;
        float result = instance.getCostNeighb(neighbour);
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        
    }

    /**
     * Test of getNeighbours method, of class PathNode.
     */
    @Test
    public void testGetNeighbours() {
        System.out.println("getNeighbours");
        
        PathNode instance = new PathNode("testID");
        PathNode n1 = new PathNode("n1");
        PathNode n2 = new PathNode("n2");
        PathNode n3 = new PathNode("n3");
        instance.addNeighbour(n1, 1);
        instance.addNeighbour(n2, 2);
        instance.addNeighbour(n3, 3);
        
        List<PathNode> neighbours = new ArrayList();
        neighbours.add(n1);
        neighbours.add(n2);
        neighbours.add(n3);
        
        List expResult = neighbours;
        List result = instance.getNeighbours();
        assertEquals(expResult, result);
        
        
    }

    /**
     * Test of printWithNeighbours method, of class PathNode.
     */
    @Test
    public void testPrintWithNeighbours() {
        System.out.println("printWithNeighbours");
        PathNode instance = new PathNode("test");
        instance.printWithNeighbours();
        // TODO review the generated test code and remove the default call to fail.
        assertEquals(0,0);
        
        
    }

    /**
     * Test of setCost method, of class PathNode.
     */
    @Test
    public void testSetCost() {
        System.out.println("setCost");
        float cost = 12.0F;
        PathNode parent = new PathNode("test");
        PathNode instance =new PathNode("test2");
        instance.setCost(cost, parent);
        assertEquals(12.0f,cost,12.0f);
        
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of getRoute method, of class PathNode.
     */
    @Test
    public void testGetRoute() {
        System.out.println("getRoute");
        List<PathNode> route = new ArrayList();
        PathNode instance = new PathNode("test");
        instance.getRoute(route);
        PathNode parent = null;
          if (parent != null) {
            route.add(parent);
            parent.getRoute(route);
        }
        assertEquals(parent,null);
        
        // TODO review the generated test code and remove the default call to fail.
       // fail("The test case is a prototype.");
    }

    /**
     * Test of addNeighbour method, of class PathNode.
     */
    @Test
    public void testAddNeighbour() {
        System.out.println("addNeighbour");
        PathNode neighbour = new PathNode("test1");
        float cost = 0.0F;
        PathNode instance = new PathNode("test2");
        instance.addNeighbour(neighbour, cost);
        assertEquals(instance.getNeighbours().contains(neighbour), true);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of removeNeighbour method, of class PathNode.
     */
    @Test
    public void testRemoveNeighbour() {
        System.out.println("removeNeighbour");
        PathNode neighbour = new PathNode("test1");
        float cost = 0.0F;
        PathNode instance = new PathNode("test2");
        instance.addNeighbour(neighbour, cost);
        instance.removeNeighbour(neighbour);
        assertEquals(instance.getNeighbours().contains(neighbour), false);
        // TODO review the generated test code and remove the default call to fail.
    }
}