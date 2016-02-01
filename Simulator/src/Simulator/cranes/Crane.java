/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulator.cranes;

import Simulator.Container;
import Simulator.WorldObject;
import com.jme3.asset.AssetManager;
import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private List<SimpleEntry> commandQueue;
    protected boolean hasHolder;
    
    public Crane(Node rootNode, AssetManager assetManager, Vector3f position, Vector3f magnetPos, Spatial model, String craneType, float speed) {
        super(rootNode, assetManager, position, model);
        this.craneType = craneType;
        this.defaultPos = position;
        this.speed = speed;
        this.hasHolder = true;
        this.commandQueue = new ArrayList<>();
        this.containerTarget = null;
    }
    
    /**
     * initialize grabber and grabberholder for this crane
     * @param position position of grabberHolder in relation to crane
     * @param grabberSpeed speed of grabber
     * @param holderSpeed speed of holder
     * @param yOffset offset to normal position of grabber on the y axis
     */
    public final void initGrabber(Vector3f position, float grabberSpeed, float holderSpeed, float yOffset) {
        this.grabberHolder = new GrabberHolder(this.node, this.assetManager, position, craneType, holderSpeed);
        this.grabber = this.grabberHolder.initGrabber(craneType, yOffset, grabberSpeed);
    }
    
     /**
     * motionPaths are not as accurate as we like so when done, this method is
     * called to fix the model's position to the actual target position
     */
    private void fixPositionToTarget() {
        if ((this.craneType.equals("dockingcrane") && this.node.getLocalRotation().getY() != 0.0) || this.craneType.equals("traincrane")) 
        {
            this.setPosition(new Vector3f(this.motionTarget.x, this.getPosition().y, this.getPosition().z));
        } else 
        {
            this.setPosition(new Vector3f(this.getPosition().x, this.getPosition().y, this.motionTarget.z));
        }
    }
    
    /**
     * Set a location to move to
     * @param target 
     */
    public void setTarget(Vector3f target) {
        this.motionTarget = target;
        this.motionPath = new MotionPath();
        this.holderDone = false;
        this.craneDone = false;
        float distance = 1;
        
        // dirty fix for nullpointerexception
        // apparantly you need at least two waypoints
        this.motionPath.addWayPoint(this.getPosition());
        
        if ((this.craneType.equals("dockingcrane") && this.node.getLocalRotation().getY() != 0.0) || this.craneType.equals("traincrane")) 
        {
            distance = FastMath.abs(target.x - this.getPosition().x);
            this.motionPath.addWayPoint(new Vector3f(target.x, this.getPosition().y, this.getPosition().z));
        } else {
            distance = FastMath.abs(target.z - this.getPosition().z);
            this.motionPath.addWayPoint(new Vector3f(this.getPosition().x, this.getPosition().y, target.z));
        }
        
        craneMotion = new MotionEvent(this.node, this.motionPath);
        //craneMotion.setSpeed(4.0f); // TODO: remove this line
        craneMotion.setInitialDuration(distance / this.speed);
        
        craneMotion.play();
    }
    
    /**
     * move to default position
     */
    public void resetPosition() 
    {
        this.setTarget(this.defaultPos);
    }
    
    /**
     * Move a non-attached container to a target
     * @param container
     * @param target 
     */
    public void moveContainer(Container container, Vector3f target) 
    {
        if (this.containerTarget == null) {
            this.containerTarget = target;
            this.grabContainer(container);     
        } else {
            this.commandQueue.add(new SimpleEntry<>(container, target));
        }
    }
    
    public void executeQueued() {
        if (!this.commandQueue.isEmpty() && this.targetContainer == null) {
            SimpleEntry command = this.commandQueue.remove(0);
            Container container = (Container) command.getKey();
            this.containerTarget = (Vector3f) command.getValue();
            this.grabContainer(container);
        }
    }
    
    /**
     * set container as target and pick it up
     * @param container 
     */
    public void grabContainer(Container container)
    {
        Vector3f targetPos = container.node.getWorldTranslation();
        Vector3f holderTarget = this.node.worldToLocal(targetPos, null);
        Vector3f grabberTarget = this.grabberHolder.node.worldToLocal(targetPos, null);
        this.targetContainer = container;
        this.cmd = Cmd.GRABBING;
        this.setTarget(targetPos);

        this.grabber.setTarget(this.grabber.toRealPos(grabberTarget));
        
        if (!this.craneType.equals("truckcrane")) { 
            this.grabberHolder.setTarget(holderTarget);        
            this.grabberHolder.motionPath.addListener(this);
            this.grabberHolder.grabberHolderMotion.play();
        }
        
        this.grabber.motionPath.addListener(this);
        this.motionPath.addListener(this);
    }
    
    /**
     * move currently attached container to a target and detach it
     * @param target 
     */
    public void putContainer(Vector3f target) 
    {
        Vector3f holderTarget = this.node.worldToLocal(target, null);
        Vector3f grabberTarget = this.node.worldToLocal(target, null);
        
        // Dockingcrane grabberposition has extra offset on the y axis
        // due to the fact that the holder is in the wrong position (toot low)
        // this "fixes" it
        if (this.craneType.equals("dockingcrane")) {
            grabberTarget.setY(grabberTarget.y - this.grabberHolder.getPosition().y);
        }
        
        this.setTarget(target);
        this.grabberHolder.setTarget(holderTarget);
        this.grabberHolder.motionPath.addListener(this);
        this.grabber.setTarget(this.grabber.toRealPos(grabberTarget));
        this.grabber.motionPath.addListener(this);
        this.grabberHolder.grabberHolderMotion.play();
        this.motionPath.addListener(this);
    }
    
    /**
     * check if crane and grabber are in position
     * if that's the case, then move grabber to target
     */
    private void moveGrabberIfReady() 
    {            
        //System.out.println("crane: trymove");

        if ((this.holderDone || !this.hasHolder) && this.craneDone) {
            this.grabber.grabberMotion.play();
            if (this.cmd == Cmd.GRABBING) {
                this.cmd = Cmd.Nothing;
            }
        }
    }
    
    /**
     * called when crane reaches the end of its motionPath
     */
    private void craneMotionDone() 
    {
        if (this.cmd == Cmd.GRABBING)
        {
            this.craneDone = true;
            this.moveGrabberIfReady();
        } else if (this.cmd == Cmd.PUTTING) {
            this.craneDone = true;
            this.moveGrabberIfReady();
        }
        this.fixPositionToTarget();
        this.motionPath = null;
    }
    
    /**
     * called when grabberHolder reaches the end of its motionPath
     */    
    private void holderMotionDone() 
    {
        if (this.cmd == Cmd.GRABBING) 
        {
            this.holderDone = true;
            this.moveGrabberIfReady();
        } else if (this.cmd == Cmd.PUTTING) 
        {
            this.holderDone = true;
            this.moveGrabberIfReady();
        }
        
        this.grabberHolder.motionPath = null;
        this.grabberHolder.fixPositionToTarget();
    }
    
    /**
     * Attach container after delay.
     * WARNING:
     * this method is called from a separate thread. This means that it is not
     * possible to make any modifications to the scene from this method.
     */
    private void delayAttachContainer() 
    {
        //System.out.println("crane: grabbed");
        this.cmd = Cmd.GRABBER;
        this.grabber.resetPosition(this);
        this.targetContainer.setVehicle(this);
    }
    
    /**
     * Detach container after delay.
     * WARNING:
     * this method is called from a separate thread. This means that it is not
     * possible to make any modifications to the scene from this method.
     */
    private void delayDetachContainer() 
    {        
        this.cmd = Cmd.RESET;
        this.targetContainer.operationDone();
        this.targetContainer = null;
        if (this.commandQueue.isEmpty()) 
        {
            this.grabber.resetPosition(this);
        }
    }
    
    /**
     * called when grabber reaches the end of its motionPath
     */
    private void grabbermotionDone() 
    {
        if (this.cmd != Cmd.GRABBER && this.cmd != Cmd.PUTTING && this.cmd != Cmd.RESET) 
        { // attach container
            this.grabber.fixPositionToTarget();
            this.grabber.attachContainer(this.targetContainer);
            // Asynchronously wait before attaching container so that we don't block execution
            new Thread(new Runnable(){
                    @Override
                    public void run() {
                        try
                        {
                            Thread.sleep(3000);
                        } catch (InterruptedException ex)
                        {
                            Logger.getLogger(Crane.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        delayAttachContainer();
                    }
            }).start();
        } else if (this.cmd == Cmd.GRABBER) 
        { // lower container
            this.putContainer(this.containerTarget);
            this.cmd = Cmd.PUTTING;
        } else if (this.cmd == Cmd.PUTTING) 
        { // detach container
            this.grabber.fixPositionToTarget();
            Vector3f pos = targetContainer.node.getWorldTranslation();
            Quaternion rot = targetContainer.node.getWorldRotation();
            this.rootNode.attachChild(this.targetContainer.node);
            targetContainer.node.setLocalTranslation(pos);
            targetContainer.node.setLocalRotation(rot);
            new Thread(new Runnable(){
                    @Override
                    public void run() {
                        try
                        {
                            Thread.sleep(3000
                                    );
                        } catch (InterruptedException ex)
                        {
                            Logger.getLogger(Crane.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        delayDetachContainer();
                    }
            }).start();
        } else if (this.cmd == Cmd.RESET) 
        { // end
            this.cmd = Cmd.Nothing;
            this.grabber.motionPath = null;
            this.targetContainer = null;
        }
    }
    
    
    @Override
    public void onWayPointReach(MotionEvent motionControl, int wayPointIndex) 
    {
        if (this.targetContainer != null) {            
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
