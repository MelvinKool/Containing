/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulator.vehicles;

import Simulator.Container;
import Simulator.WorldObject;
import com.jme3.asset.AssetManager;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author erwin
 */
public class TrainCart extends WorldObject {
    
    public Container container;
    
    public TrainCart(Node rootNode, AssetManager assetManager, Vector3f position, Spatial model) {
        super(rootNode, assetManager, position, model);
    }
    
    public void attachContainer(Container container) {
        Vector3f cartPos = this.getPosition();
        Vector3f containerPos = cartPos.add(0.0f, 1.0f, 0.0f);
        this.node.attachChild(container.node);
        
        container.setPosition(containerPos);
        container.node.rotate(0.0f, FastMath.DEG_TO_RAD * 90.0f, 0.0f);
        
    }
        
    
}
