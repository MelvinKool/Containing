/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingsimulator;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/**
 *Class of the Cranes which belong to a buffer, extends baseclass Crane
 * @author User
 */
public class BufferCrane extends Crane {
    
    private Buffer buffer;
    private Vector3f tempTarget;

    /**
     * BufferCrane constructor
     * @param id crane ID
     * @param basePos position
     * @param base base spatial
     * @param slider slider spatial
     * @param hook hook spatial
     * @param buffer buffer link
     */
    public BufferCrane(String id, Vector3f basePos, Spatial base, Spatial slider, Spatial hook, Buffer buffer)
    {
        super(id, basePos,base,slider, hook);
        this.buffer = buffer;
        hNode.setLocalTranslation(new Vector3f(0,18.5f,0));
        this.hook.rotate(0, 90*FastMath.DEG_TO_RAD, 0);
        this.slider.rotate(0,90*FastMath.DEG_TO_RAD,0);
        this.defPosHook = hNode.getWorldTranslation().clone();
        this.defPosSlider = sNode.getWorldTranslation().clone();
        
        this.baseDur = 3f;
        this.sliDur = 5f;
        this.hookDur = 3f;
        this.baseLP = 0.66f; 
    }
    /**
     * @return buffer
     */
    public Buffer getBuffer()
    {
        return this.buffer;
    }



     @Override
    protected void updateGet()
    {
         switch(action)
        {
             default:
                 commonActions();
                 break;
            case 6:
                  if(readyToLoad())
                  {
                    if (doAction(1,false))
                    {  
                        resetPos(3);
                    }
                  }
                break;
            case 7:
                doAction(2,false);
                break;
            case 8:
                doAction(3,false);
                break;
            case 9:
                if(doAction(3,true))
                {
                   this.buffer.addContainer(cont.indexPosition, cont);
                   this.hNode.detachChild(cont);
                }
                break; 
            case 10:
                resetPos(2);
                resetPos(3);
                finishActions();
                break;
         }
    }
     /**
     *
     * @param cont
     * @param up
     */
    public void pickupContainer(Container cont, boolean up)
     {
         this.tempTarget = buffer.getSideVector3f(up);
         this.pickupContainer(cont);
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
                doAction(2,true);
                break;
            case 7:
                target = tempTarget; //target is parkingSpot up or down
                doAction(1,false);
                break;
            case 8: //nog niet af vanaf hier
                  if(readyToLoad())
                  {
                    doAction(3,false);
                  }
                break;
            case 9:
                if(!waitingOnTimer)
                {
                    setTimer(detachDur);
                }
                break;
            case 10:
                if(doAction(3,true))
                {
                contOffHook(); //container to agv
                }
                break;
            case 11:
                resetPos(2);
                resetPos(3);
                finishActions();
                break;
        }
    }
    
    @Override
    public ParkingSpot getParkingspot() {
        return buffer.getBestParkingSpot();
    }
}

