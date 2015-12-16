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
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import java.util.List;

/**
 *
 * @author erwin
 */
class GrabberHolder extends WorldObject {
public Vector3f motionTarget;
    public Vector3f defaultPos;
    public MotionEvent grabberHolderMotion;
    public String craneType;
    
    public MotionPath motionPath;
    private Container targetContainer;
    private float speed;
    
    
    public GrabberHolder(Node node, AssetManager assetManager, Vector3f position, String craneType, float speed) {
        super(node, assetManager, position, "Models/crane/" + craneType + "/grabbingGearHolder.j3o");
        this.defaultPos = position;
        this.craneType = craneType;
        this.speed = speed;
    }
    
    public Grabber initGrabber(String craneType) {
        return new Grabber(this.node, this.assetManager, craneType);
    }
    
    // motionPaths are not as accurate as we like
    public void fixPositionToTarget() {
        this.setPosition(new Vector3f(this.motionTarget.x, this.getPosition().y, this.getPosition().z));
    }
    
    public void setTarget(Vector3f target) {        
        this.motionTarget = target;
        this.motionPath = new MotionPath();
        float distance = FastMath.abs(target.x - this.getPosition().x);
        
        System.out.println("holderdist: " + distance + " tx: " + target.x + " hx: " + this.getPosition().x);
        
        this.motionPath.addWayPoint(this.getPosition());
        this.motionPath.addWayPoint(new Vector3f(target.x, this.getPosition().y, this.getPosition().z));
        
        grabberHolderMotion = new MotionEvent(this.node, this.motionPath);
        grabberHolderMotion.setSpeed(1.0f);
        grabberHolderMotion.setInitialDuration(distance / this.speed);
        System.out.println("hduration: " + distance / this.speed);
    }
    
}
