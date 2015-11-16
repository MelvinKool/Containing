/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingsimulator;

import com.jme3.math.Vector3f;

/**
 * Represents a parking spot for an AGV inside the simulator
 * @author Sietse
 */
public class ParkingSpot {
    public Vector3f translation;        //local translation of parking space
    public float rotation;              //rotation of parking space
    public boolean occupied;            //whether this spot is occupied or not
    
    /**
     * Constructor
     * @param translation the location of this parking spot
     * @param rotation the rotation for the parked vehicle
     */
    public ParkingSpot(Vector3f translation, float rotation){
        this.translation = translation;
        this.rotation = rotation;
        this.occupied = false;
    }
}
