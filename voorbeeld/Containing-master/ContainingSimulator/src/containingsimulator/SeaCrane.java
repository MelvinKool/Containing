/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingsimulator;

import com.jme3.animation.LoopMode;
import com.jme3.asset.AssetManager;
import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.MotionPathListener;
import com.jme3.cinematic.PlayState;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *Class of cranes handling the seaships, extends baseclass Crane
 * @author User
 */
public class SeaCrane extends Crane {

    //motionpaths for moving objects
   
    public SeaCrane(String id, Vector3f basePos, Spatial base, Spatial slider, Spatial hook) {
        
        super(id, basePos, base,slider, hook);
        hNode.setLocalTranslation(new Vector3f(0,25,0));
        
        this.defPosHook = hNode.getWorldTranslation().clone();
        this.defPosSlider = sNode.getWorldTranslation().clone();
        
        this.baseDur = 1.1f;
        this.sliDur = 1.1f;
        this.hookDur = 3f;
        
        //50 percent of default speed
      
        
        
        
    }

   @Override
    protected void updateGet()
    {
         switch (action) {
             default:
                 commonActions();
                break;
            case 6:
                if(readyToLoad())
                {
                    if(doAction(2,false))
                    {
                       resetPos(3);

                    }
                }
                break;
            case 7:
                doAction(3,false);
                break;
            case 8:
                if(!waitingOnTimer)
                {
                    setTimer(detachDur);
                }
                break;
           case 9:
                if(doAction(3,true))
                {
                   contOffHook();
                }
                break; 
            case 10:
                if(doAction(2,true)){
                    resetPos(3);
                }
                break;
            case 11:
             this.finishActions();
                break;
         }
    }

    @Override
    protected void updatePickup() 
    {
        switch (action) {
            default:
               commonActions();
                break;
            case 6:
                if (!doAction(2,true))
                {
                    resetPos(3);
                }
                break;
            case 7:
                if(readyToLoad())
                {
                    if (doAction(3,false))
                    {
                    resetPos(2);
                    }
                }
                break;
            case 8:
                if(!waitingOnTimer)
                {
                    setTimer(detachDur);
                }
                break;
            case 9:
                if(doAction(3,true))
                {
                    contOffHook();
                }
                break;
            case 10:
                 resetPos(2);
                 resetPos(3);
                 finishActions();
                break;
        }
    }


    public ParkingSpot getParkingspot() {
        return new ParkingSpot(parkingVector, (float) Math.PI);
    }
}
