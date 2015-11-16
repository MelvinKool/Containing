/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingcontroller;

/**
 *
 * @author Ruben
 */
public class Crane {

    /**
     * LastX to detect wich container has to be pickedup
     */
    public int lastX = 13;
    
    /**
     * kind of crane
     */
    final static public int LorryCrane = 0;
    /**
     *kind of crane
     */
    final static public int TrainCrane = 1;
    /**
     *kind of crane
     */
    final static public int SeaCrane = 2;
    /**
     *kind of crane
     */
    final static public int BargeCrane = 3;
    /**
     *kind of crane
     */
    final static public int BufferCrane = 4;
    /**
     * Crane ID
     */
    public String id;
    /**
     * waypoint node of crane
     */
    public PathNode node;
    Container container;
    /**
     * start from range
     */
    public int startRange = 0;
    /**
     * Range for crane
     */
    public int range = 0;
    int type;

    /**
     * create crane with id
     *
     * @param id
     * @param type  
     */
    public Crane(String id, int type) {
        this.id = id;
        this.type = type;
    }

    /**
     *
     * @param cont
     */
    public void loadContainer(Container cont) {
    }

    /**
     *
     * @param cont
     */
    public void getContainer(Container cont) {
    }
    boolean ready = true;

    boolean getReady() {
        return ready;
    }

    void setIsReady(boolean b) {
        ready = b;
    }

    @Override
    public String toString() {
        return "Crane{" + "id=" + id + ", node=" + node + ", ready=" + ready + '}';
    }
}
