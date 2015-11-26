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
import java.util.List;
import java.util.Set;

/**
 *
 * @author erwin
 */
public class Grabber extends WorldObject {
    
    public Vector3f motionTarget;
    public Vector3f defaultPos;
    public MotionEvent grabberMotion;
    private MotionPath motionPath;
    private Container targetContainer;
    
    private GrabberHolder grabbingHolder;
    private Hook hookLeft;
    private Hook hookRight;
    
    public Grabber(Node rootNode, AssetManager assetManager, List<MotionEvent> motionControls, String craneType) {
        super(rootNode, assetManager, motionControls, Vector3f.ZERO, "Models/crane/" + craneType + "/grabbingGear.j3o");
        this.defaultPos = Vector3f.ZERO;
        
        this.hookLeft = new Hook(
                this.node, this.assetManager, this.motionControls,
                new Vector3f(0,0,0), "Models/crane/dockingcrane/hookLeft.j3o",
                Hook.LEFT_HOOK);
        this.hookRight = new Hook(
                this.node, this.assetManager, this.motionControls, 
                new Vector3f(0,0,0), "Models/crane/dockingcrane/hookRight.j3o",
                Hook.RIGHT_HOOK);
    }
    
    /**
     *
     * @param target
     */
    public void setTarget(Vector3f target) {
        this.motionTarget = target;
        this.motionPath = new MotionPath();
        
        this.motionPath.addWayPoint(this.getPosition());

        this.motionPath.addWayPoint(target);
        
        grabberMotion = new MotionEvent(this.node, this.motionPath);
        
        motionPath.addListener(new MotionPathListener() {
            public void onWayPointReach(MotionEvent control, int wayPointIndex) {
                if (motionPath.getNbWayPoints() == wayPointIndex + 1) {
                    if (targetContainer != null) {
                        attachContainer(targetContainer);
                    }
                    motionTarget = null;
                    motionControls.remove(grabberMotion);
                }
            }
        });
        
        
        motionControls.add(grabberMotion);
    }
    
    public void attachContainer(Container container) {
        this.node.attachChild(container.node);
        
        this.hookLeft.close();
        this.hookRight.close();
    }
    
    public void resetPosition() {
        this.setTarget(this.defaultPos);
    }

    public void targetContainer(Container targetContainer, Vector3f targetPosition) {
        this.targetContainer = targetContainer;
        this.setTarget(targetPosition);
        
        this.hookLeft.open();
        this.hookRight.open();
    }
}
