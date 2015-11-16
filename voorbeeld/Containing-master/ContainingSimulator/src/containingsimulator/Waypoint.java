/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingsimulator;

import com.jme3.math.Vector3f;

/**
 *
 * @author Sietse
 */
class Waypoint {
    
    public float speed;
    public Vector3f location;
    public String name;
    
    /**
     * Constructor
     * @param location waypoint location 
     * @param speed intended speed for this waypoint
     */
    public Waypoint(Vector3f location, float speed){
        this.location = location;
        this.speed = speed;
    }
}
