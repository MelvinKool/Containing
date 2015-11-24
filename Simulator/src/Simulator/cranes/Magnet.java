/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulator.cranes;

import Simulator.*;
import com.jme3.asset.AssetManager;
import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.MotionPathListener;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author erwin
 */
public class Magnet extends WorldObject {
    
    public Vector3f motionTarget;
    public Vector3f defaultPos;
    private MotionPath motionPath;
    
    public Magnet(Node rootNode, AssetManager assetManager, Vector3f position, String modelFile) {
        super(rootNode, assetManager, position, modelFile);
        this.defaultPos = position;
    }
    
    public void setTarget(Vector3f target) {
        this.motionTarget = target;
        this.motionPath = new MotionPath();
        MotionEvent craneMotion;
        
        this.motionPath.addWayPoint(target);
        
        craneMotion = new MotionEvent(this.node, this.motionPath);
        craneMotion.setInitialDuration(10.0f);
        craneMotion.setSpeed(10.0f / this.motionTarget.distance(this.getPosition()));
        
        craneMotion.play();  
    }
    
    public void attachContainer(Container container) {
        this.node.attachChild(container.node);
    }
    
    public void resetPosition() {
        this.setTarget(this.defaultPos);
    }
}
