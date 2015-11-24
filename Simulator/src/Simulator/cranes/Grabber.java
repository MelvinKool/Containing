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
    
    private GrabbingHolder grabbingHolder;
    private Hook hookLeft;
    private Hook hookRight;
    
    public Grabber(Node rootNode, AssetManager assetManager, List<MotionEvent> motionControls, Vector3f position) {
        super(rootNode, assetManager, motionControls, position, "Models/crane/dockingcrane/grabbingGear.j3o");
        this.defaultPos = position;
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
    }
    
    public void resetPosition() {
        this.setTarget(this.defaultPos);
    }

    public void targetContainer(Container targetContainer, Vector3f targetPosition) {
        this.targetContainer = targetContainer;
        this.setTarget(targetPosition);
    }
}
