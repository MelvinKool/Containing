/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulator.cranes;

import Simulator.*;
import com.jme3.asset.AssetManager;
import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import java.util.List;

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
    private float yOffset;
    
    public Grabber(Node rootNode, AssetManager assetManager, String craneType, float yOffset) {
        super(rootNode, assetManager, Vector3f.ZERO, "Models/crane/" + craneType + "/grabbingGear.j3o");
        this.defaultPos = Vector3f.ZERO;
        this.container = null;
        this.yOffset = yOffset;
        
        this.hookLeft = new Hook(
                this.node, this.assetManager,
                Vector3f.ZERO, "Models/crane/" + craneType + "/hookLeft.j3o",
                Hook.LEFT_HOOK);
        this.hookRight = new Hook(
                this.node, this.assetManager,
                Vector3f.ZERO, "Models/crane/" + craneType + "/hookRight.j3o",
                Hook.RIGHT_HOOK);
    }
    
    /**
     * Fixes position with y offset of grabber with center point
     * @param pos
     * @return new Vector3f with fixed postion
     */
    public Vector3f toRealPos(Vector3f pos) {
        return new Vector3f(0.0f, pos.y + this.yOffset, 0.0f);
    }
    
    /**
     * set a target to move to
     * @param target
     */
    public void setTarget(Vector3f target) {
        this.motionTarget = target;
        this.motionPath = new MotionPath();
        
        this.motionPath.addWayPoint(this.getPosition());

        this.motionPath.addWayPoint(new Vector3f(this.getPosition().x, target.y, this.getPosition().z));
        
        grabberMotion = new MotionEvent(this.node, this.motionPath);
        grabberMotion.setSpeed(5.0f); // TODO: remove this line
    }
    
    /**
     * attach container to grabber node
     * @param container 
     */
    public void attachContainer(Container container) {
        Vector3f pos = container.node.getWorldTranslation();
        Quaternion rot = container.node.getWorldRotation();
        this.node.attachChild(container.node);
        container.node.setLocalTranslation(this.node.worldToLocal(pos, null));
        container.node.rotate(rot);
        this.container = container;
    }
    
//    public void detachContainer() {
//        Vector3f pos = container.node.getWorldTranslation();
//        Quaternion rot = container.node.getWorldRotation();
//        container.node.setLocalTranslation(pos);
//        container.node.setLocalRotation(rot);
//        //container = null;
//    }
    
    /**
     * reset position to default position
     * @param listener crane instance to add to listeners of motionPath
     */
    public void resetPosition(Crane listener) {
        this.setTarget(this.defaultPos);
        this.motionPath.addListener(listener);
        this.grabberMotion.play();
    }
}
