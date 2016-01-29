/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulator.vehicles;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;

/**
 * Culls parts of the train when they are out of the world bounds
 * @author erwin
 */
public class TrainControl extends AbstractControl implements Cloneable      
{
    private Train train;
    private float disappearX;

    public TrainControl(Train train) {
        this.train = train;
        this.disappearX = 1715.0f;        
    }
    
    @Override
    protected void controlUpdate(float tpf)
    {
        for (TrainCart trainCart : this.train.trainCarts) {
            if (trainCart.node.getWorldTranslation().x > this.disappearX && 
                    trainCart.node.getCullHint() != Spatial.CullHint.Always) 
            {
                trainCart.node.setCullHint(Spatial.CullHint.Always);
            } else if (trainCart.node.getWorldTranslation().x < this.disappearX &&
                    trainCart.node.getCullHint() != Spatial.CullHint.Inherit) 
            {
                trainCart.node.setCullHint(Spatial.CullHint.Inherit);
            }
        }
        
        if (this.train.locomotive.node.getWorldTranslation().x >= this.disappearX &&
                this.train.locomotive.node.getCullHint() != Spatial.CullHint.Always)
        {
            this.train.locomotive.node.setCullHint(Spatial.CullHint.Always);
            this.train.canDestroy = true;
            System.out.println("train can be destroyed");
        } else if (this.train.locomotive.node.getWorldTranslation().x < this.disappearX &&
                this.train.locomotive.node.getCullHint() != Spatial.CullHint.Inherit)
        {
            this.train.locomotive.node.setCullHint(Spatial.CullHint.Inherit);
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) { }
    
}
