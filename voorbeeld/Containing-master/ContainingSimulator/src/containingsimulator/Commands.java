/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingsimulator;

/**
 *
 * @author Hendrik
 */
public class Commands {
    

    /**
     * Shutdown
     */
    static public final int SHUTDOWN = 9;
    
    /**
     * Defect
     */
    static public final int DEFECT= 13;
    
    /**
     * Move crane
     */
    static public final int MOVE_CRANE = 12;
    
    /**
     * Retreive infor from controller
     */
    static public final int RETREIVE_INFO = 11;
    
    /**
     * Pause or play
     */
    static public final int PAUSE_PLAY = 10;
    /**
     *Device is ready
     */
    static public final int READY = 0;
     
    /**
     *Move AGV
     */
    static public final int MOVE = 1;
    
    /**
     *Crane needs to pickup CONTAINER from buffer or transport
     */
    static public final int PICKUP_CONTAINER = 2;
    
    /**
     *Give container to AGV
     */
    static public final int GIVE_CONTAINER = 3;
    
    /**
     *Crane needs to put CONTAINER down on buffer or transport
     */
    static public final int PUT_CONTAINER = 4;
    
    /**
     * Crane gets container from AGV
     */
    static public final int GET_CONTAINER = 5;
    
    /**
     * Simulator creates a new transport
     */
    static public final int CREATE_TRANSPORTER = 6;
    
    /**
     * Remove transporter from simulation
     */
    static public final int REMOVE_TRANSPORTER = 7;
    
    /**
     * Change simulator speed
     */
    static public final int CHANGE_SPEED = 8;
}
