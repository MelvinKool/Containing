/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulator.cranes;

import Simulator.WorldObject;
import com.jme3.asset.AssetManager;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 *
 * @author erwin
 */
class Hook extends WorldObject {
    
    public static final int RIGHT_HOOK = 0;
    public static final int LEFT_HOOK = 1;
    
    private Quaternion openRotation;
    private Quaternion closeRotation;
    
    public Hook(Node rootNode, AssetManager assetManager, Vector3f position, String modelFile, int hookType)
    {
        super(rootNode, assetManager, position, modelFile);
        //this.initRotations(hookType);
    }
    
    public void open() 
    {
        //this.node.setLocalRotation(this.openRotation);
    }
    
    public void close() 
    {
        //this.node.setLocalRotation(this.closeRotation);
    }

    private void initRotations(int hookType)
    {
        float rotation = FastMath.PI / 4;
        
        if (hookType == LEFT_HOOK) 
        {
            rotation = -rotation;
        }
        
        this.openRotation = new Quaternion();
        this.closeRotation = Quaternion.ZERO;
        
        this.openRotation.fromAngleNormalAxis(rotation, Vector3f.UNIT_X);
        this.closeRotation.fromAngleNormalAxis(-rotation, Vector3f.UNIT_X);
    }
}
