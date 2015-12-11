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
    
    
    public GrabberHolder(Node node, AssetManager assetManager, Vector3f position, String craneType) {
        super(node, assetManager, position, "Models/crane/" + craneType + "/grabbingGearHolder.j3o");
        this.defaultPos = position;
        this.craneType = craneType;
    }
    
    public Grabber initGrabber(String craneType) {
        return new Grabber(this.node, this.assetManager, craneType);
    }
    
    public void setTarget(Vector3f target) {        
        this.motionTarget = target;
        this.motionPath = new MotionPath();
        
        this.motionPath.addWayPoint(this.getPosition());
        this.motionPath.addWayPoint(new Vector3f(target.x, this.getPosition().y, this.getPosition().z));
        
        grabberHolderMotion = new MotionEvent(this.node, this.motionPath);
        grabberHolderMotion.setSpeed(5.0f);
    }
    
}
