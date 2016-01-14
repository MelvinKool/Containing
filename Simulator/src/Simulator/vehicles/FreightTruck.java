/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulator.vehicles;

import Simulator.Container;
import Simulator.WorldObject;
import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author erwin
 */
public class FreightTruck extends WorldObject {
    
    public FreightTruck(Node rootNode, AssetManager assetManager, Vector3f position, Spatial model) {
        super(rootNode, assetManager, position, model);
    }
    
    public void attachContainer(Container container) {
        container.setVehicle(this);
        this.node.attachChild(container.node);
        container.setPosition(new Vector3f(0,1,0));
    }
    
}
