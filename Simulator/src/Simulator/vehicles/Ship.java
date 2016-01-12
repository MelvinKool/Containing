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
import java.util.List;

/**
 *
 * @author erwin
 */
public class Ship extends WorldObject {
    
    public List<Container> containers;
    
    public Ship(Node rootNode, AssetManager assetManager, Vector3f position, Spatial model) {
        super(rootNode, assetManager, position, model);
    }
    
}
