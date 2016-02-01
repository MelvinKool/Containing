/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulator;

import com.jme3.math.Vector3f;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;

/**
 *
 * @author erwin
 */
public class TrainParking
{
    private HashMap<Integer, Boolean> spots = new HashMap<>();
    private Vector3f position;
    private float agvLength;
    
    public TrainParking(Vector3f position, float agvLength, int spotCount) {
        this.position = position;
        this.agvLength = agvLength;
        this.initSpots(spotCount);
    }
    
    private void initSpots(int spotCount) {
        for (int i = 0; i < spotCount; i++) {
            this.spots.put(i, true);
        }
    }
    
    public SimpleEntry<Integer, Vector3f> getFirstFreeSpot()
    {
        int spot = 0;
        Vector3f spotPos = this.position.clone();
        
        for (int checkSpot : this.spots.keySet()) {
            if (this.spots.get(checkSpot)) {
                spot = checkSpot;
                break;
            }
        }
        
        spotPos.add(agvLength * spot, 0.0f, 0.0f);
        
        return new SimpleEntry<>(spot, spotPos);
    }
    
    public void setSpot(int spot, boolean free) {
        this.spots.put(spot, free);
    }
    
    public Vector3f getPosition() {
        return this.position;
    }
    
 }
