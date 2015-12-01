package org.nhl.containing.vehicles;

import com.jme3.asset.AssetManager;
import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.util.List;
import org.nhl.containing.Container;

public class Seaship extends Transporter {

    private AssetManager assetManager;
    private List<Container> containerList;
    // zeeship Dit is x=0 Y=0 Z=0, links onderin ( als je vanaf de achterkant kijkt ) 
    // Hier passen op de x as 16 containers op
    private final int zexAs = -23;
    private final int zeyAs = 0;
    private final int zezAs = 160;
    private int containerCounter = 0;
    private float x = -23;
    private float y = 0;
    private float z = 160;
    private Spatial boat;
    private MotionPath path;
    private MotionEvent motionControl;

    public Seaship(AssetManager assetManager, int id, List<Container> containerList) {
        super(id);
        this.assetManager = assetManager;
        this.containerList = containerList;
        speed = 0.5f;
        initSeaship();
        initMotionPaths();
    }

    /**
     * Initialize a boat.
     */
    private void initSeaship() {
        try {
            // Load a model.
            boat = assetManager.loadModel("Models/medium/ship/seaship.j3o");
            boat.scale(0.87f, 1, 0.57f);
            this.attachChild(boat);
            for (int i = 0; i < containerList.size(); i++) {
                float containerLength = containerList.get(i).getBoundingBox().getYExtent() * 10;
                float x = zexAs + containerList.get(i).getSpawnY() * 3;
                float z = zezAs - containerList.get(i).getSpawnX() * containerLength;
                float y = zeyAs + containerList.get(i).getSpawnZ() * 3f;
                containerList.get(i).setLocalTranslation(x, y, z);
                this.attachChild(containerList.get(i));
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialize motionpath and motionevent
     */
    private void initMotionPaths() {
        path = new MotionPath();
        motionControl = new MotionEvent(this, path);
        motionControl.setRotation(new Quaternion().fromAngleNormalAxis((float) Math.PI, Vector3f.UNIT_Y));
    }

    /**
     * Lets the seaship arrive
     *
     * @param location not used
     */
    @Override
    public void arrive(int location) {
        motionControl.setSpeed(speed);
        path.clearWayPoints();
        path.addWayPoint(new Vector3f(-750, 0, 500));
        path.addWayPoint(new Vector3f(-330, 0, -20));
        path.setCurveTension(0.3f);
        path.addListener(this);
        motionControl.play();
    }

    /**
     * Makes this seaship depart
     */
    @Override
    public void depart() {
        motionControl.setSpeed(speed);
        path.clearWayPoints();
        path.addWayPoint(new Vector3f(-330, 0, -20));
        path.addWayPoint(new Vector3f(-345, 0, -300));
        path.setCurveTension(0.3f);
        path.addListener(this);
        motionControl.play();
    }
    
    public void addContainer(Container container){
        this.containerList.add(container);
    }
    
    public void removeContainer(Container container){
        this.containerList.remove(container);
    }

    /**
     * Debug method, displays object name, speed, amount of containers and it's
     * waypoints.
     *
     * @return information about this object
     */
    public String getDebugInfo() {
        String info = this.getClass().getSimpleName() + "\nSpeed: " + speed + "\nLocation: " + this.getLocalTranslation() + "\nCarrying: " + containerList.size() + " containers.\n";
        for (int i = 0; i < path.getNbWayPoints(); i++) {
            info += "Waypoint " + (i + 1) + ": " + path.getWayPoint(i) + " ";
        }
        return info + "\n";
    }

    /**
     * Debug method, displays object name, speed, amount of containers and it's
     * waypoints.
     *
     * @return information about this object
     */
    public String getWaypoints() {
        String info = "\nSeaship waypoints ";
        for (int j = 0; j < path.getNbWayPoints(); j++) {
            info += "Waypoint " + (j + 1) + ": " + path.getWayPoint(j) + " ";
        }
        return info + "\n";
    }

    /**
     * Get the next empty spot on the ship.
     *
     * @return Vector of next empty location
     */
    public Vector3f getNextSpot() {
        x += 2.5f;
        if (x >= 22) {
            x = -20.5f;
            z -= 13.5f;
        }
        if (z <= -137) {
            x = -20.5f;
            z = 160;
            y += 2.9f;
        }
        containerCounter++;
        return new Vector3f(x, y, z);

    }

    @Override
    public void onWayPointReach(MotionEvent motionControl, int wayPointIndex) {
        if (wayPointIndex + 1 == path.getNbWayPoints()) {
            setArrived(true);
        }
    }
}
