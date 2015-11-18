/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulator;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 *
 * @author erwin
 */
public class WorldObject {
    
    public Node node;
    public Node rootNode;
    public AssetManager assetManager;
    
    public WorldObject(Node rootNode, AssetManager assetManager, Vector3f position, String modelFile) {
        this.rootNode = rootNode;
        this.assetManager = assetManager;
        this.initObject(position, modelFile);
    }
    
    public void setPosition(Vector3f position) {
        this.node.setLocalTranslation(position);
    }
    
    public Vector3f getPosition() {
        return this.node.getLocalTranslation();
    }
    
    public Vector3f getRealPosition() {
        Vector3f pos = this.getPosition();
        Vector3f size = this.node.getLocalScale();
        
        float realX = (pos.x + size.x) / 2f;
        float realY = (pos.y + size.y) / 2f;
        float realZ = (pos.z + size.z) / 2f;

        return new Vector3f(realX, realY, realZ);
    }
    
    public void initObject(Vector3f initialPosition, String modelFile) {
        this.node = (Node) this.assetManager.loadModel(modelFile);
        this.setPosition(initialPosition);
        this.rootNode.attachChild(this.node);
    }
    
}
