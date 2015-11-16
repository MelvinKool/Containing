package org.nhl.containing.areas;

import com.jme3.asset.AssetManager;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import org.nhl.containing.cranes.TrainCrane;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jeroen
 */
public class TrainArea extends Area {

    public static final Quaternion YAW090 = new Quaternion().fromAngleAxis(FastMath.PI / 2, new Vector3f(0, 1, 0));
    public List<TrainCrane> trainCranes = new ArrayList();
    public TrainCrane trainCrane;
    public TrainCrane secondTrainCrane;
    private AssetManager assetManager;
    private int craneXAxis = 0;
    private int trainRailsXAxis = -30;
    private int craneRailsXAxis = -30;
    private int cranes;

    public TrainArea(AssetManager assetManager, int cranes) {
        this.assetManager = assetManager;
        this.cranes = cranes;
        initTrainArea();
    }

    /**
     * Initialize a train area.
     */
    private void initTrainArea() {

        // Add traincranes to the list and scene.
        for (int i = 0; i < cranes; i++) {
            trainCranes.add(new TrainCrane(assetManager));
            trainCranes.get(i).setLocalTranslation(craneXAxis, 0, 0);
            trainCranes.get(i).setId(i);
            this.attachChild(trainCranes.get(i));
            craneXAxis += 30;
        }

        // Add trainRails.
        Spatial railTrack = assetManager.loadModel("Models/rails/trainRails.j3o");
        railTrack.setLocalRotation(YAW090);

        for (int i = 0; i < 72; i++) {
            Spatial nextRail = railTrack.clone();
            nextRail.setLocalTranslation(trainRailsXAxis, 0, 0);
            this.attachChild(nextRail);
            trainRailsXAxis += 11;
        }

        // Add craneRails.
        Spatial craneRails = assetManager.loadModel("Models/rails/craneRails.j3o");
        craneRails.setLocalRotation(YAW090);

        for (int i = 0; i < 72; i++) {
            Spatial nextRail = craneRails.clone();
            nextRail.setLocalTranslation(craneRailsXAxis, 0, 0);
            this.attachChild(nextRail);
            craneRailsXAxis += 11;
        }

    }
    
    public List<TrainCrane> getTrainCranes(){
        return trainCranes;
    }
}