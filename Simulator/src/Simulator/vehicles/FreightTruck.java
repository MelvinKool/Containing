/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulator.vehicles;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 *
 * @author erwin
 */
public class FreightTruck extends WorldObject {
    
    public FreightTruck(Node rootNode, AssetManager assetManager, Vector3f position, String modelFile) {
        super(rootNode, assetManager, position, modelFile);
    }
    
}
