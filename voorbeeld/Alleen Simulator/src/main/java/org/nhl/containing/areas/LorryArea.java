package org.nhl.containing.areas;

import com.jme3.asset.AssetManager;
import org.nhl.containing.cranes.TruckCrane;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jeroen
 */
public class LorryArea extends Area {

    public List<TruckCrane> truckCranes = new ArrayList();
    private AssetManager assetManager;
    private int cranes;
    private int craneXAxis = 0;

    public LorryArea(AssetManager assetManager, int cranes) {
        this.assetManager = assetManager;
        this.cranes = cranes;
        initLorryArea();
    }

    /**
     * Initialize a lorry area.
     */
    private void initLorryArea() {
        // Add truck cranes to the list and scene.
        int craneCount = cranes - 1;
        for (int i = 0; i < cranes; i++) {
            truckCranes.add(new TruckCrane(assetManager));
            truckCranes.get(i).setLocalTranslation(craneXAxis, 0, 0);
            truckCranes.get(i).setId(craneCount);
            this.attachChild(truckCranes.get(i));
            craneXAxis += 14;
            craneCount--;
        }
    }
    
    public List<TruckCrane> getTruckCranes(){
        return truckCranes;
    }
}
