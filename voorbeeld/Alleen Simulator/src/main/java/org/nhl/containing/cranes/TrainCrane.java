package org.nhl.containing.cranes;

import com.jme3.asset.AssetManager;
import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import org.nhl.containing.Container;
import org.nhl.containing.vehicles.Agv;
import org.nhl.containing.vehicles.Train;

public class TrainCrane extends Crane {

    private static final Quaternion YAW090 = new Quaternion().fromAngleAxis(FastMath.PI / 2, new Vector3f(0, 1, 0));
    private AssetManager assetManager;
    private Agv agv;
    private Node wagon;
    private Train train;
    private Spatial container;
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
    private CraneDirection direction;

    public TrainCrane(AssetManager assetManager) {
        this.assetManager = assetManager;
        this.name = "TrainCrane";
        initTrainCrane();
    }

    /**
     * Initialize a train crane.
     */
    private void initTrainCrane() {

        // Load a model.
        Spatial trainCrane = assetManager.loadModel("Models/high/crane/traincrane/crane.j3o");
        trainCrane.setLocalRotation(YAW090);
        this.attachChild(trainCrane);
    }

    /**
     * Let the crane move to the container's position. And Put the container on
     * the Agv.
     *
     * @param container Request a container.
     * @param agv Agv that needs the container.
     */
    public void trainToAgv(Spatial container, Agv agv , Train train) {
        this.container = container;
        this.agv = agv;
        this.train = train;
        this.direction = CraneDirection.TRAINTOAGV;
        initWaypoints();
        moveToContainer();
    }

    /**
     * Let the crane move to the container's posisition. And put the container
     * on the train.
     *
     * @param container Request a container.
     * @param wagon Wagon that needs the container.
     */
    public void agvToTrain(Spatial container, Node wagon, Train train) {
        this.container = container;
        this.wagon = wagon;
        this.train = train;
        this.direction = CraneDirection.AGVTOTRAIN;
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
        attachChild(container);
        container.rotate(0, (float) Math.PI / 2, 0);

        containerPathUpCounter = containerPathUp.getNbWayPoints();
        containerPathUp.setCurveTension(0.0f);
//      containerPathUp.enableDebugShape(assetManager, this);
        containerPathUp.addListener(this);

        MotionEvent motionControl = new MotionEvent(getChild(1), containerPathUp);
        motionControl.setDirectionType(MotionEvent.Direction.None);
        motionControl.play();
    }

    /**
     * Move back to the vehicle.
     */
    private void createPathToVehicle() {
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
     * Put the container on the vehicle.
     */
    private void putContainerOnVehicle() {
        containerPathDownCounter = containerPathDown.getNbWayPoints();
        containerPathDown.setCurveTension(0.0f);
//        containerPathDown.enableDebugShape(assetManager, this);
        containerPathDown.addListener(this);
        MotionEvent motionControl = new MotionEvent(container, containerPathDown);
        motionControl.setDirectionType(MotionEvent.Direction.None);
        motionControl.play();
        motionControl.dispose();
    }

    /**
     * Initialize the waypoints depending on the requested direction.
     */
    private void initWaypoints() {
        switch (direction) {
            case AGVTOTRAIN:
                cranePath.addWayPoint(getLocalTranslation());
                cranePath.addWayPoint(new Vector3f((container.getWorldTranslation().x - getWorldTranslation().x) + getLocalTranslation().x + 0.01f, 0, 0));

                containerPathUp.addWayPoint(new Vector3f(0, 1, 6));
                containerPathUp.addWayPoint(new Vector3f(0, 5, 6));
                containerPathUp.addWayPoint(new Vector3f(0, 5, 0));

                newCranePath.addWayPoint(new Vector3f((container.getWorldTranslation().x - getWorldTranslation().x) + getLocalTranslation().x, 0, getLocalTranslation().z));
                newCranePath.addWayPoint(new Vector3f((wagon.getWorldTranslation().x - getWorldTranslation().x) + getLocalTranslation().x + 0.01f, 0, getLocalTranslation().z));

                containerPathDown.addWayPoint(new Vector3f(0, 5, 0));
                containerPathDown.addWayPoint(new Vector3f(0, 1, 0));

                cranePathBack.addWayPoint(new Vector3f((wagon.getWorldTranslation().x - getWorldTranslation().x) + getLocalTranslation().x + 0.01f, 0, getLocalTranslation().z));
                cranePathBack.addWayPoint(getLocalTranslation());
                break;

            case TRAINTOAGV:
                cranePath.addWayPoint(getLocalTranslation());
                cranePath.addWayPoint(new Vector3f((container.getWorldTranslation().x - getWorldTranslation().x) + getLocalTranslation().x + 0.01f, 0, 0));

                containerPathUp.addWayPoint(new Vector3f(0, 1, 0));
                containerPathUp.addWayPoint(new Vector3f(0, 5, 0));
                containerPathUp.addWayPoint(new Vector3f(0, 5, 6));

                newCranePath.addWayPoint(new Vector3f((container.getWorldTranslation().x - getWorldTranslation().x) + getLocalTranslation().x, 0, getLocalTranslation().z));
                newCranePath.addWayPoint(new Vector3f((agv.getWorldTranslation().x - getWorldTranslation().x) + getLocalTranslation().x + 0.01f, 0, getLocalTranslation().z));

                containerPathDown.addWayPoint(new Vector3f(0, 5, 6));
                containerPathDown.addWayPoint(new Vector3f(0, 1, 6));

                cranePathBack.addWayPoint(new Vector3f((agv.getWorldTranslation().x - getWorldTranslation().x) + getLocalTranslation().x + 0.01f, 0, getLocalTranslation().z));
                cranePathBack.addWayPoint(getLocalTranslation());
                break;
        }
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
        }

        if (containerPathUpCounter == wayPointIndex + 1) {
            createPathToVehicle();
            wayPointIndex = 0;
            containerPathUpCounter = 0;
        }

        if (newCranePathCounter == wayPointIndex + 1) {
            putContainerOnVehicle();
            wayPointIndex = 0;
            newCranePathCounter = 0;
        }

        if (containerPathDownCounter == wayPointIndex + 1) {
            switch (direction) {
                case TRAINTOAGV:
                    detachChild(container);
                    agv.attachChild(container);
                    container.setLocalTranslation(0, 1, 0);
                    //train.removeContainer((Container) container);
                    agv.addContainer((Container) container);
                    
                    break;
                case AGVTOTRAIN:
                    detachChild(container);
                    wagon.attachChild(container);
                    container.setLocalTranslation(0, 1, 0);
                    agv.removeContainer();
                    //train.addContainer((Container) container);
                    break;
            }

            container.rotate(0, (float) Math.PI / 2, 0);

            wayPointIndex = 0;
            containerPathDownCounter = 0;
            returnToStart();

            container.setLocalTranslation(0, 1, 0);

        }

        if (cranePathBackCounter == wayPointIndex + 1) {
            cranePathBackCounter = 0;
            wayPointIndex = 0;
            resetCrane();
            setArrived(true);
        }
    }

    private enum CraneDirection {

        AGVTOTRAIN, TRAINTOAGV
    }
}
