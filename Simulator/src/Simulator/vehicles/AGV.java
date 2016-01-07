/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulator.vehicles;

import Simulator.Container;
import Simulator.WorldObject;
import com.jme3.asset.AssetManager;
import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.PlayState;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.math.FastMath;
import com.jme3.math.Spline;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author erwin
 */
public class AGV extends WorldObject {
    
    private MotionPath motionPath;
    private MotionEvent motionEvent;
    private List<Vector3f> allWayPoints = new ArrayList<>();
    private float duration;
    private float difX;
    private float difZ;
    private float totalDistance;
    private Vector3f lookAtPosition;
    private int agvSpeed = 250;
    private int currentLookPos = 0;
    
    public Container container;
    
    public AGV(Node rootNode, AssetManager assetManager, Vector3f position, Spatial model) {
        super(rootNode, assetManager, position, model);
    }
    
    //getDistance calculates to total distance of the motionPath. 
    //And will calculate the duration over all the waypoints
    public void getDistance(List<Vector3f> wayPoints)
    {
        for(int i = 0; i < wayPoints.size();i++)
        {
            difX = FastMath.abs(this.node.getLocalTranslation().x - wayPoints.get(i).x);
            difZ = FastMath.abs(this.node.getLocalTranslation().z - wayPoints.get(i).z);
            totalDistance += difZ += difX;
        }
        duration = totalDistance/agvSpeed;
        System.out.println("totaldistance "+totalDistance);
        System.out.println("Duration of event "+duration);
    }
    
    public void setWayPoints(List<Vector3f> wayPointVectors){
        for(int i = 0; i < wayPointVectors.size(); i++)
        allWayPoints.add(wayPointVectors.get(i));
        lookAtPosition = this.allWayPoints.get(0);
        setPath(allWayPoints);
        System.out.println("lookat swp " + lookAtPosition);
    }
    
    public void setPath(List<Vector3f> wayPoints) {
        if (this.motionPath == null) {
            this.motionPath = new MotionPath();
            this.motionPath.addWayPoint(this.node.getWorldTranslation());

            System.out.println("new motionpath");
        }
        
        for (Vector3f wayPoint : wayPoints) {
            this.motionPath.addWayPoint(wayPoint);
            System.out.println("new waypoint at: " + wayPoint.toString());
        }
        
        if (this.motionEvent == null) {
            this.motionEvent = new MotionEvent(this.node, this.motionPath);
            System.out.println("new motionevent");
        }
        
        if (this.motionEvent.getPlayState() != PlayState.Playing) {
            this.motionEvent.play();

            System.out.println("start play");
        }
        
        //getDistance calculates to total distance of the motionPath. 
        //And will calculate the duration over all the waypoints
        getDistance(wayPoints);
        
        this.motionPath.setCurveTension(0.0f);
        this.motionPath.setPathSplineType(Spline.SplineType.Linear);
        this.motionEvent.setLookAt(lookAtPosition, Vector3f.ZERO);
        System.out.println("setPathLookat " + lookAtPosition);
        this.motionEvent.setDirectionType(MotionEvent.Direction.LookAt);
        this.motionEvent.setInitialDuration(duration);
        this.motionPath.addListener(this);
    }
    
    @Override
    public void onWayPointReach(MotionEvent motionControl, int wayPointIndex) {
            //this.motionPath.removeWayPoint(wayPointIndex);
            if(currentLookPos < allWayPoints.size())
            {
                currentLookPos++;
                if(currentLookPos == allWayPoints.size())
                currentLookPos = 0;
            }
            
            lookAtPosition = allWayPoints.get(currentLookPos);
            System.out.println("updated lookat " + lookAtPosition);
            this.motionEvent.setLookAt(lookAtPosition, Vector3f.ZERO);
            
        if (this.motionPath.getNbWayPoints() == wayPointIndex + 1) {
            System.out.println("delete motion");
            this.motionPath = null;
            this.motionEvent = null; 
        }
    }
}