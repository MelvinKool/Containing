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
    public Grabber grabber;
    
    private MotionPath motionPath;
    private Container targetContainer;
    
    public GrabberHolder(Node node, AssetManager assetManager, List<MotionEvent> motionControls, String craneType) {
        super(node, assetManager, motionControls, Vector3f.ZERO, "Models/crane/" + craneType + "/grabbingGearHolder.j3o");
        this.defaultPos = Vector3f.ZERO;
        this.initGrabber(craneType);
    }
    
    public void initGrabber(String craneType) {
        this.grabber = new Grabber(this.node, this.assetManager, this.motionControls, craneType);
    }
    
    public void setTarget(Vector3f target) {
        this.motionTarget = target;
        this.motionPath = new MotionPath();
        
        this.motionPath.addWayPoint(this.getPosition());
        this.motionPath.addWayPoint(new Vector3f(target.x, this.getPosition().y, this.getPosition().z));
        
        grabberHolderMotion = new MotionEvent(this.node, this.motionPath);
        
        this.motionControls.add(grabberHolderMotion);
    }
    
}
