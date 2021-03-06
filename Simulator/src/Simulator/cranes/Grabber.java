/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulator.cranes;

import Simulator.*;
import com.jme3.asset.AssetManager;
import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 *
 * @author erwin
 */
public class Grabber extends WorldObject {
    
    public Vector3f motionTarget;
    public Vector3f defaultPos;
    public MotionEvent grabberMotion;
    public MotionPath motionPath;
    public Container container;
    
    private Hook hookLeft;
    private Hook hookRight;
    private float speed;
    private float yOffset;
    
    public Grabber(Node rootNode, AssetManager assetManager, String craneType, float yOffset, float grabberSpeed)
    {
        super(rootNode, assetManager, new Vector3f(0,0,0), "Models/crane/" + craneType + "/grabbingGear.j3o");
        this.defaultPos = new Vector3f(0,0,0);
        this.container = null;
        this.yOffset = yOffset;
        this.speed = grabberSpeed;
        
        this.hookLeft = new Hook(
                this.node, this.assetManager,
                new Vector3f(0,0,0), "Models/crane/" + craneType + "/hookLeft.j3o",
                Hook.LEFT_HOOK);
        this.hookRight = new Hook(
                this.node, this.assetManager,
                new Vector3f(0,0,0), "Models/crane/" + craneType + "/hookRight.j3o",
                Hook.RIGHT_HOOK);
    }
    
    /**
     * Fixes position with y offset of grabber with center point
     * @param pos
     * @return new Vector3f with fixed postion
     */
    public Vector3f toRealPos(Vector3f pos)
    {
        return new Vector3f(0.0f, pos.y + this.yOffset, 0.0f);
    }
    
    public void fixPositionToTarget()
    {
        this.setPosition(this.motionTarget);
    }
    
    /**
     * set a target to move to
     * @param target
     */
    public void setTarget(Vector3f target) 
    {
        float distance = FastMath.abs(target.y - this.getPosition().y);
        this.motionTarget = target;
        this.motionPath = new MotionPath();
        
        this.motionPath.addWayPoint(this.getPosition());

        this.motionPath.addWayPoint(new Vector3f(this.getPosition().x, target.y, this.getPosition().z));
        
        grabberMotion = new MotionEvent(this.node, this.motionPath);
        grabberMotion.setInitialDuration(distance / this.speed);
    }
    
    /**
     * attach container to grabber node
     * @param container 
     */
    public void attachContainer(Container container) 
    {
        Vector3f pos = container.node.getWorldTranslation();
        Quaternion rot = container.node.getWorldRotation();
        this.node.attachChild(container.node);
        container.node.setLocalTranslation(this.node.worldToLocal(pos, null));
        //container.node.setLocalRotation(rot);
        this.container = container;
    }
    
//    public void detachContainer() {
//        Vector3f pos = container.node.getWorldTranslation();
//        Quaternion rot = container.node.getWorldRotation();
//        container.node.setLocalTranslation(pos);
//        container.node.setLocalRotation(rot);
//        container = null;
//    }
    
    /**
     * reset position to default position
     * @param listener crane instance to add to listeners of motionPath
     */
    public void resetPosition(Crane listener) 
    {
        this.setTarget(this.defaultPos);
        this.motionPath.addListener(listener);
        this.grabberMotion.play();
    }
}
