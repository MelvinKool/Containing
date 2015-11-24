/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulator.cranes;

import Simulator.Container;
import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 *
 * @author erwin
 */
public class DockCrane extends Crane {
    
    public DockCrane(Node rootNode, AssetManager assetManager, Vector3f position) {
        super(rootNode, assetManager, position, new Vector3f(0, 0, 0), "Models/DockCrane.j3o");
    }   
}
