/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulator.cranes;

import Simulator.WorldObject;
import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 *
 * @author erwin
 */
public class Magnet extends WorldObject {
    public Magnet(Node rootNode, AssetManager assetManager, Vector3f position, String modelFile) {
        super(rootNode, assetManager, position, modelFile);
    }
}
