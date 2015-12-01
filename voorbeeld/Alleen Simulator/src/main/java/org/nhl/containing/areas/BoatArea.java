package org.nhl.containing.areas;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Spatial;
import org.nhl.containing.cranes.DockingCrane;

import java.util.ArrayList;
import java.util.List;
import org.nhl.containing.cranes.DockingCraneInlandShip;
import org.nhl.containing.cranes.DockingCraneSeaShip;

/**
 * @author Jeroen
 */
public class BoatArea extends Area {

    private List<DockingCrane> dockingCranes = new ArrayList();
    private AssetManager assetManager;
    private int craneAxis = 0;
    private int craneRailsAxis = -30;
    private int cranes;
    private int rails;
    private BoatArea.AreaType type;

    public BoatArea(AssetManager assetmanager, AreaType type, int cranes, int rails) {
        this.assetManager = assetmanager;
        this.type = type;
        this.cranes = cranes;
        this.rails = rails;
        initBoatArea();
    }

    /**
     * Initialize a boat area.
     */
    private void initBoatArea() {
        Spatial craneRails = assetManager.loadModel("Models/rails/craneRails.j3o");
        switch (type) {
            case SEASHIP:
                // Add docking cranes to the list and scene.
                for (int i = 0; i < cranes; i++) {
                    dockingCranes.add(new DockingCraneSeaShip(assetManager, this.type));
                    dockingCranes.get(i).setLocalTranslation(0, 0, craneAxis);
                    dockingCranes.get(i).setId(i);
                    dockingCranes.get(i).setName("DockingCraneSeaShip");
                    this.attachChild(dockingCranes.get(i));
                    craneAxis += 18;
                }

                // Add crane rails.

                for (int i = 0; i < rails; i++) {
                    Spatial nextRail = craneRails.clone();
                    nextRail.setLocalTranslation(42f, 0, craneRailsAxis);
                    nextRail.setLocalScale(0.89f, 1, 1);
                    this.attachChild(nextRail);
                    craneRailsAxis += 11;
                }
                break;
            case INLANDSHIP:
                // Add docking cranes to the list and scene.
                for (int i = 0; i < cranes; i++) {
                    dockingCranes.add(new DockingCraneInlandShip(assetManager, this.type));
                    dockingCranes.get(i).setLocalTranslation(craneAxis, 0, 0);
                    dockingCranes.get(i).rotate(0, (float) Math.PI / 2, 0);
                    dockingCranes.get(i).setId(i);
                    dockingCranes.get(i).setName("DockingCraneInlandShip");
                    this.attachChild(dockingCranes.get(i));
                    craneAxis += 18;
                }

                // Add crane rails.

                for (int i = 0; i < rails; i++) {
                    Spatial nextRail = craneRails.clone();
                    nextRail.setLocalTranslation(craneRailsAxis, 0, -42f);
                    nextRail.rotate(0, (float) Math.PI / 2, 0);
                    nextRail.setLocalScale(0.89f, 1, 1);
                    this.attachChild(nextRail);
                    craneRailsAxis += 11;
                }
                break;
        }
    }

    public static enum AreaType {

        INLANDSHIP, SEASHIP
    }

    public List<DockingCrane> getDockingCranes() {
        return dockingCranes;
    }
}
