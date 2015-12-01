/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingsimulator;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/**
 * Class of cranes handling the bargeships, extends baseclass Crane
 * @author Len
 */
public class BargeCrane extends Crane { 
    
    /**
     * BargeCrane constructor
     * @param id crane ID
     * @param basePos position
     * @param base base spatial
     * @param slider slider spatial
     * @param hook hook spatial
     */
    public BargeCrane(String id, Vector3f basePos, Spatial base, Spatial slider, Spatial hook)
    {
        super(id,basePos,base,slider,hook);
        this.base.rotate(0, 90*FastMath.DEG_TO_RAD, 0);
        this.hook.rotate(0, 90*FastMath.DEG_TO_RAD, 0);
        this.slider.rotate(0, 90*FastMath.DEG_TO_RAD, 0);
        this.hNode.setLocalTranslation(new Vector3f(0,25,0));
        
        defPosHook = this.hNode.getWorldTranslation().clone();
        defPosSlider = this.sNode.getWorldTranslation().clone();
        
        this.baseDur = 2.2f;
        this.sliDur = 5f;
        this.hookDur = 3f;
        this.baseLP = 0.375f;
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
                finishActions();
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
                   
                    if(doAction(3,false))
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
                 if (doAction(3,true))
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

