/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulator.cranes;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 *
 * @author erwin
 */
public class SortCrane extends Crane {
    
    public Magnet magnet;
    
    public SortCrane(Node rootNode, AssetManager assetManager, Vector3f position) {
        super(rootNode, assetManager, position, new Vector3f(0, 0, 0), "Models/SortCrane.j3o");
    }
}
