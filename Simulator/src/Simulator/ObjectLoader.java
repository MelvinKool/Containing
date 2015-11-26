/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulator;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Spatial;

/**
 *
 * @author erwin
 */
public class ObjectLoader {
    
    public Spatial container;
    public Spatial sortCrane;
    public Spatial dockCrane;
    public Spatial agv;
    public Spatial freightTruck;
    public Spatial locomotive;
    public Spatial Ship;
    public Spatial trainCart;
    
    public ObjectLoader(AssetManager assetManager) {
        this.dockCrane = assetManager.loadModel("Models/crane/dockingcrane/crane.j3o");
        this.container = assetManager.loadModel("Models/container/container.j3o");
    }
    
    public Spatial getDockCraneModel() {
       return this.dockCrane.clone();
    }
    
    public Spatial getContainerModel() {
       return this.container.clone();
    }
    
    public void loadCranes() {
    }
    
}
