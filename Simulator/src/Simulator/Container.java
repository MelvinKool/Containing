/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulator;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.*;

/**
 *
 * @author erwin
 */
public class Container extends WorldObject {
    
    public Container(Node rootNode, AssetManager assetManager, Vector3f position, String modelFile) {
        super(rootNode, assetManager, position, modelFile);
    }
    
}
