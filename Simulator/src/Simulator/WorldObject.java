package Simulator;

import com.jme3.asset.AssetManager;
import com.jme3.asset.AssetNotFoundException;
import com.jme3.cinematic.MotionPathListener;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class WorldObject implements Cloneable, MotionPathListener
{
    
    public Node node;
    public Node rootNode;
    public AssetManager assetManager;
    
    public WorldObject(Node rootNode, AssetManager assetManager, Vector3f position, String modelFile)
    {
        this.rootNode = rootNode;
        this.assetManager = assetManager;
        this.initObject(position, modelFile);
    }
    
    public WorldObject(Node rootNode, AssetManager assetManager, Vector3f position, Spatial model)
    {
        this.rootNode = rootNode;
        this.assetManager = assetManager;
        this.initModel(position, model);
    }
    
    private void initModel(Vector3f position, Spatial model)
    {
        if (model instanceof Geometry)
        {
            this.node = new Node();
            this.node.attachChild(model);
        }
        else
        {
            this.node = (Node) model;
        }
        
        this.setPosition(position);
        this.rootNode.attachChild(model);
    }
    
    public void setPosition(Vector3f position)
    {
        this.node.setLocalTranslation(position);
    }
    
    public Vector3f getPosition()
    {
        return this.node.getLocalTranslation();
    }
    
    @Override
    public WorldObject clone() throws CloneNotSupportedException
    {
       return (WorldObject) super.clone();
    }
    
    
    public void initObject(Vector3f initialPosition, String modelFile)
    {
        Spatial spatial = null;
        try
        {
            spatial = this.assetManager.loadModel(modelFile);        
        }
        catch(AssetNotFoundException e)
        {
            System.err.println("Model not found: " + modelFile);
        }
        
        if(spatial instanceof Geometry)
        {
            this.node = new Node();
            this.node.attachChild(spatial);
        }
        else if(spatial == null)
        {
            this.node = new Node();
        }
        else
        {
            this.node = (Node) spatial;
        }
        this.setPosition(initialPosition);
        this.rootNode.attachChild(this.node);
    }

    @Override
    public void onWayPointReach(MotionEvent motionControl, int wayPointIndex)
    {
        
    }
}