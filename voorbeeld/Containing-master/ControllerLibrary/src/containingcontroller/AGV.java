/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingcontroller;

import containing.xml.Message;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ruben
 */
public class AGV {

    private boolean ready = false;
    static private int id = 1;
    /**
     * Indicating if cart is home
     */
    private boolean isHome;
    /**
     * Container that the AGV carries
     */
    public Container container;
    /**
     * Name of the home buffer
     */
    public PathNode home;
    /**
     * the home of the agv
     */
    public Buffer homeBuffer;
    /**
     * Name of the AGV
     */
    public String name;

    /**
     * Constructor
     *
     * @param home
     * @param homeBuffer
     * @param name
     */
    public AGV(PathNode home, Buffer homeBuffer, String name) {
        this.home = home;
        this.name = name;
        this.homeBuffer = homeBuffer;
        this.ready = true;
    }

    AGV(PathNode upperNode, Buffer b) {
        this(upperNode, b, "AGV" + String.format("%03d", id++));
        this.isHome = true;
        this.ready = true;
    }

    /**
     * Command the AGV to move to crane
     *
     * @param c
     * @param destination
     */
    public void moveToCrane(Crane destination, Controller c) {
        try {
            isHome = false;
            PathFinder finder = c.getPathFinder();
            List<PathNode> path = finder.getShortestPath(home, destination.node, true);
            Message moveMessage = new Message(Commands.MOVE, null);
            ArrayList<String> nodeIds = new ArrayList<String>();
            for (PathNode node : path) {
                nodeIds.add(node.getId());
            }
            nodeIds.add(0, this.name);
            moveMessage.setParameters(nodeIds.toArray());
            c.sendMessage(moveMessage);
        } catch (Exception ex) {
            Logger.getLogger(AGV.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Send AGV home
     *
     * @param source
     * @param c
     */
    public void moveToHome(Crane source, Controller c) {
        try {
            PathFinder finder = c.getPathFinder();
            List<PathNode> path = finder.getShortestPath(source.node, this.home, false);
            Message moveMessage = new Message(Commands.MOVE, null);
            ArrayList<String> parameters = new ArrayList<String>();
            parameters.add(this.name);
            for (PathNode node : path) {
                parameters.add(node.getId());
            }
            moveMessage.setParameters(parameters.toArray());
            c.sendMessage(moveMessage);
        } catch (Exception ex) {
            Logger.getLogger(AGV.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    boolean getIsReady() {
        return ready;
    }

    void setIsReady(boolean newValue) {
        ready = newValue;
    }

    @Override
    public String toString() {
        return "AGV{" + "name=" + name + ", isHome=" + isHome + ", container=" + container + ", home=" + home + ", homeBuffer=" + homeBuffer.id + '}';
    }

    /**
     * check if agv is ready
     *
     * @return
     */
    public boolean isReady() {
        return ready;
    }

    /**
     * set if agv is ready
     *
     * @param ready
     */
    public void setReady(boolean ready) {
        this.ready = ready;
    }

    /**
     * check if agv is gome
     *
     * @return
     */
    public boolean isIsHome() {
        return isHome;
    }

    /**
     * set if agv is home
     *
     * @param isHome
     */
    public void setIsHome(boolean isHome) {
        this.isHome = isHome;
    }
}
