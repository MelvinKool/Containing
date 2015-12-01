/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingcontroller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Len
 */
public class PathNode {

    /**
     * name of PathNode
     */
    private String id;
    /**
     * cost of this PathNode
     */
    private float cost = -1f;
    /**
     * the PathNode that is responsible for setting this cost
     */
    private PathNode parent;
    /**
     * Contains the linked PathNodes to this one
     */
    private List<PathNode> neighbours;
    /**
     * Contains linked PathNodes with the costs to this one
     */
    private HashMap<PathNode, Float> neighb = new HashMap(); //neighbor with cost to this node

    /**
     * Constructor
     *
     * @param id
     */
    public PathNode(String id) {
        this.id = id;
        neighbours = new ArrayList();
    }

    /**
     * print this node with its neighbors id and costs
     */
    public void printWithNeighbours() {
        System.out.print(this.id + "  ");
        for (PathNode node : neighbours) {
            System.out.print(node.getId() + " - " + this.getCostNeighb(node) + "      ");
        }
        System.out.println();
    }

    /**
     * returns ID
     *
     * @return
     */
    public String getId() {
        return this.id;
    }

    /**
     * set the shortest cost for now coming from parent
     *
     * @param cost
     * @param parent
     */
    public void setCost(float cost, PathNode parent) {
        this.cost = cost;
        this.parent = parent;
    }

    /**
     * returns cost
     *
     * @return
     */
    public float getCost() {
        return this.cost;
    }

    /**
     * add all parents to the list these are all the nodes that create the
     * shortest path
     *
     * @param route
     */
    public void getRoute(List<PathNode> route) {
        if (parent != null) {
            route.add(parent);
            parent.getRoute(route);
        }
    }

    /**
     * returns cost of this PathNode to a neighbor
     *
     * @param neighbour
     * @return
     */
    public float getCostNeighb(PathNode neighbour) {
        if (neighb.size() > 0) {
            return neighb.get(neighbour);
        } else {
            return 1000;
        }
    }

    /**
     * add new Neighbor
     *
     * @param neighbour
     * @param cost
     */
    public void addNeighbour(PathNode neighbour, float cost) {
        if (!neighbours.contains(neighbour)) {
            neighbours.add(neighbour);
            neighb.put(neighbour, cost);
        }
    }

    /**
     * remove a neighbor
     *
     * @param neighbour
     */
    public void removeNeighbour(PathNode neighbour) {
        neighbours.remove(neighbour);
        neighb.remove(neighbour);
    }

    /**
     * return a list of all neighbors
     *
     * @return
     */
    public List<PathNode> getNeighbours() {
        return this.neighbours;
    }
}
