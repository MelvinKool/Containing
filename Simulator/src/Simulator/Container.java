package Simulator;

import com.jme3.asset.AssetManager;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.math.Vector3f;
import com.jme3.scene.*;
import java.util.List;

public class Container extends WorldObject
{
    public String containerId;
    
    public Container(Node rootNode, AssetManager assetManager, List<MotionEvent> motionControls, Vector3f position, Spatial model)
    {
        super(rootNode, assetManager, position, model);
    }    
}