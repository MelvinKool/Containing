/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulator.cranes;

import Simulator.WorldObject;
import com.jme3.asset.AssetManager;
import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 *
 * @author erwin
 */
public class Crane extends WorldObject {
    
    public Vector3f target;
    public Magnet magnet;
    
    public Crane(Node rootNode, AssetManager assetManager, Vector3f position, Vector3f magnetPos, String modelFile) {
        super(rootNode, assetManager, position, modelFile);
        this.initMagnet(magnetPos);
    }
    
    public final void initMagnet(Vector3f position) {
        this.magnet = new Magnet(this.node, this.assetManager, position, "Models/magnet.j3o");
    }
    
    public void setTarget(Vector3f target) {
        MotionPath cranePath = new MotionPath();
        MotionPath magnetPath = new MotionPath();
        MotionEvent craneMotion;
        MotionEvent magnetMotion;
        
        if (this instanceof DockCrane) {
            cranePath.addWayPoint(new Vector3f(target.x, this.getPosition().y, this.getPosition().z));
            magnetPath.addWayPoint(new Vector3f(this.magnet.getPosition().x, target.y, target.z));
        } else if (this instanceof SortCrane) {
            
        }
        
        craneMotion = new MotionEvent(this.node, cranePath);
        craneMotion.setInitialDuration(10.0f);
        craneMotion.setSpeed(10.0f / this.target.distance(this.getPosition()));
        
        magnetMotion = new MotionEvent(this.magnet.node, magnetPath);
        magnetMotion.setInitialDuration(10.0f);
        magnetMotion.setSpeed(10.0f / this.target.distance(this.magnet.getPosition()));
        
        craneMotion.play();
        magnetMotion.play();       
    }
}
