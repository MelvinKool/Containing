package org.nhl.containing.vehicles;

import com.jme3.asset.AssetManager;
import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import java.util.ArrayList;
import java.util.List;
import org.nhl.containing.Container;

public class Train extends Transporter {

    private AssetManager assetManager;
    private int wagonZAxis = -11;
    private List<Container> trainContainerList;
    private ArrayList<Node> trainWagons = new ArrayList();
    private MotionPath path;
    private MotionEvent motionControl;

    public Train(AssetManager assetManager, int id, List<Container> trainContainerList) {
        super(id);
        this.assetManager = assetManager;
        this.trainContainerList = trainContainerList;
        speed = 0.8f;
        initTrain();
        initMotionPaths();
        this.rotate(new Quaternion().fromAngleAxis(FastMath.PI * 3 / 2, new Vector3f(0, 1, 0)));
    }

    /**
     * Initialize a train.
     */
    private void initTrain() {
        // Load a model.
        Node train = (Node) assetManager.loadModel("Models/low/train/train.j3o");
        this.attachChild(train);

        //Load wagons.
        Node wagon = (Node) assetManager.loadModel("Models/low/train/wagon.j3o");

        for (int i = 0; i < trainContainerList.size(); i++) {
            Node nextWagon = (Node) wagon.clone();
            trainWagons.add(nextWagon);
            trainWagons.get(i).setLocalTranslation(0, 0, wagonZAxis);
            trainContainerList.get(i).setLocalTranslation(0, 1, 0);
            trainWagons.get(i).attachChild(trainContainerList.get(i));
            this.attachChild(trainWagons.get(i));
            wagonZAxis -= 15;
        }
        
        while(trainWagons.size() < 20){
            Node nextWagon = (Node) wagon.clone();
            nextWagon.setLocalTranslation(0, 0, wagonZAxis);
            this.attachChild(nextWagon);
            trainWagons.add(nextWagon);
            wagonZAxis -= 15;
        }
        
        Node reverseTrain = (Node) train.clone();
        reverseTrain.rotate(0, (float) Math.PI, 0);
        reverseTrain.setLocalTranslation(0, 0, wagonZAxis + 4);
        this.attachChild(reverseTrain);
        
    }

    /**
     * Initialize motionpath and motionevent
     */
    private void initMotionPaths() {
        path = new MotionPath();
        motionControl = new MotionEvent(this, path);
    }

    /**
     * Creates waypoints and lets the vehicle arrive at it's given location
     *
     * @param location not used
     */
    @Override
    public void arrive(int location) {
        motionControl.setSpeed(speed);
        path.clearWayPoints();
        path.addWayPoint(new Vector3f(250, 0, -180));
        path.addWayPoint(new Vector3f(-200, 0, -180));
        path.addListener(this);
        motionControl.play();
    }

    /**
     *
     */
    @Override
    public void depart() {
        motionControl.setSpeed(speed);
        path.clearWayPoints();
        path.addWayPoint(this.getWorldTranslation());
        path.addWayPoint(new Vector3f(-200, 0, -180));
        path.addWayPoint(new Vector3f(250, 0, -180));
        path.addListener(this);
        motionControl.play();
    }
    
    public void removeContainer(Container container){
        this.trainContainerList.add(container);
    }
    
    public void addContainer(Container container){
        this.trainContainerList.add(container);
    }

    /**
     * Debug method, displays object name, speed, amount of containers and it's
     * waypoints.
     *
     * @return information about this object
     */
    public String getDebugInfo() {
        String info = this.getClass().getSimpleName() + "\nSpeed: " + speed + "\nLocation: " + this.getLocalTranslation() + "\nCarrying: " + trainContainerList.size() + " containers.\n";
        for (int i = 0; i < path.getNbWayPoints(); i++) {
            info += "Waypoint " + (i + 1) + ": " + path.getWayPoint(i) + " ";
        }
        return info + "\n";
    }

    /**
     * Returns the waypoint coörds
     *
     * @return string containing the waypoints
     */
    public String getWaypoints() {
        String info = "\nTrain waypoints ";
        for (int j = 0; j < path.getNbWayPoints(); j++) {
            info += "Waypoint " + (j + 1) + ": " + path.getWayPoint(j) + " ";
        }
        return info + "\n";
    }

    public ArrayList<Node> getTrainWagons() {
        return trainWagons;
    }

    @Override
    public void onWayPointReach(MotionEvent motionControl, int wayPointIndex) {
        if (wayPointIndex + 1 == path.getNbWayPoints()) {
            setArrived(true);
        }

    }
}
