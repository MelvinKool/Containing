package Simulator.vehicles;

import Simulator.Container;
import Simulator.TrainParking;
import Simulator.WorldObject;
import com.jme3.asset.AssetManager;
import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.PlayState;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Spline;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;

public class AGV extends WorldObject
{
    private MotionPath motionPath;
    private MotionEvent motionEvent;
    public List<Vector3f> wayPointList = new ArrayList<>(); // TDOD: make privatew
    private float duration;
    private float totalDistance;
    private float agvSpeed;
    private Vector3f vectorZero = new Vector3f(0,0,0);
    private boolean busy;

    public Container container;
    private TrainParking trainParking;
    private SimpleEntry<Integer, Vector3f> trainParkingSpot = null;

    public AGV(Node rootNode, AssetManager assetManager, Vector3f position, Spatial model, TrainParking trainParking)
    {
        super(rootNode, assetManager, position, model);
        this.trainParking = trainParking;
        this.busy = false;
    }
    
    public void setPath(List<Vector3f> wayPoints, float distance)
    {
//        if(wayPoints == null || distance == 0 || (motionEvent != null && motionEvent.getPlayState() == PlayState.Playing))
//        {
//            return;
//        }
        if (this.trainParkingSpot != null) {
            this.trainParking.setSpot(this.trainParkingSpot.getKey(), true);
            this.trainParkingSpot = null;
        }
        if(container != null)
            agvSpeed = 20;
        else
            agvSpeed = 40;
        motionPath = new MotionPath();
        this.wayPointList = wayPoints;
        this.totalDistance = distance;
        this.duration = this.totalDistance/agvSpeed;
        this.motionPath.setPathSplineType(Spline.SplineType.Linear);
        if(wayPoints.size() > 0)
        {
            motionPath.addWayPoint(node.getWorldTranslation());
            for(Vector3f wayPoint : this.wayPointList) {
                
                // If end waypoint is train then queue in line 
                if (this.trainParking.getPosition().equals(wayPoint)) {
                    SimpleEntry<Integer, Vector3f> spot = this.trainParking.getFirstFreeSpot();
                    wayPoint = spot.getValue().clone();
                    this.trainParking.setSpot(spot.getKey(), false);
                    this.trainParkingSpot = spot;
                }
                motionPath.addWayPoint(wayPoint);
            }
                
            startMotionEvent();
        }
    }

    public void startMotionEvent()
    {
        motionEvent = new MotionEvent(this.node, this.motionPath);
        if(motionEvent.getPath().getNbWayPoints() > 1)
        {
            this.motionEvent.setLookAt(motionEvent.getPath().getWayPoint(motionEvent.getCurrentWayPoint()), vectorZero);
        }
        this.motionEvent.setDirectionType(MotionEvent.Direction.LookAt);
        this.motionEvent.setInitialDuration(this.duration);
        this.motionPath.addListener(this);
        motionEvent.play();
    }
	
    @Override
    public void onWayPointReach(MotionEvent motionControl, int wayPointIndex)
    {
        this.node.setLocalTranslation(motionPath.getWayPoint(wayPointIndex));
        this.node.lookAt(motionEvent.getPath().getWayPoint(wayPointIndex), vectorZero);
        this.motionEvent.setLookAt(motionEvent.getPath().getWayPoint(wayPointIndex), vectorZero);
        if(motionPath.getNbWayPoints() == wayPointIndex + 1)
        {
            if (this.trainParkingSpot != null && motionControl.getPath().getWayPoint(wayPointIndex).equals(this.trainParkingSpot.getValue())) {
                this.node.rotate(0.0f, FastMath.DEG_TO_RAD * 90.0f, 0.0f);
            }
            if(this.container != null)
            {
                this.container.operationDone();
                this.container = null;
            }
            this.motionEvent = null;
            this.motionPath = null;
        }            
    }
	
    public void attachContainer(Container container) 
    {
        Quaternion rot = container.node.getWorldRotation().clone();
        this.container = container;
        this.node.attachChild(container.node);
        this.container.node.setLocalTranslation(0, 1.2f, 0);
        this.container.node.rotate(rot);
        this.container.setVehicle(this);
        this.container.operationDone();
    }
    
    public boolean isBusy() 
    {
        return this.busy;
    }
    
    public void setBusy(boolean busy) 
    {
        this.busy = busy;
    }
}