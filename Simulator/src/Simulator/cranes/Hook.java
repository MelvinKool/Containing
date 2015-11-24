/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulator.cranes;

import Simulator.WorldObject;
import com.jme3.asset.AssetManager;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import java.util.List;

/**
 *
 * @author erwin
 */
class Hook extends WorldObject {
    public Hook(Node rootNode, AssetManager assetManager, List<MotionEvent> motionControls, Vector3f position, String modelFile) {
        super(rootNode, assetManager, motionControls, position, modelFile);
    }
    
    public void open() {
        
    }
    
    public void close() {
        
    }
}
