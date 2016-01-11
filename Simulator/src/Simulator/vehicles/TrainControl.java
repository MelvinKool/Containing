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
 *
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
            if (trainCart.node.getWorldTranslation().x > this.disappearX && trainCart.node.getCullHint() != Spatial.CullHint.Always) {
                trainCart.node.setCullHint(Spatial.CullHint.Always);
            } else if (trainCart.node.getWorldTranslation().x < this.disappearX) {
                trainCart.node.setCullHint(Spatial.CullHint.Inherit);
            }
        }
        
        if (this.train.locomotive.node.getWorldTranslation().x > this.disappearX) {
            this.train.locomotive.node.setCullHint(Spatial.CullHint.Always);
            this.train.canDestroy = true;
        } else if (this.train.locomotive.node.getWorldTranslation().x < this.disappearX) {
            this.train.locomotive.node.setCullHint(Spatial.CullHint.Inherit);
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp)
    {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
