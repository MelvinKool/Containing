/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingcontroller;

/**
 *
 * @author Hendrik
 */
public class Commands {

    /**
     * Set object as defect so it doesn't function anymore
     */
    final static public int DEFECT = 13;

    /**
     * Move crane to posistion to load a container in to a transporter
     */
    final static public int MOVE_CRANE = 12;
    /**
     * Device is ready
     */
    final static public int READY = 0;

    /**
     *     Get extra info
     */
    
    final static public int RETREIVE_INFO = 11;

    /**
     * Shutdown simulator
     */
    final static public int SHUTDOWN = 9;
    /**
     * Container must be moved by AVG
     */
    static public int MOVE = 1;
    /**
     * Crane needs to pickup CONTAINER
     */
    static public int PICKUP_CONTAINER = 2;
    /**
     * Crane needds to give CONTAINER to AGV
     */
    static public int GIVE_CONTAINER = 3;
    /**
     * Create transporter with containers
     */
    static public int PUT_CONTAINER = 4;
    /**
     * Remove transporter
     */
    static public int GET_CONTAINER = 5;
    /**
     * Container must be moved by AVG
     */
    static public int CREATE_TRANSPORTER = 6;

    /**
     * remove transporter from simulator
     */
    static public int REMOVE_TRANSPORTER = 7;

    /**
     * change speed
     */
    static public int CHANGE_SPEEED = 8;

    /**
     * change speed
     */
    static public int PAUSE_PLAY = 10;
}
