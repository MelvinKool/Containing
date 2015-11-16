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
 * Class of the lorry crane, extends baseclass Crane
 * @author Len
 */
public class LorryCrane extends Crane implements MotionPathListener {

    /**
     *ParkingSpot of this crane, position for AGV to wait
     */
    public ParkingSpot parkingSpot;
    
    /**
     * Constructor
     * @param id
     * @param pos
     * @param base
     * @param slider
     * @param hook
     */
    public LorryCrane(String id, Vector3f pos, Spatial base, Spatial slider, Spatial hook) 
    {
        super(id,pos,base,slider.clone().scale(0.4f),hook);
        sNode.setLocalTranslation(new Vector3f(0, -10, 0));
        this.slider.setLocalTranslation(new Vector3f(0, 6.5f, 0));
        this.hNode.setLocalTranslation(new Vector3f(0, 16.5f, 0));
      
        this.parkingSpot = new ParkingSpot(new Vector3f(pos.x,pos.y,pos.z-25f),0f);
        this.defPosHook = this.hNode.getLocalTranslation().clone();
        this.defPosSlider = this.sNode.getWorldTranslation().clone();
        
        this.baseDur = 5.5f;
        this.sliDur = 5f;
        this.hookDur = 5f;
        this.baseLP = 0.75f;
        
    }
    
    //used steps by both possible actions (pickup/get container)
    private void commonSteps()
    {
        switch(action)
        {
            case 1:
               doAction(1,false);;
                break;
            case 2:
                  target = target.add(new Vector3f(0,cont.getLocalTranslation().y/2,0));
                  doAction(3,false);
                break;
            case 3:
                if(!waitingOnTimer)
                {
                    setTimer(attachDur);
                }
                break;
            case 4:
                if(doAction(3,true))
                { contToHook();
                    cont.setLocalTranslation(cont.getLocalTranslation().subtract(0,1.5f,0));
                }
                break;
        }
    }
    
    @Override
    protected void updateGet()
    {
        switch(action)
        {
            default:
               commonSteps();
                break;
            case 5:
                if(readyToLoad())
                {
                doAction(1,false);    
                }
                break;
            case 6:
                doAction(3,false);
                    //cont.setLocalTranslation(cont.getLocalTranslation().subtract(0,1.5f,0));
                break;
                case 7:
                if(!waitingOnTimer)
                {
                    setTimer(detachDur);
                }
                break;
            case 8:
                 if(cont != null){ 
                target =target.add(0,cont.size.y*4,0);}
                if(doAction(3,true))
                {
                   contOffHook();
                }
                break;
            case 9:
                doAction(1,true);
                break;
            case 10:
                
                resetPos(2);
                resetPos(3);
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
                commonSteps();
                break;
            case 5:
                target = parkingSpot.translation;
                if(doAction(1,false))
                { 
                   resetPos(3);
                }
                break;
            case 6:
                if(agv!=null && cont != null){ 
                target = agv.getWorldTranslation().add(0,cont.size.y*4,0);}
                 if(readyToLoad())
                 {
                     doAction(3,false);
                 }
                break;
            case 7:
                if(!waitingOnTimer)
                {
                    setTimer(detachDur);
                }
                break;
            case 8:
                 if (doAction(3,true))
                    {  
                      contOffHook();
                    }
                break;
            case 9:
                doAction(1,true);
                break;
            case 10:
                resetPos(3);
                this.finishActions();
                break;
        }
    }

    //returns parkingSpot
    @Override
    public ParkingSpot getParkingspot() {
        return this.parkingSpot;
    }
}
