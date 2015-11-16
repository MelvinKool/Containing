package org.nhl.containing.cranes;

import com.jme3.asset.AssetManager;
import org.nhl.containing.areas.BoatArea;

public class DockingCraneSeaShip extends DockingCrane{

    public DockingCraneSeaShip(AssetManager assetManager, BoatArea.AreaType type) {
        super(assetManager, type);
        this.name = "DockingCraneSeaShip";
    }

}



