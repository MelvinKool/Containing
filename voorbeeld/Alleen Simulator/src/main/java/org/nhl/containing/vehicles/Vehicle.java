package org.nhl.containing.vehicles;

import com.jme3.cinematic.MotionPathListener;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.scene.Node;

public class Vehicle extends Node implements MotionPathListener {

    private int id;
    private int processingMessageId;
    private boolean arrived;
    protected float speed;

    public Vehicle(int id) {
        super();
        this.id = id;
        arrived = false;
    }

    public int getId() {
        return id;
    }

    public boolean isArrived() {
        return arrived;
    }

    public void setArrived(boolean arrived) {
        this.arrived = arrived;
    }
   
    public int getProcessingMessageId() {
        return processingMessageId;
    }

    public void setProcessingMessageId(int processingMessageId) {
        this.processingMessageId = processingMessageId;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public void multiplySpeed(float multiplier)
    {
        speed *= multiplier;
    }

    @Override
    public void onWayPointReach(MotionEvent motionControl, int wayPointIndex) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
