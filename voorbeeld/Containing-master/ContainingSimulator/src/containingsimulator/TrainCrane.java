/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingsimulator;

import com.jme3.animation.LoopMode;
import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.MotionPathListener;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *Class of the cranes handling the trains, extends baseclass Crane
 * @author Len
 */
public class TrainCrane extends Crane implements MotionPathListener{
    
    public TrainCrane(String id, Vector3f basePos, Spatial base, Spatial slider, Spatial hook)
    {
        super(id, basePos,base,slider, hook);
        this.base.rotate(0, 90*FastMath.DEG_TO_RAD, 0);
        //this.hook.rotate(0, 90*FastMath.DEG_TO_RAD, 0);
        hNode.setLocalTranslation(new Vector3f(0,12,0));
        
       
        this.defPosHook = this.hNode.getWorldTranslation().clone();
        this.defPosSlider = this.sNode.getWorldTranslation().clone();
        
         this.baseDur = 3f;
        this.sliDur = 5f;
        this.hookDur = 5f;
        this.baseLP = 0.66f; 
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
        switch(action)
        {
            default: 
              commonActions();
                break;
            case 6:
                 if(doAction(2,true))
                 {
                    resetPos(3);
                 }
                break;
            case 7:
                  if(readyToLoad())
                  {
                      target = agv.getWorldTranslation().add(0,cont.size.y*2,0); 
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
    
    @Override
    public ParkingSpot getParkingspot() {
        return new ParkingSpot(parkingVector,(float)Math.PI/2f);
    }
}
