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
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author erwin
 */
public class Crane extends WorldObject {
    
    public Vector3f defaultPos;
    public Vector3f motionTarget;
    public Magnet magnet;
    public Container targetContainer;

    private MotionPath motionPath;
    
    public Crane(Node rootNode, AssetManager assetManager, Vector3f position, Vector3f magnetPos, String modelFile) {
        super(rootNode, assetManager, position, modelFile);
        this.defaultPos = position;
        this.initMagnet(magnetPos);
    }
    
    public final void initMagnet(Vector3f position) {
        this.magnet = new Magnet(this.node, this.assetManager, position, "Models/magnet.j3o");
    }
    
    public void setTarget(Vector3f target) {
        this.motionTarget = target;
        this.motionPath = new MotionPath();
        MotionEvent craneMotion;
        
        if (this instanceof DockCrane) {
            this.motionPath.addWayPoint(new Vector3f(target.x, this.getPosition().y, this.getPosition().z));
            this.magnet.setTarget(new Vector3f(this.magnet.getPosition().x, target.y, target.z));
        } else if (this instanceof SortCrane) {
        }
        
        craneMotion = new MotionEvent(this.node, this.motionPath);
        craneMotion.setInitialDuration(10.0f);
        craneMotion.setSpeed(10.0f / this.motionTarget.distance(this.getPosition()));
        
        motionPath.addListener(new MotionPathListener() {
            public void onWayPointReach(MotionEvent control, int wayPointIndex) {
                if (motionPath.getNbWayPoints() == wayPointIndex + 1) {
                    magnet.attachContainer(targetContainer);
                    motionTarget = null;
                }
            }
        });
        
        craneMotion.play();       
    }
    
    public void resetPosition() {
        this.setTarget(this.defaultPos);
        this.magnet.resetPosition();        
    }
    
    public void targetContainer(Container container) {
        Vector3f containerPos = container.getRealPosition();
        Vector3f containerSize = container.node.getLocalScale();
        Vector3f targetPos = new Vector3f(containerPos.x, containerPos.y + (containerSize.y / 2), containerPos.z);
        this.targetContainer = container;
        this.setTarget(targetPos);
    }
}
