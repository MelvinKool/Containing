
package org.nhl.containing.cranes;

import com.jme3.asset.AssetManager;
import org.nhl.containing.areas.BoatArea;

public class DockingCraneInlandShip extends DockingCrane{

    public DockingCraneInlandShip(AssetManager assetManager, BoatArea.AreaType type) {
        super(assetManager, type);
        this.name = "DockingCraneInlandShip";
    }
    
}
