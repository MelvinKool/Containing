/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingcontroller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Len Class that creates map of all PathNodes contains method for
 * calculating shortest Path (Dijkstra algorithm)
 */
public class PathFinder {

    private List<PathNode> map = new ArrayList(); //complete map
    private List<PathNode> mapCSE = new ArrayList();  //seacranes
    private List<PathNode> mapCBA = new ArrayList(); //bargecranes
    private List<PathNode> mapCTR = new ArrayList(); //traincranes
    private List<PathNode> mapCLO = new ArrayList(); // lorrycranes
    private List<PathNode> mapMR = new ArrayList();  //main points
    private List<PathNode> mapBA = new ArrayList(); //buffer a
    private List<PathNode> mapBB = new ArrayList(); //buffer b
    private static String lblBA = "bfa";//buffer a points
    private static String lblBB = "bfb";//buffer b points
    private static String lblCTR = "ctr";//cranes train points
    private static String lblCLO = "clo"; //cranes lorry points
    private static String lblCSE = "cse";//cranes sea points
    private static String lblCBA = "cba"; //cranes barge points
    private static String lblM = "m"; //main points
    private float smallCost = 0.5f; //smallest cost (the lorry points)

    /**
     * create pathfinder
     */
    public PathFinder() {
    }

    /**
     * Creates map in steps
     */
    public void createMap() {
        createBPoints(); //create buffer
        createCBA(); //create barge
        createCLO(); //create lorry
        createCTR(); //create train
        createCSE(); //create sea
        connectGreen(); //create mainpoints and if necessary connect to other points
    }

    /**
     * Creates all PathNodes connected to buffers
     */
    private void createBPoints() {
        float cost = smallCost * 2;
        PathNode previousA = new PathNode(lblBA + "001");
        PathNode previousB = new PathNode(lblBB + "001");
        getMapBA().add(previousA);
        getMapBB().add(previousB);

        for (int i = 2; i <= 63; i++) {
            PathNode BAi = new PathNode(lblBA + String.format("%03d", i));
            PathNode BBi = new PathNode(lblBB + String.format("%03d", i));
            getMapBA().add(BAi);
            getMapBB().add(BBi);

            link(previousA, BAi, cost);
            link(previousB, BBi, cost);

            previousA = BAi;
            previousB = BBi;
        }

        getMap().addAll(getMapBA());
        getMap().addAll(getMapBB());
    }

    /**
     * Creates all PathNodes of the BargeCranes
     */
    private void createCBA() {
        float cost = smallCost * 6;
        PathNode previousNode = new PathNode(lblCBA + "001");
        int i = 2;
        getMapCBA().add(previousNode);

        while (i <= 8) {
            PathNode CLi = new PathNode(lblCBA + String.format("%03d", i));
            getMapCBA().add(CLi);

            link(CLi, previousNode, cost);
            previousNode = CLi;
            i++;
        }
        getMap().addAll(getMapCBA());
    }

    /**
     * Inserts a PathNode between two connected PathNodes
     *
     * @param idA = connection 1
     * @param idC = connection 2
     * @param b = the insertion node
     * @param costA = cost of connection 1 to insertion node
     * @param costC = cost of connection 2 to insertion node
     */
    private void insertNode(String idA, String idC, PathNode b, float costA, float costC) {
        PathNode tmpNode = null;
        for (PathNode nodeA : getMap()) {
            if (nodeA.getId().equals(idA)) {
                for (PathNode cNode : nodeA.getNeighbours()) {
                    if (cNode.getId().equals(idC)) {
                        tmpNode = cNode;
                        link(nodeA, b, costA);
                        link(b, tmpNode, costC);
                        delink(nodeA, cNode);
                    }
                }
            }
        }
    }

    /**
     * Creates PathNodes of all SeaCranes
     */
    private void createCSE() {
        float cost = smallCost * 4;
        int i = 2;
        PathNode previousNode = new PathNode(lblCSE + "001");
        getMapCSE().add(previousNode);

        while (i <= 10) {
            PathNode cseNode = new PathNode(lblCSE + String.format("%03d", i));
            getMapCSE().add(cseNode);

            link(cseNode, previousNode, cost);
            previousNode = cseNode;
            i++;
        }
        getMap().addAll(getMapCSE());
    }

    /**
     * Creates PathNodes of all TrainCranes
     */
    private void createCTR() {
        float cost = smallCost * 18; //was 18
        int i = 2;
        PathNode previousNode = new PathNode(lblCTR + "001");
        getMapCTR().add(previousNode);

        while (i <= 4) {
            PathNode ctrNode = new PathNode(lblCTR + String.format("%03d", i));
            getMapCTR().add(ctrNode);
            link(ctrNode, previousNode, cost);
            previousNode = ctrNode;
            i++;
        }
        getMap().addAll(getMapCTR());
    }

    /**
     * Creates PathNodes of all LorryCranes
     */
    private void createCLO() {
        float cost = smallCost;
        PathNode previousNode = new PathNode(lblCLO + "001");
        int i = 2;
        getMapCLO().add(previousNode);

        while (i <= 20) {
            PathNode cloNode = new PathNode(lblCLO + String.format("%03d", i));
            getMapCLO().add(cloNode);
            link(cloNode, previousNode, cost);
            previousNode = cloNode;
            i++;
        }
        getMap().addAll(getMapCLO());
    }

    /**
     * Creates all MainRoad PathNodes and connects all existing PathNodes to
     * these MainRoad PathNodes
     */
    private void connectGreen() {
        //green boundary points connecting buffer row A
        PathNode[] greenR = new PathNode[16];
        for (int i = 0; i < greenR.length; i++) {
            greenR[i] = new PathNode(lblM + (i + 1));
            getMapMR().add(greenR[i]);
        }

        //connect row BB with green dots
        link(greenR[0], getMapBB().get(0), smallCost * 2);
        this.insertNode(lblBB + "030", lblBB + "031", greenR[3], smallCost, smallCost);
        this.insertNode(lblBB + "034", lblBB + "035", greenR[4], smallCost, smallCost);
       // this.insertNode(lblBB + "044", lblBB + "045", greenR[5], smallCost, smallCost);
      
        link(greenR[6], getMapBB().get(62), smallCost * 6);

        //connect green dots counter clockwise
        link(greenR[0], greenR[1], smallCost * 3);// s
        link(greenR[1], getMapCBA().get(0), smallCost * 14);//e
        link(greenR[2], getMapCBA().get(7), smallCost * 6);// n
        link(greenR[2], greenR[3], smallCost * 3);
        link(greenR[4], getMapCLO().get(0), smallCost * 2);
        link(getMapCLO().get(19),getMapBB().get(43), smallCost);
        //link(greenR[5], getMapCLO().get(19), smallCost * 2);
        link(greenR[6], greenR[7], smallCost * 40);
        link(greenR[7], greenR[8], smallCost * 4);
        link(greenR[8], greenR[9], smallCost * 3);
        link(greenR[8], getMapBA().get(62), smallCost);
        link(greenR[9], getMapCTR().get(3), smallCost * 28);
        link(greenR[10], getMapCTR().get(0), smallCost * 68);
        link(greenR[10], greenR[11], smallCost * 3);
        this.insertNode(lblBA + "001", lblBA + "002", greenR[11], smallCost, smallCost);
        link(greenR[12], getMapBA().get(0), smallCost * 2); //
        link(greenR[12], greenR[15], smallCost * 38);
        link(greenR[12], greenR[13], smallCost * 3);
        link(greenR[13], getMapCSE().get(0), smallCost * 4); 
        link(greenR[14], getMapCSE().get(9), smallCost * 4);
        link(greenR[14], greenR[15], smallCost * 3);
        link(greenR[15], greenR[0], smallCost * 3);

        getMap().addAll(getMapMR());
    }

    /**
     * Connects two PathNodes to each other
     *
     * @param a = PathNode 1
     * @param b = PathNode 2
     * @param cost = cost of 1 to 2
     */
    private void link(PathNode a, PathNode b, float cost) {
        a.addNeighbour(b, cost);
        b.addNeighbour(a, cost);
    }

    /**
     * De-connects two PathNodes from each other
     *
     * @param a = PathNode 1
     * @param b = PathNode 2
     */
    private void delink(PathNode a, PathNode b) {
        a.removeNeighbour(b);
        b.removeNeighbour(a);
    }

    /**
     * Overload of getShortestPath(String,String,boolean)
     *
     * @param srce
     * @param dest
     * @return
     * @throws Exception
     */
    public List<PathNode> getShortestPath(PathNode srce, PathNode dest) throws Exception {
        try {
            return getShortestPath(srce, dest, false);
        } catch (Exception ex) {
            throw ex;
        }
    }

    /**
     * Calculates the shortest Path from a source to a destination by using the
     * Dijkstra Algorithm Checks each neighbor of a PathNode starting from
     * source if neighbor-cost is larger than current-cost, neighbor-cost is
     * current-cost, neighbor-parent is current PathNode in the end loop back
     * from destination to source by adding all parents to list the list is the
     * shortest path
     *
     * @param srce
     * @param dest
     * @param optimize = if true-> method-call to optimize() to optimize the
     * path
     * @return
     * @throws Exception
     */
    public List<PathNode> getShortestPath(PathNode srce, PathNode dest, boolean optimize) throws Exception {
        List<PathNode> alreadyChecked = new ArrayList();
        List<PathNode> shortestRoute = new ArrayList();
        List<PathNode> unSettledNodes = new ArrayList();
        
      
        
        PathNode srceNode = srce;
        PathNode destNode = dest;

        if (srceNode != null && destNode != null) {
            srceNode.setCost(0, null); //start cost}
            unSettledNodes.add(srceNode);
            float totalCost = 1000;
            while (unSettledNodes.size() > 0) {
                PathNode node = getMinimum(unSettledNodes);
                alreadyChecked.add(node);
                unSettledNodes.remove(node);

                for (PathNode neighbour : node.getNeighbours()) {
                    float curCost = node.getCost() + node.getCostNeighb(neighbour);
                    if (alreadyChecked.contains(neighbour) || curCost > totalCost) {
                        continue;
                    }

                    if (neighbour.getCost() == -1 || neighbour.getCost() > curCost) {
                        neighbour.setCost(curCost, node);
                    }
                    if (neighbour.equals(destNode)) {
                        if (neighbour.getCost() < curCost) {
                            totalCost = curCost;
                        }
                    }
                    unSettledNodes.add(neighbour);
                }
            }
            
            
            
            shortestRoute.add(destNode); //add dest to shortest path
            destNode.getRoute(shortestRoute); //create shortest path
            Collections.reverse(shortestRoute); //reverse path , srce to dest
           
            if (optimize) {
                optimize(shortestRoute); //delete useless nodes
            }
             for(PathNode node :alreadyChecked)
                  {
                      node.setCost(-1, null);
                  }
             
          
            return shortestRoute;
        } else {
            throw new Exception("source and/or destination not found in map");
        }

    }

    /**
     *
     * @param id
     * @return
     */
    private PathNode getPathNode(String id) {
        for (int i = 0; i < getMap().size(); i++) {
            PathNode curNode = getMap().get(i);
            if (curNode.getId().equals(id)) {
                return curNode;
            }
        }
        return null;
    }

    /**
     * optimizes the path by deleting nodes on a linear path east-east-east will
     * be optimized as east-east, east-north-east cant be optimized.
     *
     * @param shortestRoute
     */
    public void optimize(List<PathNode> shortestRoute) {
        for (int i = 1; i < shortestRoute.size() - 1; i++) {

            String prevID = shortestRoute.get(i - 1).getId();
            String curID = shortestRoute.get(i).getId();
            String nextID = shortestRoute.get(i + 1).getId();

            if (prevID.length() > 2 && curID.length() > 2 && nextID.length() > 2) {
                if (curID.substring(0, 3).equals(nextID.substring(0, 3)) && curID.substring(0, 3).equals(prevID.substring(0, 3))) {
                    shortestRoute.remove(i);
                    i--;
                }
            }
        }
    }

    /**
     * returns PathNode with lowest cost
     *
     * @param neighbours
     * @return
     */
    public PathNode getMinimum(List<PathNode> neighbours) {
        PathNode minnode = null;
        float mindistance = 9999f;
        for (PathNode node : neighbours) {
            if (node.getCost() < mindistance) {
                mindistance = node.getCost();
                minnode = node;
            }
        }
        return minnode;
    }

    /**
     * @return the complete map
     */
    public List<PathNode> getMap() {
        return map;
    }

    /**
     * @return the mapCBA
     */
    public List<PathNode> getMapCBA() {
        return mapCBA;
    }

    /**
     * @return the mapCTR
     */
    public List<PathNode> getMapCTR() {
        return mapCTR;
    }

    /**
     * @return the mapCLO
     */
    public List<PathNode> getMapCLO() {
        return mapCLO;
    }

    /**
     * @return the mapMR
     */
    public List<PathNode> getMapMR() {
        return mapMR;
    }

    /**
     * @return the mapBA
     */
    public List<PathNode> getMapBA() {
        return mapBA;
    }

    /**
     * @return the mapBB
     */
    public List<PathNode> getMapBB() {
        return mapBB;
    }

    /**
     * @return the mapCSE
     */
    public List<PathNode> getMapCSE() {
        return mapCSE;
    }
}
