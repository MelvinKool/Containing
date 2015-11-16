package org.nhl.containing.cranes;

import com.jme3.asset.AssetManager;
import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import org.nhl.containing.Container;
import org.nhl.containing.areas.BoatArea;
import org.nhl.containing.vehicles.Agv;
import org.nhl.containing.vehicles.Inlandship;
import org.nhl.containing.vehicles.Seaship;

public class DockingCrane extends Crane {

    private AssetManager assetManager;
    private Agv agv;
    private Container container;
    private Node boat;
    private MotionPath containerPathUp = new MotionPath();
    private MotionPath containerPathDown = new MotionPath();
    private MotionPath cranePath = new MotionPath();
    private MotionPath newCranePath = new MotionPath();
    private MotionPath cranePathBack = new MotionPath();
    private int cranePathCounter;
    private int newCranePathCounter;
    private int containerPathUpCounter;
    private int containerPathDownCounter;
    private int cranePathBackCounter;
    private Vector3f nextContainerSpot;
    private BoatArea.AreaType type;
    private CraneDirection direction;

    public DockingCrane(AssetManager assetManager, BoatArea.AreaType type) {
        this.assetManager = assetManager;
        this.type = type;
        initDockingCrane();
    }

    /**
     * Initialize a docking crane.
     */
    private void initDockingCrane() {

        // Load a model.
        Spatial moveableCrane = assetManager.loadModel("Models/low/crane/dockingcrane/crane.j3o");
        this.attachChild(moveableCrane);
    }

    /**
     * Let the crane move to the container's position. And put the container on
     * the Agv.
     *
     * @param container Request a container.
     * @param agv Agv that needs the container.
     */
    public void boatToAgv(Container container, Agv agv) {
        this.container = container;
        this.agv = agv;
        this.direction = CraneDirection.BOATTOAGV;
        initWaypoints();
        moveToContainer();
    }

    /**
     * Let the crane move to the container's position. And put the container on
     * the boat.
     *
     * @param container Request a container.
     * @param boat Boat that needs the container.
     */
    public void agvToBoat(Container container, Node boat) {
        this.container = container;
        this.boat = boat;
        this.direction = CraneDirection.AGVTOBOAT;
        switch (type) {
            case SEASHIP:
                nextContainerSpot = ((Seaship) boat).getNextSpot();
                break;
            case INLANDSHIP:
                nextContainerSpot = ((Inlandship) boat).getNextSpot();
                break;
        }
        initWaypoints();
        moveToContainer();
    }

    /**
     * Move the crane to the container.
     */
    private void moveToContainer() {
        cranePathCounter = cranePath.getNbWayPoints();
        cranePath.setCurveTension(0.0f);
//        cranePath.enableDebugShape(assetManager, this);
        cranePath.addListener(this);

        MotionEvent motionControl = new MotionEvent(this, cranePath);
        motionControl.setDirectionType(MotionEvent.Direction.None);
        motionControl.setSpeed(speed);
        motionControl.play();
        motionControl.dispose();
    }

    /**
     * Put the container on the crane.
     */
    private void moveContainer() {
        containerPathUpCounter = containerPathUp.getNbWayPoints();
        containerPathUp.setCurveTension(0.0f);
//        containerPathUp.enableDebugShape(assetManager, this);
        containerPathUp.addListener(this);

        MotionEvent motionControl = new MotionEvent(container, containerPathUp);
        motionControl.setDirectionType(MotionEvent.Direction.None);
        motionControl.setSpeed(speed);
        motionControl.play();
        motionControl.dispose();
    }

    /**
     * Move back to the vehicle.
     */
    private void createPathtoVehicle() {
        newCranePathCounter = newCranePath.getNbWayPoints();
        newCranePath.setCurveTension(0.0f);
//        newCranePath.enableDebugShape(assetManager, this);
        newCranePath.addListener(this);
        MotionEvent motionControl = new MotionEvent(this, newCranePath);
        motionControl.setDirectionType(MotionEvent.Direction.None);
        motionControl.setSpeed(speed);
        motionControl.play();
        motionControl.dispose();
    }

    /**
     * Put the container on the Vehicle.
     */
    private void putContainerOnVehicle() {
        containerPathDownCounter = containerPathDown.getNbWayPoints();
        containerPathDown.setCurveTension(0.0f);
//        containerPathDown.enableDebugShape(assetManager, this);
        containerPathDown.addListener(this);

        MotionEvent motionControl = new MotionEvent(container, containerPathDown);
        motionControl.setDirectionType(MotionEvent.Direction.None);
        motionControl.setSpeed(speed);
        motionControl.play();
        motionControl.dispose();
    }

    /**
     * Initialize waypoints depending on the type of platform.
     */
    private void initWaypoints() {
        switch (type) {
            case SEASHIP:
                seashipWaypoints();
                break;

            case INLANDSHIP:
                inlandshipWaypoints();
                break;
        }
    }

    /**
     * Initialize waypoints depending on the direction.
     */
    private void seashipWaypoints() {
        switch (direction) {
            case BOATTOAGV:
                seashipToAgvWaypoints();
                break;
            case AGVTOBOAT:
                agvToSeashipWaypoints();
                break;
        }
    }

    /**
     * Initialize waypoints depending on the direction.
     */
    private void inlandshipWaypoints() {
        switch (direction) {
            case BOATTOAGV:
                inlandshipToAgvWaypoints();
                break;
            case AGVTOBOAT:
                agvToInlandshipWaypoints();
                break;
        }
    }

    /**
     * Initialize waypoints.
     */
    private void seashipToAgvWaypoints() {
        cranePath.addWayPoint(getLocalTranslation());
        cranePath.addWayPoint(new Vector3f(getLocalTranslation().x, 0, (container.getWorldTranslation().z - getWorldTranslation().z) + getLocalTranslation().z + 0.01f));

        containerPathUp.addWayPoint(new Vector3f(container.getWorldTranslation().x - getWorldTranslation().x, container.getWorldTranslation().y, 0));
        containerPathUp.addWayPoint(new Vector3f(container.getWorldTranslation().x - getWorldTranslation().x, 22, 0));
        containerPathUp.addWayPoint(new Vector3f((agv.getWorldTranslation().x - getWorldTranslation().x) + 0.01f, 22, 0));

        newCranePath.addWayPoint(new Vector3f(getLocalTranslation().x, 0, (container.getWorldTranslation().z - getWorldTranslation().z) + getLocalTranslation().z));
        newCranePath.addWayPoint(new Vector3f(getLocalTranslation().x, 0, (agv.getWorldTranslation().z - getWorldTranslation().z) + getLocalTranslation().z + 0.01f)) ;

        containerPathDown.addWayPoint(new Vector3f((agv.getWorldTranslation().x - getWorldTranslation().x), 22, 0));
        containerPathDown.addWayPoint(new Vector3f((agv.getWorldTranslation().x - getWorldTranslation().x), 1, 0));

        cranePathBack.addWayPoint(new Vector3f(getLocalTranslation().x, 0, (agv.getWorldTranslation().z - getWorldTranslation().z) + getLocalTranslation().z + 0.009f));
        cranePathBack.addWayPoint(getLocalTranslation());
    }

    /**
     * Initialize waypoints.
     */
    private void agvToSeashipWaypoints() {
        cranePath.addWayPoint(getLocalTranslation());
        cranePath.addWayPoint(new Vector3f(getLocalTranslation().x, 0, (container.getWorldTranslation().z - getWorldTranslation().z) + getLocalTranslation().z + 0.01f));

        containerPathUp.addWayPoint(new Vector3f(container.getWorldTranslation().x - getWorldTranslation().x, 1, 0));
        containerPathUp.addWayPoint(new Vector3f(container.getWorldTranslation().x - getWorldTranslation().x, 22, 0));
        containerPathUp.addWayPoint(new Vector3f((boat.getWorldTranslation().x - getWorldTranslation().x) + nextContainerSpot.x + 0.01f, 22, 0));

        newCranePath.addWayPoint(new Vector3f(getLocalTranslation().x, 0, (container.getWorldTranslation().z - getWorldTranslation().z) + getLocalTranslation().z));
        newCranePath.addWayPoint(new Vector3f(getLocalTranslation().x, 0, (boat.getWorldTranslation().z - getWorldTranslation().z) + getLocalTranslation().z + nextContainerSpot.z + 0.01f));

        containerPathDown.addWayPoint(new Vector3f((boat.getWorldTranslation().x - getWorldTranslation().x) + nextContainerSpot.x, 22, 0));
        containerPathDown.addWayPoint(new Vector3f((boat.getWorldTranslation().x - getWorldTranslation().x) + nextContainerSpot.x, nextContainerSpot.y, 0));

        cranePathBack.addWayPoint(new Vector3f(getLocalTranslation().x, 0, (boat.getWorldTranslation().z - getWorldTranslation().z) + getLocalTranslation().z + nextContainerSpot.z + 0.01f));
        cranePathBack.addWayPoint(getLocalTranslation());

    }

    /**
     * Initialize waypoints.
     */
    private void inlandshipToAgvWaypoints() {
        cranePath.addWayPoint(getLocalTranslation());
        cranePath.addWayPoint(new Vector3f((container.getWorldTranslation().x - getWorldTranslation().x) + getLocalTranslation().x + 0.01f, 0, getLocalTranslation().z));

        containerPathUp.addWayPoint(new Vector3f((getWorldTranslation().z - container.getWorldTranslation().z), container.getWorldTranslation().y, 0));
        containerPathUp.addWayPoint(new Vector3f((getWorldTranslation().z - container.getWorldTranslation().z), 22, 0));
        containerPathUp.addWayPoint(new Vector3f((getWorldTranslation().z - agv.getWorldTranslation().z) + 0.01f, 22, 0));

        newCranePath.addWayPoint(new Vector3f((container.getWorldTranslation().x - getWorldTranslation().x) + getLocalTranslation().x, 0, getLocalTranslation().z));
        newCranePath.addWayPoint(new Vector3f((agv.getWorldTranslation().x - getWorldTranslation().x) + getLocalTranslation().x, 0, getLocalTranslation().z));

        containerPathDown.addWayPoint(new Vector3f((getWorldTranslation().z - agv.getWorldTranslation().z), 22, 0));
        containerPathDown.addWayPoint(new Vector3f((getWorldTranslation().z - agv.getWorldTranslation().z) , 1, 0));

        cranePathBack.addWayPoint(new Vector3f((agv.getWorldTranslation().x - getWorldTranslation().x) + getLocalTranslation().x + 0.009f, 0, getLocalTranslation().z));
        cranePathBack.addWayPoint(getLocalTranslation());
    }

    /**
     * Initialize waypoints.
     */
    private void agvToInlandshipWaypoints() {
        cranePath.addWayPoint(getLocalTranslation());
        cranePath.addWayPoint(new Vector3f((container.getWorldTranslation().x - getWorldTranslation().x) + getLocalTranslation().x + 0.01f, 0, getLocalTranslation().z));

        containerPathUp.addWayPoint(new Vector3f((getWorldTranslation().z - container.getWorldTranslation().z), 1, 0));
        containerPathUp.addWayPoint(new Vector3f((getWorldTranslation().z - container.getWorldTranslation().z), 22, 0));
        containerPathUp.addWayPoint(new Vector3f((getWorldTranslation().z - boat.getWorldTranslation().z) - nextContainerSpot.x + 0.01f, 22, 0));

        newCranePath.addWayPoint(new Vector3f((container.getWorldTranslation().x - getWorldTranslation().x) + getLocalTranslation().x, 0, getLocalTranslation().z));
        newCranePath.addWayPoint(new Vector3f((boat.getWorldTranslation().x - getWorldTranslation().x) + getLocalTranslation().x - nextContainerSpot.z + 0.01f, 0, getLocalTranslation().z));

        containerPathDown.addWayPoint(new Vector3f((getWorldTranslation().z - boat.getWorldTranslation().z) - nextContainerSpot.x, 22, 0));
        containerPathDown.addWayPoint(new Vector3f((getWorldTranslation().z - boat.getWorldTranslation().z) - nextContainerSpot.x, nextContainerSpot.y, 0));

        cranePathBack.addWayPoint(new Vector3f((boat.getWorldTranslation().x - getWorldTranslation().x) + getLocalTranslation().x - nextContainerSpot.z + 0.009f, 0, getLocalTranslation().z));
        cranePathBack.addWayPoint(getLocalTranslation());
    }

    /**
     * Move back to the start location.
     */
    private void returnToStart() {
        cranePathBackCounter = cranePathBack.getNbWayPoints();
        cranePathBack.setCurveTension(0.0f);
//        cranePathBack.enableDebugShape(assetManager, this);
        cranePathBack.addListener(this);
        MotionEvent motionControl = new MotionEvent(this, cranePathBack);
        motionControl.setDirectionType(MotionEvent.Direction.None);
        motionControl.play();
        motionControl.dispose();
    }

    /**
     * Completely reset the crane.
     */
    private void resetCrane() {
        containerPathUp.clearWayPoints();
        containerPathDown.clearWayPoints();
        cranePath.clearWayPoints();
        newCranePath.clearWayPoints();
        cranePathBack.clearWayPoints();
        cranePathCounter = 0;
        newCranePathCounter = 0;
        containerPathUpCounter = 0;
        containerPathDownCounter = 0;
        cranePathBackCounter = 0;
        containerPathUp = new MotionPath();
        containerPathDown = new MotionPath();
        cranePath = new MotionPath();
        newCranePath = new MotionPath();
        cranePathBack = new MotionPath();
    }

    /**
     * Method gets automatically called everytime a waypoint is reached.
     *
     * @param motionControl motioncontrol of the path.
     * @param wayPointIndex Index of the current waypoint.
     */
    @Override
    public void onWayPointReach(MotionEvent motionControl, int wayPointIndex) {
        if (cranePathCounter == wayPointIndex + 1) {
            cranePath.clearWayPoints();
            moveContainer();
            wayPointIndex = 0;
            cranePathCounter = 0;
            this.attachChild(container);
        }

        if (containerPathUpCounter == wayPointIndex + 1) {
            createPathtoVehicle();
            wayPointIndex = 0;
            containerPathUpCounter = 0;
        }

        if (newCranePathCounter == wayPointIndex + 1) {
            putContainerOnVehicle();
            wayPointIndex = 0;
            newCranePathCounter = 0;
        }

        if (containerPathDownCounter == wayPointIndex + 1) {
            switch (type) {
                case SEASHIP:
                    switch (direction) {
                        case BOATTOAGV:
                            detachChild(container);
                            agv.attachChild(container);
                            container.setLocalTranslation(0, 1, 0);
                            //((Seaship)boat).removeContainer(container);
                            agv.addContainer(container);
                            break;
                        case AGVTOBOAT:
                            detachChild(container);
                            boat.attachChild(container);
                            container.setLocalTranslation(nextContainerSpot);
                            //((Seaship)boat).addContainer(container);
                            agv.removeContainer();
                            break;
                    }
                    break;

                case INLANDSHIP:
                    switch (direction) {
                        case BOATTOAGV:
                            detachChild(container);
                            agv.attachChild(container);
                            container.setLocalTranslation(0, 1, 0);
                            //((Inlandship)boat).removeContainer(container);
                            agv.addContainer(container);
                            break;
                        case AGVTOBOAT:
                            detachChild(container);
                            boat.attachChild(container);
                            container.setLocalTranslation(nextContainerSpot);
                            //((Inlandship)boat).addContainer(container);
                            agv.removeContainer();
                            break;
                    }
                    break;
            }
            containerPathDownCounter = 0;
            wayPointIndex = 0;
            returnToStart();

        }

        if (cranePathBackCounter == wayPointIndex + 1) {
            cranePathBackCounter = 0;
            wayPointIndex = 0;
            resetCrane();
            setArrived(true);

        }
    }

    private enum CraneDirection {

        AGVTOBOAT, BOATTOAGV
    }
}
