package org.nhl.containing.vehicles;

import com.jme3.asset.AssetManager;
import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import org.nhl.containing.Container;

public class Agv extends Vehicle {

    private AssetManager assetManager;
    private Container container;
    private MotionPath dijkstraPath;
    private MotionPath depotPath;
    private boolean atDepot;
    private boolean readyToLeave;
    private int parkingSpot;
    MotionEvent motionControl;
    private float agvparkX;
    private float agvparkZ;
    int parkedAt = 0; // 0 = storage | 1 = seaship | 2 = inlandship | 3 = train | 4 = lorry
    
    public Agv(AssetManager assetManager, int id, float X, float Z) {
        super(id);
        this.assetManager = assetManager;
        speed = 0.5f;
        initAgv();
        initMotionPaths();
        agvparkX = X;
        agvparkZ = Z;
    }

    public int getParkingSpot() {
        return parkingSpot;
    }

    public void setParkingSpot(int parked) {
        this.parkingSpot = parked;
    }

    public boolean isReadyToLeave() {
        return readyToLeave;
    }

    public void setReadyToLeave(boolean readyToLeave) {
        this.readyToLeave = readyToLeave;
    }

    public boolean isAtDepot() {
        return atDepot;
    }
    public void setParked(int platform){
        parkedAt = platform;
    }
    
    /**
     * Initialize an Agv.
     */
    private void initAgv() {
        // Load a model.
        Spatial agv = assetManager.loadModel("Models/low/agv/agv.j3o");
        this.attachChild(agv);
    }

    /**
     * Initialize motionpath and motionevent
     */
    private void initMotionPaths() {
        dijkstraPath = new MotionPath();
        depotPath = new MotionPath();
        motionControl = new MotionEvent(this, dijkstraPath);
        motionControl.setSpeed(speed);
        // set the speed and direction of the AGV using motioncontrol
        motionControl.setDirectionType(MotionEvent.Direction.PathAndRotation);
        motionControl.setRotation(new Quaternion().fromAngleNormalAxis(0, Vector3f.UNIT_Y));
    }

    /**
     * Method that moves this agv over the given path
     *
     * @param path character arraylist filled with the waypoints
     */
    public void move(String route) {
        dijkstraPath.clearWayPoints();
        //depart from parked location before following the dijkstra path
        switch (parkedAt){
            case 0:
                leaveStoragePlatform();
                break;
            case 1:
                leaveSeashipPlatform();
                break;
            case 2:
                leaveInlandshipPlatform();
                break;
            case 3:
                leaveTrainPlatform();
                break;
            case 4:
                leaveLorryPlatform();
                break;
        }
        for (char waypoint : route.toCharArray()) {
            switch (waypoint) {
                case 'A':
                    dijkstraPath.addWayPoint(new Vector3f(580, 0, -140));
                    break;
                case 'B':
                    dijkstraPath.addWayPoint(new Vector3f(580, 0, 135));
                    break;
                case 'C':
                    dijkstraPath.addWayPoint(new Vector3f(330, 0, -140));
                    break;
                case 'D':
                    dijkstraPath.addWayPoint(new Vector3f(330, 0, 136));
                    break;
                case 'E':
                    dijkstraPath.addWayPoint(new Vector3f(70, 0, -140));
                    break;
                case 'F':
                    dijkstraPath.addWayPoint(new Vector3f(70, 0, 135));
                    break;
                case 'G':
                    dijkstraPath.addWayPoint(new Vector3f(-210, 0, -140));
                    break;
                case 'H':
                    dijkstraPath.addWayPoint(new Vector3f(-210, 0, 135));
                    break;
                //Enter trainplatform path
                case 'I':
                    dijkstraPath.addWayPoint(new Vector3f(60, 0, -171));
                    dijkstraPath.addWayPoint(new Vector3f(30, 0, -171));
                    break;
                //Enter seaship path
                case 'J':
                    dijkstraPath.addWayPoint(new Vector3f(-240, 0, -150));
                    dijkstraPath.addWayPoint(new Vector3f(-283, 0, -150));
                    break;
                //Enter inlandship path
                case 'K':
                    dijkstraPath.addWayPoint(new Vector3f(-230, 0, 162));
                    dijkstraPath.addWayPoint(new Vector3f(-285, 0, 162));
                    dijkstraPath.addWayPoint(new Vector3f(-285, 0, 177));
                    break;
                case 'L':
                    dijkstraPath.addWayPoint(new Vector3f(455, 0, -140));
                    break;
                case 'M':
                    dijkstraPath.addWayPoint(new Vector3f(455, 0, 135));
                    break;
                case 'N':
                    dijkstraPath.addWayPoint(new Vector3f(200, 0, 135));
                    break;
                case 'O':
                    dijkstraPath.addWayPoint(new Vector3f(200, 0, -140));
                    break;
                case 'P':
                    dijkstraPath.addWayPoint(new Vector3f(-70, 0, -140));
                    break;
                case 'Q':
                    dijkstraPath.addWayPoint(new Vector3f(-70, 0, 135));
                    break;
            }
        }
        dijkstraPath.setCurveTension(0.1f);
        dijkstraPath.addListener(this);
        setArrived(false);
        motionControl.setPath(dijkstraPath);
        motionControl.play();
    }

    /**
     * Makes the agv park on the trainplatform AGV should be at location I
     *
     * @param location the parking place
     */
    public void parkAtTrainPlatform(int location) {
        //make the first waypoint it's current location
        depotPath.clearWayPoints();
        depotPath.addWayPoint(this.getWorldTranslation());
        depotPath.addWayPoint(new Vector3f(30, 0, -171));
        depotPath.addWayPoint(new Vector3f(-178 + (20 * location), 0, -172));
        depotPath.addWayPoint(new Vector3f(-188 + (20 * location), 0, -176));
        depotPath.addWayPoint(new Vector3f(-190 + (20 * location), 0, -176));
        agvparkX = -190 + (20 * location);
        agvparkZ = -176;
        depotPath.setCurveTension(0.1f);
        depotPath.addListener(this);
        atDepot = false;
        motionControl.setPath(depotPath);
        motionControl.play();
        parkedAt = 3;
    }

    /**
     * Makes the agv park on the seashipplatform AGV should be at location J
     *
     * @param location the parking place
     */
    public void parkAtSeashipPlatform(int location) {
        depotPath.clearWayPoints();
        depotPath.addWayPoint(this.getWorldTranslation());
        depotPath.addWayPoint(new Vector3f(-284, 0, -146));
        depotPath.addWayPoint(new Vector3f(-284, 0, 123 - (20 * location)));
        depotPath.addWayPoint(new Vector3f(-289, 0, 132 - (20 * location)));
        depotPath.addWayPoint(new Vector3f(-289, 0, 135 - (20 * location)));
        agvparkX = -289;
        agvparkZ = 135 - (20 * location);
        depotPath.setCurveTension(0.1f);
        depotPath.addListener(this);
        atDepot = false;
        motionControl.setPath(depotPath);
        motionControl.play();
        parkedAt = 1;
    }

    /**
     * Makes the agv park on the inlandshipplatform. AGV should be at location K
     *
     * @param location the parking place
     */
    public void parkAtInlandshipPlatform(int location) {
        depotPath.clearWayPoints();
        depotPath.addWayPoint(this.getWorldTranslation());
        depotPath.addWayPoint(new Vector3f(-285, 0, 177));
        depotPath.addWayPoint(new Vector3f(130 - (20 * location), 0, 177));
        depotPath.addWayPoint(new Vector3f(143 - (20 * location), 0, 183));
        depotPath.addWayPoint(new Vector3f(147 - (20 * location), 0, 183));
        agvparkX = 147 - (20 * location);
        agvparkZ = 183;
        depotPath.setCurveTension(0.1f);
        depotPath.addListener(this);
        atDepot = false;
        motionControl.setPath(depotPath);
        motionControl.play();
        parkedAt = 2;
    }

    /**
     * Makes AGV park under a crane on the lorryplatform The given location
     * determines which crane it will park under AGV should be at location N
     *
     * @param location the crane and parking spot
     */
    public void parkAtLorryPlatform(int location) {
        depotPath.clearWayPoints();
        depotPath.addWayPoint(this.getWorldTranslation());
        depotPath.addWayPoint(new Vector3f(566 - (14 * location), 0, 135));
        depotPath.addWayPoint(new Vector3f(566 - (14 * location), 0, 145));
        depotPath.addWayPoint(new Vector3f(566 - (14 * location), 0, 156));
        agvparkX = (566 - (14 * location));
        agvparkZ = 156;
        depotPath.addListener(this);
        atDepot = false;
        motionControl.setPath(depotPath);
        motionControl.play();
        parkedAt = 4;
    }

    /**
     * Makes the AGV park on the storage platform Side to park is recognized
     * automatically by using the z axis which platform to park at is recognized
     * by the location/waypoint it is standing on
     *
     * @param location
     */
    public void parkAtStoragePlatform(int location) {
        depotPath.clearWayPoints();
        motionControl.setPath(depotPath);
        location += 1;
        int temp = location;
        int xStartPoint;
        float eastorwest;
        //determine which side the AGV is at
        if (this.getWorldTranslation().z < 0) {
            eastorwest = -122f;
        } else {
            eastorwest = 113f;
        }
        //if the agv is lower than x=70 its at ship platform
        if (this.getWorldTranslation().x < 70) {
            xStartPoint = -167;
        } //if lower than 330 train platform
        else if (this.getWorldTranslation().x < 330) {
            xStartPoint = 113;
        } //else it's at the lorry platform
        else {
            xStartPoint = 363;
        }
        //AGVs spawn in groups of 6, after 6 a extra distance is added
        while (temp > 6) {
            temp -= 6;
            xStartPoint += 22;
        }
        depotPath.addWayPoint(new Vector3f(this.getWorldTranslation()));
        depotPath.addWayPoint(new Vector3f(xStartPoint + (4.7f * location), 0, this.getWorldTranslation().z));
        depotPath.addWayPoint(new Vector3f(xStartPoint + (4.7f * location), 0, eastorwest));
        agvparkX = xStartPoint + (4.7f * location);
        agvparkZ = eastorwest;
        motionControl.play();
        parkedAt = 0;
    }

    /**
     * Method for making a parked AGV leave the Seashipplatform
     */
    public void leaveSeashipPlatform() {
        dijkstraPath.addWayPoint(new Vector3f(agvparkX, 0, agvparkZ));
        dijkstraPath.addWayPoint(new Vector3f(agvparkX + 7, 0, agvparkZ + 5));
        dijkstraPath.addWayPoint(new Vector3f(agvparkX + 7, 0, 156));
        dijkstraPath.addWayPoint(new Vector3f(-205, 0, 156));
    }

    /**
     * Method for making a parked AGV leave the inlandship platform
     */
    public void leaveLorryPlatform() {
        dijkstraPath.addWayPoint(new Vector3f(agvparkX, 0, agvparkZ));
        dijkstraPath.addWayPoint(new Vector3f(agvparkX, 0, agvparkZ - 20));
        dijkstraPath.addWayPoint(new Vector3f(330, 0, 136));
    }

/**
     * Determine in which storagearea this AGV is parked and send the AGV to the
     * nearby waypoint
     */
    public void leaveStoragePlatform() {
            dijkstraPath.addWayPoint(new Vector3f(agvparkX, 0, agvparkZ));
            if (agvparkZ == -122) {
                dijkstraPath.addWayPoint(new Vector3f(agvparkX, 0, agvparkZ-18));
            }
            else{
                dijkstraPath.addWayPoint(new Vector3f(agvparkX, 0, agvparkZ+22));
            }
    }

    /**
     * Method for making a parked AGV leave the inlandship platform
     */
    public void leaveInlandshipPlatform() {
        depotPath.clearWayPoints();
        depotPath.addWayPoint(this.getWorldTranslation());
        depotPath.addWayPoint(new Vector3f(agvparkX, 0, agvparkZ));
        depotPath.addWayPoint(new Vector3f(agvparkX + 15, 0, agvparkZ - 7));
        depotPath.addWayPoint(new Vector3f(180, 0, 177));
        depotPath.addWayPoint(new Vector3f(180, 0, 140));
        depotPath.setCurveTension(0.1f);
        depotPath.addListener(this);
        atDepot = false;
        motionControl.setPath(depotPath);
        motionControl.play();
    }

    /**
     * Method for making a parked AGV leave the trainplatform
     */
    public void leaveTrainPlatform() {
        depotPath.clearWayPoints();
        depotPath.addWayPoint(this.getWorldTranslation());
        depotPath.addWayPoint(new Vector3f(agvparkX, 0, agvparkZ));
        depotPath.addWayPoint(new Vector3f(agvparkX - 5, 0, agvparkZ + 5));
        depotPath.addWayPoint(new Vector3f(-210, 0, -171));
        depotPath.addWayPoint(new Vector3f(-210, 0, -140));
        depotPath.setCurveTension(0.3f);
        depotPath.addListener(this);
        atDepot = false;
        motionControl.setPath(depotPath);
        motionControl.play();
    }

    public void addContainer(Container container) {
        this.container = container;
    }

    public void removeContainer() {
        this.container = null;
    }

    /**
     * path.addListener(this); // set the speed and direction of the AGV using
     * motioncontrol
     * motionControl.setDirectionType(MotionEvent.Direction.PathAndRotation);
     * motionControl.setRotation(new Quaternion().fromAngleNormalAxis(0,
     * Vector3f.UNIT_Y)); motionControl.setSpeed(speed); motionControl.play(); }
     *
     * /**
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
        //get waypoints for the AGV (does not exist in this class yet)
//        for (int i = 0; i < path.getNbWayPoints(); i++) {
//            info += "Waypoint " + (i+1) + ": " + path.getWayPoint(i) + " ";
//        }
        return info + "\n";
    }

    /**
     * Gets all created waypoints
     *
     * @return string with the waypoints
     */
    public String getWaypoints() {
        String info = "\nAGV Waypoints: ";
        for (int j = 0; j < dijkstraPath.getNbWayPoints(); j++) {
            info += "Waypoint " + (j + 1) + ": " + dijkstraPath.getWayPoint(j) + " ";
        }
        return info + "\n";
    }

    @Override
    public void onWayPointReach(MotionEvent motionControl, int wayPointIndex) {
        if (dijkstraPath != null && wayPointIndex + 1 == dijkstraPath.getNbWayPoints()) {
            setArrived(true);
        }
        if (depotPath != null && wayPointIndex + 1 == depotPath.getNbWayPoints()) {
            atDepot = true;
        }
    }
}
