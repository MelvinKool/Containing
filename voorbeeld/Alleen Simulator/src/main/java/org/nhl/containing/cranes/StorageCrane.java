package org.nhl.containing.cranes;

import com.jme3.asset.AssetManager;
import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.util.List;
import org.nhl.containing.Container;
import org.nhl.containing.areas.StorageArea;
import org.nhl.containing.vehicles.Agv;

public class StorageCrane extends Crane {

    private AssetManager assetManager;
    private Agv agv;
    private Container container;
    private StorageArea storageArea;
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

    public StorageCrane(AssetManager assetManager, StorageArea storageArea) {
        this.assetManager = assetManager;
        this.storageArea = storageArea;
        this.name = "StorageCrane";
        initStorageCrane();
    }

    /**
     * Initialize a storage crane.
     */
    private void initStorageCrane() {

        // Load a model.
        Spatial storageCrane = assetManager.loadModel("Models/high/crane/storagecrane/crane.j3o");
        this.attachChild(storageCrane);
    }

    /**
     * Let the crane move to the container's position. And Put the container on
     * the Agv.
     *
     * @param container Request a container.
     * @param agv Agv that needs the container.
     */
    public void storageToAgv(Container container, Agv agv) {
        this.container = container;
        this.agv = agv;
        this.direction = CraneDirection.STORAGETOAGV;
        initWaypoints();
        moveToContainer();
    }

    /**
     * Let the crane move to the container's position. And place the container
     * on the given location.
     *
     * @param container Request a container
     * @param containerLocation Location to place the container.
     */
    public void agvToStorage(Container container) {
        this.container = container;
        this.direction = CraneDirection.AGVTOSTORAGE;
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
        container.rotate(container.getWorldRotation());
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
     * Move back to the Agv.
     */
    private void createPathtoAgv() {
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
     * Put the container on the Agv.
     */
    private void putContainerOnAgv() {
        container.rotate(container.getWorldRotation());
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
     * Initialize the waypoints depending on the requested direction.
     */
    private void initWaypoints() {
        switch (direction) {
            case AGVTOSTORAGE:

                Vector3f spot = storageArea.getPosition(container);

                cranePath.addWayPoint(getLocalTranslation());
                cranePath.addWayPoint(new Vector3f(getLocalTranslation().x, 0, (container.getWorldTranslation().z - getWorldTranslation().z) + getLocalTranslation().z + 0.01f));

                containerPathUp.addWayPoint(new Vector3f(container.getWorldTranslation().x - this.getWorldTranslation().x, container.getWorldTranslation().y, 0));
                containerPathUp.addWayPoint(new Vector3f(container.getWorldTranslation().x - this.getWorldTranslation().x, 24, 0));
                containerPathUp.addWayPoint(new Vector3f((storageArea.getWorldTranslation().x - getWorldTranslation().x) + spot.x + 0.01f, 24, 0));

                newCranePath.addWayPoint(new Vector3f(getLocalTranslation().x, 0, (container.getWorldTranslation().z - getWorldTranslation().z) + getLocalTranslation().z));
                newCranePath.addWayPoint(new Vector3f(getLocalTranslation().x, 0, getLocalTranslation().z + spot.z + 0.01f));

                containerPathDown.addWayPoint(new Vector3f((storageArea.getWorldTranslation().x - getWorldTranslation().x) + spot.x + 0.01f, 24, 0));
                containerPathDown.addWayPoint(new Vector3f((storageArea.getWorldTranslation().x - getWorldTranslation().x) + spot.x + 0.01f, spot.y, 0));

                cranePathBack.addWayPoint(new Vector3f(getLocalTranslation().x, 0, getLocalTranslation().z + spot.z + 0.009f));
                cranePathBack.addWayPoint(getLocalTranslation());

                break;
            case STORAGETOAGV:
                cranePath.addWayPoint(getLocalTranslation());
                cranePath.addWayPoint(new Vector3f(getLocalTranslation().x, 0, (container.getWorldTranslation().z - getWorldTranslation().z) + getLocalTranslation().z + 0.01f));

                containerPathUp.addWayPoint(new Vector3f(container.getWorldTranslation().x - this.getWorldTranslation().x, container.getWorldTranslation().y, 0));
                containerPathUp.addWayPoint(new Vector3f(container.getWorldTranslation().x - this.getWorldTranslation().x, 24, 0));
                containerPathUp.addWayPoint(new Vector3f((agv.getWorldTranslation().x - getWorldTranslation().x) + 0.01f, 24, 0));

                newCranePath.addWayPoint(new Vector3f(getLocalTranslation().x, 0, (container.getWorldTranslation().z - getWorldTranslation().z) + getLocalTranslation().z));
                newCranePath.addWayPoint(new Vector3f(getLocalTranslation().x, 0, (agv.getWorldTranslation().z - getWorldTranslation().z) + getLocalTranslation().z + 0.01f));

                containerPathDown.addWayPoint(new Vector3f((agv.getWorldTranslation().x - getWorldTranslation().x), 24, 0));
                containerPathDown.addWayPoint(new Vector3f((agv.getWorldTranslation().x - getWorldTranslation().x), 1, 0));

                cranePathBack.addWayPoint(new Vector3f(getLocalTranslation().x, 0, (agv.getWorldTranslation().z - getWorldTranslation().z) + getLocalTranslation().z + 0.009f));
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
            this.attachChild(container);
        }

        if (containerPathUpCounter == wayPointIndex + 1) {
            createPathtoAgv();
            wayPointIndex = 0;
            containerPathUpCounter = 0;
        }

        if (newCranePathCounter == wayPointIndex + 1) {
            putContainerOnAgv();
            wayPointIndex = 0;
            newCranePathCounter = 0;
        }

        if (containerPathDownCounter == wayPointIndex + 1) {
            switch (direction) {
                case STORAGETOAGV:
                    detachChild(container);
                    agv.attachChild(container);
                    container.setLocalTranslation(0, 1, 0);
                    storageArea.removeContainer(container);
                    for (List<Container> element : storageArea.getFilledPositions()) {
                        element.remove(container);
                    }

                    agv.addContainer(container);
                    break;
                case AGVTOSTORAGE:
                    Vector3f position = container.getWorldTranslation();
                    detachChild(container);
                    storageArea.attachChild(container);
                    container.setLocalTranslation(position.subtract(storageArea.getWorldTranslation()));
                    //agv.removeContainer();
                    storageArea.addContainer(container);
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

        AGVTOSTORAGE, STORAGETOAGV
    }
}
