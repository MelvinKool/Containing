package org.nhl.containing.vehicles;

import com.jme3.asset.AssetManager;
import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import org.nhl.containing.Container;

public class Lorry extends Transporter {

    private AssetManager assetManager;
    private int lorryZAxis = 11;
    private Container container;
    private MotionPath path;
    private MotionEvent motionControl;

    public Lorry(AssetManager assetManager, int id, Container c) {
        super(id);
        this.assetManager = assetManager;
        this.container = c;
        speed = 1f;
        initLorry();
        initMotionPaths();
    }

    public Container getContainer() {
        return container;
    }
    /**
     * Initialize a lorry.
     */
    private void initLorry() {
        // Load a model.
        Node lorry = (Node) assetManager.loadModel("Models/low/truck.j3o");
        lorry.setLocalTranslation(0, 0, lorryZAxis);
        container.setLocalTranslation(0, 1, lorryZAxis);
        this.attachChild(lorry);
        this.attachChild(container);
    }

    /**
     * Initialize motionpath and motionevent
     */
    private void initMotionPaths() {
        path = new MotionPath();
        motionControl = new MotionEvent(this, path);
    }
    /**
     * makes the lorrys arrive at the given position
     * @param location the parking spot the lorry will arrive at (0-19)
     */
    @Override
    public void arrive(int location) {
        motionControl.setSpeed(speed);
        path.clearWayPoints();
        path.addWayPoint(new Vector3f(566 - (14 * location), 0, 185));
        path.addWayPoint(new Vector3f(566 - (14 * location), 0, 159));
        path.addListener(this);
        motionControl.play();
    }
    /**
     * Lets the lorry depart
     */
    @Override
    public void depart() {
        motionControl.setSpeed(speed);
        path.clearWayPoints();
        path.addWayPoint(this.getWorldTranslation());
        path.addWayPoint(new Vector3f(this.getLocalTranslation().x, 0, 159));
        path.addWayPoint(new Vector3f(this.getLocalTranslation().x, 0, 185));
        path.addListener(this);
        motionControl.play();
    }
    /**
     * Debug method, displays object name, speed, amount of containers and it's
     * waypoints.
     *
     * @return debug information about the object
     */
    public String getDebugInfo() {
        String info = this.getClass().getSimpleName() + "\nSpeed: " + speed + "\nLocation: " + this.getLocalTranslation() + "\nCarrying: ";
        if (container != null) {
            info += "1 Container.\n";
        } else {
            info += "nothing.\n";
        }
        for (int i = 0; i < path.getNbWayPoints(); i++) {
            info += "Waypoint " + (i + 1) + ": " + path.getWayPoint(i) + " ";
        }
        return info + "\n";
    }

    public void addContainer(Container container){
        this.container = container;
    }
    
    public void removeContainer(){
        this.container = null;
    }
    
    /**
     * debug method that returns the waypoints of the given truck
     *
     * @param place parking place number
     * @return string containing the waypoints/debug info
     */
    public String getWaypoints(int place) {
        String info = "\nWaypoints truck #" + place + " : ";
        for (int j = 0; j < path.getNbWayPoints(); j++) {
            info += "Waypoint " + (j + 1) + ": " + path.getWayPoint(j) + " ";
        }
        return info + "\n";
    }
    
    @Override
    public void onWayPointReach(MotionEvent motionControl, int wayPointIndex) {
        if( wayPointIndex + 1 == path.getNbWayPoints())
        {
            setArrived(true);
        }
    }
}
