/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulator;

import com.jme3.asset.AssetManager;
import com.jme3.cinematic.MotionPathListener;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.List;

/**
 *
 * @author erwin
 */
public class WorldObject implements Cloneable, MotionPathListener {
    
    public Node node;
    public Node rootNode;
    public AssetManager assetManager;
    public List<MotionEvent> motionControls;
    
    public WorldObject(Node rootNode, AssetManager assetManager, List<MotionEvent> motionControls, Vector3f position, String modelFile) {
        this.rootNode = rootNode;
        this.assetManager = assetManager;
        this.motionControls = motionControls;
        this.initObject(position, modelFile);
    }
    
    public WorldObject(Node rootNode, AssetManager assetManager, List<MotionEvent> motionControls, Vector3f position, Spatial model) {

        this.rootNode = rootNode;
        this.assetManager = assetManager;
        this.motionControls = motionControls;
        this.initModel(position, model);
    }
    
    private void initModel(Vector3f position, Spatial model) {
        if (model instanceof Geometry) {
            this.node = new Node();
            this.node.attachChild(model);
        } else {
            this.node = (Node) model;
        }
        
        this.setPosition(position);
        this.rootNode.attachChild(model);
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
    
    public WorldObject clone() throws CloneNotSupportedException {
       return (WorldObject) super.clone();
    }
    
    
    public void initObject(Vector3f initialPosition, String modelFile) {
        Spatial spatial = this.assetManager.loadModel(modelFile);
        
        if (spatial instanceof Geometry) {
            this.node = new Node();
            this.node.attachChild(spatial);
        } else {
            this.node = (Node) spatial;
        }
        
        this.setPosition(initialPosition);
        this.rootNode.attachChild(this.node);
    }

    @Override
    public void onWayPointReach(MotionEvent motionControl, int wayPointIndex) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
