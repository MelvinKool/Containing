/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulator.cranes;

import Simulator.Container;
import Simulator.WorldObject;
import com.jme3.asset.AssetManager;
import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.MotionPathListener;
import com.jme3.cinematic.events.CinematicEvent;
import com.jme3.cinematic.events.CinematicEventListener;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import java.util.List;

/**
 *
 * @author erwin
 */
public class Crane extends WorldObject implements CinematicEventListener {
    
    public Vector3f defaultPos;
    public Vector3f motionTarget;
    public GrabberHolder grabberHolder;
    public Container targetContainer;

    private MotionPath motionPath;
    private MotionEvent craneMotion;
    
    public Crane(Node rootNode, AssetManager assetManager, List<MotionEvent> motionControls, Vector3f position, Vector3f magnetPos, String modelFile) {
        super(rootNode, assetManager, motionControls, position, modelFile);
        this.defaultPos = position;
    }
    
    public final void initGrabber(String craneType) {
        this.grabberHolder = new GrabberHolder(this.node, this.assetManager, this.motionControls, craneType);
    }
    
    public void setTarget(Vector3f target) {
        this.motionTarget = target;
        this.motionPath = new MotionPath();
        
        // dirty fix for nullpointerexception
        // apparantly you need at least two waypoints
        this.motionPath.addWayPoint(this.getPosition());
        
        if (this instanceof DockCrane) {
            this.motionPath.addWayPoint(new Vector3f(target.x, this.getPosition().y, this.getPosition().z));
        } else if (this instanceof SortCrane) {
        }
        
        craneMotion = new MotionEvent(this.node, this.motionPath);
        craneMotion.setSpeed(5.0f);
        
        motionPath.addListener(new MotionPathListener() {
            public void onWayPointReach(MotionEvent control, int wayPointIndex) {
                if (motionPath.getNbWayPoints() == wayPointIndex + 1) {
                    motionTarget = null;
                    motionControls.remove(craneMotion);
                }
            }
        });
        
        motionControls.add(craneMotion);
    }
    
    public void resetPosition() {
        this.setTarget(this.defaultPos);      
    }
    
    public void targetContainer(Container container) {
        Vector3f containerPos = container.getRealPosition();
        Vector3f containerSize = container.node.getLocalScale();
        Vector3f targetPos = new Vector3f(containerPos.x, containerPos.y + (containerSize.y / 2), containerPos.z);
        this.targetContainer = container;
        this.setTarget(targetPos);
        
        this.grabberHolder.setTarget(targetPos);
        
        this.grabberHolder.grabberHolderMotion.addListener(this);
        this.grabberHolder.grabber.setTarget(targetPos);
    }

    public void onWayPointReach(MotionEvent motionControl, int wayPointIndex) {
        
    }

    public void onPlay(CinematicEvent cinematic) { }

    public void onPause(CinematicEvent cinematic) { }

    public void onStop(CinematicEvent cinematic) { }
}
