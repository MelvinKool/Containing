/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulator.cranes;

import Simulator.Container;
import com.jme3.asset.AssetManager;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.List;

/**
 *
 * @author erwin
 */
public class DockCrane extends Crane {
    
    public DockCrane(Node rootNode, AssetManager assetManager, List<MotionEvent> motionControls, Vector3f position, Spatial model) {
        super(rootNode, assetManager, motionControls, position, Vector3f.ZERO, model);
        this.node.rotate(0, (float) Math.toRadians(90), 0);
        this.initGrabber("dockingcrane");
    }   
}
