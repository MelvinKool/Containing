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
import com.jme3.math.Quaternion;
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
    private float agvSpeed = 4.0f;
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
        this.totalDistance = motionPath.getLength(); // TODO: pretty implementation
        duration = motionPath.getLength() / agvSpeed;
        
        System.out.println("path duration:" + duration);
         
        this.motionPath.setCurveTension(0.0f);
        this.motionPath.setPathSplineType(Spline.SplineType.Linear);
        this.motionEvent.setLookAt(lookAtPosition, Vector3f.UNIT_Y);
        this.motionEvent.setDirectionType(MotionEvent.Direction.LookAt);
        this.motionEvent.setInitialDuration(duration);
        this.motionPath.addListener(this);
    }
     
    @Override
    public void onWayPointReach(MotionEvent motionControl, int wayPointIndex) {
        if (wayPointIndex < allWayPoints.size()) {
            this.motionEvent.setLookAt(allWayPoints.get(wayPointIndex), Vector3f.UNIT_Y);
        }
        if(currentLookPos < allWayPoints.size()-1)
        {
            currentLookPos++;
            if(currentLookPos == allWayPoints.size()-1)
            {
                currentLookPos = 0;
            }
        }
        //this.motionEvent.setLookAt(lookAtPosition, Vector3f.UNIT_Y);
        if (this.motionPath.getNbWayPoints() == wayPointIndex + 1) {
            System.out.println("delete motion");
            allWayPoints.clear();
            this.motionPath = null;
            this.motionEvent = null; 
            this.container.operationDone();
        }
    }
    
    public void attachContainer(Container container) {
        Vector3f pos = container.node.getWorldTranslation();
        Quaternion rot = container.node.getWorldRotation();
        this.node.attachChild(container.node);
        container.node.setLocalTranslation(this.node.worldToLocal(pos, null));
        
        this.node.attachChild(container.node);
        this.container = container;
        this.container.setVehicle(this);
    }
    
//    private MotionPath motionPath;
//    private MotionEvent motionEvent;
//    List<Vector3f> wayPoints;
//    List<Float> durations;
//    //private float duration;
//    public Container container;
//    
//    public AGV(Node rootNode, AssetManager assetManager, Vector3f position, Spatial model) {
//        super(rootNode, assetManager, position, model);
//    }
//    
//    public void setPath(List<Vector3f> wayPoints, List<Float> durations) {
//        if(wayPoints == null || durations == null || motionEvent.getPlayState() == PlayState.Playing)
//            return;
//        //set motionpath
//        motionPath = new MotionPath();
//        this.wayPoints = wayPoints;
////        motionPath.addWayPoint(node.getWorldTranslation());
////        for(Vector3f temp: wayPoints)
////           motionPath.addWayPoint(temp);
//        this.motionPath.setPathSplineType(Spline.SplineType.Linear);
//        if(wayPoints.size() > 0)
//            addNewWayPointToMPath(this.wayPoints.get(0));
//        
//    }
//    
//    private void startMotionEvent(float duration){
//        motionEvent = new MotionEvent(node,motionPath);
//        if(motionEvent == null)
//            return;
//        if(motionEvent.getPath().getNbWayPoints() > 1)
//            this.motionEvent.setLookAt(motionEvent.getPath().getWayPoint(1), Vector3f.ZERO);
//        this.motionEvent.setDirectionType(MotionEvent.Direction.LookAt);
//        this.motionEvent.setInitialDuration(duration);
//        this.motionPath.addListener(this);
//        motionEvent.play();
//    }
//    
//    private void addNewWayPointToMPath(Vector3f goalPath){
//        motionPath.addWayPoint(node.getWorldTranslation());
//        motionPath.addWayPoint(goalPath);
//        //float duration = calculateDuration(node.getWorldTranslation(), goalPath);
//        startMotionEvent(durations.get(0));
//    }
//    
//    
////    private float calculateDuration(Vector3f pos1, Vector3f pos2)
////    {
////        float distSideAC = Math.abs(Math.abs(pos2.x) - Math.abs(pos1.x));
////        float distSideBC = Math.abs(Math.abs(pos2.z) - Math.abs(pos1.z));
////        float distanceP1ToP2 = (float)Math.sqrt(Math.pow(distSideAC,2) + Math.pow(distSideBC,2));
////        System.out.println(distanceP1ToP2);
////        float duration = distanceP1ToP2/agvSpeed;
////        
////        return duration;
////    }
//    
//    @Override
//    //When the agv reaches its destination: remove the first from all location list
//    public void onWayPointReach(MotionEvent motionControl, int wayPointIndex) {
//        
//        if(wayPoints.size() > 0)
//        {
//            wayPoints.remove(0);
//            durations.remove(0);
//        }
//        if(wayPoints.size() > 0)
//            addNewWayPointToMPath(wayPoints.get(0));
//        //add a new waypoint
//        if(wayPoints.size() == 0){
//            this.container.operationDone();
//            this.motionEvent = null;
//            this.motionPath = null;
//        }            
//    }
//    
}
    