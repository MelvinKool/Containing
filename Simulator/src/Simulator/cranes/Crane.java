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
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 *
 * @author erwin
 */
public class Crane extends WorldObject {
    
    public Vector3f defaultPos;
    public Vector3f motionTarget;
    public GrabberHolder grabberHolder;
    public Grabber grabber;
    public Container targetContainer;
    public String craneType;
    
    private boolean grabbing = false;
    private MotionPath motionPath;
    private MotionEvent craneMotion;
    
    public Crane(Node rootNode, AssetManager assetManager, Vector3f position, Vector3f magnetPos, Spatial model, String craneType) {
        super(rootNode, assetManager, position, model);
        this.craneType = craneType;
        this.defaultPos = position;
        this.initGrabber();
    }
    
    public final void initGrabber() {
        this.grabberHolder = new GrabberHolder(this.node, this.assetManager, new Vector3f(0f, 9.6f, 0f), craneType);
        this.grabber = this.grabberHolder.initGrabber(craneType);
    }
    
    public void setTarget(Vector3f target) {
        this.motionTarget = target;
        this.motionPath = new MotionPath();
        
        // dirty fix for nullpointerexception
        // apparantly you need at least two waypoints
        this.motionPath.addWayPoint(this.getPosition());
        
        if (this.craneType.equals("dockingcrane") && this.node.getLocalRotation().getY() != 0.0) {
            this.motionPath.addWayPoint(new Vector3f(target.x, this.getPosition().y, this.getPosition().z));
        } else if (this.craneType.equals("dockingcrane") || this.craneType.equals("storagecrane")) {
            this.motionPath.addWayPoint(new Vector3f(this.getPosition().x, this.getPosition().y, target.z));
        }
        
        craneMotion = new MotionEvent(this.node, this.motionPath);
        craneMotion.setSpeed(5.0f);
        
        motionPath.addListener(new MotionPathListener() {
            public void onWayPointReach(MotionEvent control, int wayPointIndex) {
                if (motionPath.getNbWayPoints() == wayPointIndex + 1) {
                    motionTarget = null;
                    craneMotion.dispose();
                }
            }
        });
        
        craneMotion.play();
    }
    
    public void resetPosition() {
        this.setTarget(this.defaultPos);
    }
    
    public void targetContainer(Container container) {
        Vector3f targetPos = container.node.getWorldTranslation();
        Vector3f holderTarget = this.node.worldToLocal(targetPos, null);
        Vector3f grabberTarget = this.grabberHolder.node.worldToLocal(targetPos, null);
        this.targetContainer = container;
        this.setTarget(targetPos);
        
        this.grabberHolder.setTarget(holderTarget);
        
        this.grabberHolder.motionPath.addListener(this);
        this.grabber.setTarget(this.grabber.toRealPos(grabberTarget));
        this.grabber.motionPath.addListener(this);
        this.grabberHolder.grabberHolderMotion.play();
    }
    
    public void putContainer(Vector3f target) {
        Vector3f holderTarget = this.node.worldToLocal(target, null);
        Vector3f grabberTarget = this.node.worldToLocal(target, null);
        
        this.setTarget(target);
        this.grabberHolder.setTarget(holderTarget);
        this.grabber.setTarget(this.grabber.toRealPos(grabberTarget));
        this.grabber.motionPath.addListener(this);
    }
    
    @Override
    public void onWayPointReach(MotionEvent motionControl, int wayPointIndex) {
        if (this.targetContainer != null) {
            if (this.grabberHolder.motionPath.getNbWayPoints() == wayPointIndex + 1 && this.grabbing == false) {
                this.grabber.grabberMotion.play();
                this.grabbing = true;
            } else if (this.grabber.motionPath.getNbWayPoints() == wayPointIndex + 1) {
                this.grabber.attachContainer(this.targetContainer);
                this.grabbing = false;
                this.resetPosition();
                this.grabber.resetPosition();
            }
        }
    }
}
