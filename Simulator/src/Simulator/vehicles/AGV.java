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
    private float totalDistance;
    private Vector3f lookAtPosition;
    private int agvSpeed = 400;
    private int currentLookPos = 0;
    
    public Container container;
    
    public AGV(Node rootNode, AssetManager assetManager, Vector3f position, Spatial model) {
        super(rootNode, assetManager, position, model);
    }

//    public void setWayPoints(List<Vector3f> wayPointVectors, float totalDistance){
//        for(int i = 0; i < wayPointVectors.size(); i++)
//        allWayPoints.add(wayPointVectors.get(i));
//        lookAtPosition = this.allWayPoints.get(0);
//        //setPath(allWayPoints);
//        //this.totalDistance = totalDistance;
//    }
    
    public void setPath(List<Vector3f> wayPoints, float totalDistance) {
        if (this.motionPath == null) {
            for(int i = 0; i < wayPoints.size(); i++)
                allWayPoints.add(wayPoints.get(i));
            this.motionPath = new MotionPath();
            this.motionPath.addWayPoint(this.node.getWorldTranslation());
            this.totalDistance = totalDistance;
            System.out.println("new motionpath");
            
        }
        
        for (Vector3f wayPoint : wayPoints) {
            this.motionPath.addWayPoint(wayPoint);
            //System.out.println("new waypoint at: " + wayPoint.toString());
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
//        getDistance(wayPoints);
        duration = totalDistance/agvSpeed;
        
        this.motionPath.setCurveTension(0.0f);
        this.motionPath.setPathSplineType(Spline.SplineType.Linear);
        this.motionEvent.setLookAt(lookAtPosition, Vector3f.ZERO);
        this.motionEvent.setDirectionType(MotionEvent.Direction.LookAt);
        this.motionEvent.setInitialDuration(duration);
        this.motionPath.addListener(this);
    }
    
    @Override
    public void onWayPointReach(MotionEvent motionControl, int wayPointIndex) {
        if(currentLookPos < allWayPoints.size()-1)
        {
            currentLookPos++;
            if(currentLookPos == allWayPoints.size()-1)
            {
                currentLookPos = 0;
            }
        }
        if(currentLookPos > allWayPoints.size())
            System.out.println("OUTOFBOUND ERROR");
            
        else
        {
            lookAtPosition = allWayPoints.get(currentLookPos);
            System.out.println("updated lookat " + lookAtPosition);this.motionEvent.setLookAt(lookAtPosition, Vector3f.ZERO);
        }
        this.motionEvent.setLookAt(lookAtPosition, Vector3f.ZERO);
        System.out.println("Printing waypointindex...");
            System.out.println(wayPointIndex + 1);
            System.out.println(this.motionPath.getNbWayPoints());
        if (this.motionPath.getNbWayPoints() == wayPointIndex + 1) {
            System.out.println("delete motion");
            allWayPoints.clear();
            this.motionPath = null;
            this.motionEvent = null; 
        }
    }
}