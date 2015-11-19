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

/**
 *
 * @author erwin
 */
public class AGV extends WorldObject {
    
    public Container container;
    
    public AGV(Node rootNode, AssetManager assetManager, Vector3f position) {
        super(rootNode, assetManager, position, "Models/AGV.j3o");
    }
}
