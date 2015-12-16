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

    private enum Cmd { GRABBING, PUTTING, GRABBER, RESET, Nothing  };

    
    public Vector3f defaultPos;
    public Vector3f motionTarget;
    public Container targetContainer;
    public String craneType;
    
    private GrabberHolder grabberHolder;
    private Grabber grabber;
    private MotionPath motionPath;
    private MotionEvent craneMotion;
    private Cmd cmd;
    private Vector3f containerTarget;
    private float speed;
    private boolean holderDone;
    private boolean craneDone;
    
    public Crane(Node rootNode, AssetManager assetManager, Vector3f position, Vector3f magnetPos, Spatial model, String craneType, float speed) {
        super(rootNode, assetManager, position, model);
        this.craneType = craneType;
        this.defaultPos = position;
        this.speed = speed;
    }
    
    public final void initGrabber(Vector3f position, float holderSpeed) {
        this.grabberHolder = new GrabberHolder(this.node, this.assetManager, position, craneType, holderSpeed);
        this.grabber = this.grabberHolder.initGrabber(craneType);
    }
    
    // motionPaths are not as accurate as we like
    private void fixPositionToTarget() {
        if (this.craneType.equals("dockingcrane") && this.node.getLocalRotation().getY() != 0.0) {
            this.setPosition(new Vector3f(this.motionTarget.x, this.getPosition().y, this.getPosition().z));
        } else 
        {
            this.setPosition(new Vector3f(this.getPosition().x, this.getPosition().y, this.motionTarget.z));
        }
    }
    
    public void setTarget(Vector3f target) {
        this.motionTarget = target;
        this.motionPath = new MotionPath();
        this.holderDone = false;
        this.craneDone = false;
        float distance = 1;
        
        // dirty fix for nullpointerexception
        // apparantly you need at least two waypoints
        this.motionPath.addWayPoint(this.getPosition());
        
        if (this.craneType.equals("dockingcrane") && this.node.getLocalRotation().getY() != 0.0) {
            distance = FastMath.abs(target.x - this.getPosition().x);
            this.motionPath.addWayPoint(new Vector3f(target.x, this.getPosition().y, this.getPosition().z));
        } else {
            distance = FastMath.abs(target.z - this.getPosition().z);
            this.motionPath.addWayPoint(new Vector3f(this.getPosition().x, this.getPosition().y, target.z));
        }
        
        craneMotion = new MotionEvent(this.node, this.motionPath);
        craneMotion.setSpeed(1.0f);
        craneMotion.setInitialDuration(distance / this.speed);
        
        System.out.println(distance);
        System.out.println(craneMotion.getDuration());
        
        craneMotion.play();
    }
    
    public void resetPosition() {
        this.setTarget(this.defaultPos);
    }
    
    public void moveContainer(Container container, Vector3f target) {
        this.containerTarget = target;
        this.grabContainer(container);        
    }
    
    public void grabContainer(Container container) {
        Vector3f targetPos = container.node.getWorldTranslation();
        Vector3f holderTarget = this.node.worldToLocal(targetPos, null);
        Vector3f grabberTarget = this.grabberHolder.node.worldToLocal(targetPos, null);
        this.targetContainer = container;
        this.cmd = Cmd.GRABBING;
        this.setTarget(targetPos);
        
        if (!this.craneType.equals("truckcrane")) { 
            this.grabberHolder.setTarget(holderTarget);        
            this.grabberHolder.motionPath.addListener(this);
            this.grabberHolder.grabberHolderMotion.play();
        }
        
        this.grabber.setTarget(this.grabber.toRealPos(grabberTarget));
        this.grabber.motionPath.addListener(this);
        this.motionPath.addListener(this);
    }
    
    public void putContainer(Vector3f target) {
        Vector3f holderTarget = this.node.worldToLocal(target, null);
        Vector3f grabberTarget = this.node.worldToLocal(target, null);
        
        this.setTarget(target);
        this.grabberHolder.setTarget(holderTarget);
        this.grabberHolder.motionPath.addListener(this);
        this.grabber.setTarget(this.grabber.toRealPos(grabberTarget));
        this.grabber.motionPath.addListener(this);
        this.grabberHolder.grabberHolderMotion.play();
    }
    
    private void grabIfReady() {
        if (this.holderDone && this.craneDone) {
            this.grabber.grabberMotion.play();

            System.err.println("grabbing");
            this.cmd = Cmd.Nothing;
        }
    }
    
    private void craneMotionDone() {
        if (this.cmd == Cmd.GRABBING)
        {
            this.craneDone = true;
            this.grabIfReady();
        }
        this.fixPositionToTarget();
    }
    
    private void holderMotionDone() {
        
        if (this.cmd == Cmd.GRABBING) 
        {
            this.holderDone = true;
            this.grabIfReady();
        } else if (this.cmd == Cmd.PUTTING) 
        {
            this.grabber.grabberMotion.play();
            this.grabberHolder.motionPath = null;
        }
        this.grabberHolder.fixPositionToTarget();
    }
    
    private void grabbermotionDone() {
        if (this.cmd != Cmd.GRABBER && this.cmd != Cmd.PUTTING && this.cmd != Cmd.RESET) 
        {
            this.grabber.attachContainer(this.targetContainer);
            System.err.println("grabbed");
            this.cmd = Cmd.GRABBER;
            this.grabber.resetPosition(this);
        } else if (this.cmd == Cmd.GRABBER) 
        {
            this.putContainer(this.containerTarget);
            System.err.println("putting");
            this.cmd = Cmd.PUTTING;
        } else if (this.cmd == Cmd.PUTTING) 
        {
            System.err.println("detach");
            Vector3f pos = targetContainer.node.getWorldTranslation();
            Quaternion rot = targetContainer.node.getWorldRotation();
            this.rootNode.attachChild(this.targetContainer.node);
            targetContainer.node.setLocalTranslation(pos);
            targetContainer.node.setLocalRotation(rot);

            //this.targetContainer = null;
            this.grabber.resetPosition(this);
            this.cmd = Cmd.RESET;
        } else if (this.cmd == Cmd.RESET) 
        {
            System.err.println("reset");
            this.cmd = Cmd.Nothing;
            this.motionPath = null;
            this.grabber.motionPath = null;
        }
    }
    
    @Override
    public void onWayPointReach(MotionEvent motionControl, int wayPointIndex) {
        if (this.targetContainer != null) {
            System.out.println("waypoint");
            
            if (motionControl == this.craneMotion && 
                    this.motionPath.getNbWayPoints() == wayPointIndex + 1) 
            { // handlers for crane
                this.craneMotionDone();
            } else if (motionControl == this.grabberHolder.grabberHolderMotion && 
                    this.grabberHolder.motionPath.getNbWayPoints() == wayPointIndex + 1) 
            { // handlers for grabberHolder
                this.holderMotionDone();
            } else if (motionControl == this.grabber.grabberMotion &&
                    this.grabber.motionPath.getNbWayPoints() == wayPointIndex + 1) 
            { // handlers for grabber
                this.grabbermotionDone();
            } 
        }
    }
}
