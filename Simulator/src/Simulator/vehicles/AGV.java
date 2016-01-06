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
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.List;

/**
 *
 * @author erwin
 */
public class AGV extends WorldObject {
    
    private MotionPath motionPath;
    private MotionEvent motionEvent;
    
    public Container container;
    
    public AGV(Node rootNode, AssetManager assetManager, Vector3f position, Spatial model) {
        super(rootNode, assetManager, position, model);
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
            this.motionEvent.setLookAt(wayPoints.get(0), Vector3f.ZERO);
            this.motionEvent.setDirectionType(MotionEvent.Direction.LookAt);
            
            System.out.println("new motionevent");
        }
        
        if (this.motionEvent.getPlayState() != PlayState.Playing) {
            this.motionEvent.play();
                        this.motionPath.setCurveTension(0.0f);
            System.out.println("start play");
        }
        
        this.motionPath.addListener(this);
    }
    
    @Override
    public void onWayPointReach(MotionEvent motionControl, int wayPointIndex) {
        //this.motionPath.removeWayPoint(wayPointIndex);
        if (this.motionPath.getNbWayPoints() == wayPointIndex + 1) {
            System.out.println("delete motion");
            this.motionPath = null;
            this.motionEvent = null; 
        }
    }
}
