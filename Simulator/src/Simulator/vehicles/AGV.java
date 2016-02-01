package Simulator.vehicles;

import Simulator.Container;
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
import java.util.ArrayList;
import java.util.List;

public class AGV extends WorldObject
{
    private MotionPath motionPath;
    private MotionEvent motionEvent;
    private List<Vector3f> wayPointList = new ArrayList<>();
    private float duration;
    private float totalDistance;
    private float agvSpeed;
    private Vector3f vectorZero = new Vector3f(0,0,0);

    public Container container;

    public AGV(Node rootNode, AssetManager assetManager, Vector3f position, Spatial model)
    {
        super(rootNode, assetManager, position, model);
    }
    
    public void setPath(List<Vector3f> wayPoints, float distance)
    {
        if(wayPoints == null || distance == 0 || (motionEvent != null && motionEvent.getPlayState() == PlayState.Playing))
        {
            return;
        }
        if(container != null)
            agvSpeed = 200;
        else
            agvSpeed = 400;
        motionPath = new MotionPath();
        this.wayPointList = wayPoints;
        this.totalDistance = distance;
        this.duration = this.totalDistance/agvSpeed;
        this.motionPath.setPathSplineType(Spline.SplineType.Linear);
        if(wayPoints.size() > 0)
        {
            motionPath.addWayPoint(node.getWorldTranslation());
            for(Vector3f wayPoint : this.wayPointList)
                motionPath.addWayPoint(wayPoint);
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
            if (motionControl.getPath().getWayPoint(wayPointIndex).equals(new Vector3f(250.0f, 0.0f, -723.0f))) {
                this.node.rotate(0.0f, FastMath.DEG_TO_RAD * 90.0f, 0.0f);
            }
            if(this.container != null)
            {
                this.container.operationDone();
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
        this.container.operationDone();
    }
}