/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulator.vehicles;

import Simulator.Container;
import Simulator.WorldObject;
import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingBox;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author erwin
 */
public class Ship extends WorldObject {
    
    public List<Container> containers;
    
    public Ship(Node rootNode, AssetManager assetManager, Vector3f position, Spatial model, int rotation, float scale) 
    {
        super(rootNode, assetManager, position, model);
        this.node.scale(scale, 1, scale);
        this.node.rotate(0,FastMath.DEG_TO_RAD * rotation, 0);
        this.containers = new ArrayList<>();
    }
   
    /**
     * add List of containers to ship and stack them
     * @param containers 
     */
    public void addContainers(List<Container> containers) 
    {
        int[] bounds = {16, 6, 19};
        int[] indexes = {0, 0, 0};
        Vector3f shipSizeC = new Vector3f();
        Vector3f containerSizeC = new Vector3f(); // size from center
        Vector3f containerSize;                   // full size
        Collections.reverse(containers);
        
        ((BoundingBox) containers.get(0).node.getWorldBound()).getExtent(containerSizeC);
        ((BoundingBox) this.node.getWorldBound()).getExtent(shipSizeC);
        
        containerSize = containerSizeC.mult(2);
        
        for (Container container :  containers) {
            float xOffset = shipSizeC.x - ((bounds[0] * containerSizeC.x));
            Vector3f pos = new Vector3f(
                    (indexes[0] * containerSize.x) + xOffset - shipSizeC.x,
                    (indexes[1] * containerSize.y),
                    (indexes[2] * containerSize.z));
            
            this.attachContainer(container, pos);
            
            if (indexes[0] == bounds[0]) 
            {
                indexes[0] = 0;
                indexes[2]++;
                if (indexes[2] == bounds[2]) 
                {
                    indexes[2] = 0;
                    indexes[1]++; // height is unlimited
                }
            } else
            {
                indexes[0]++;
            }
        }
    }

    /**
     * attach container to ship node on given position
     * @param container
     * @param containerPos 
     */
    private void attachContainer(Container container, Vector3f containerPos)
    {
        this.node.attachChild(container.node);
        container.setPosition(containerPos);
        this.containers.add(container);
        container.setVehicle(this);
    }
}
