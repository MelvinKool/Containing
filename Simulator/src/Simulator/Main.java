package Simulator;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

<<<<<<< HEAD
/**
 * test
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    public static void main(String[] args) {
=======
public class Main extends SimpleApplication
{
    
    Object AGV = new Object();

    public static void main(String[] args)
    {
>>>>>>> 0112b1c6549aff58a63c7bddbf7373d1b5fffea9
        Main app = new Main();
        app.start();
    }

    @Override
<<<<<<< HEAD
    public void simpleInitApp() {
        Box b = new Box(1, 1, 1);
        Geometry geom = new Geometry("Box", b);
=======
    public void simpleInitApp()
    {
        Box b = new Box(1, 1, 1);
        Geometry geom = new Geometry("Box", b);
        
>>>>>>> 0112b1c6549aff58a63c7bddbf7373d1b5fffea9

        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        geom.setMaterial(mat);

        rootNode.attachChild(geom);
    }

    @Override
<<<<<<< HEAD
    public void simpleUpdate(float tpf) {
        //TODO: add update code
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
=======
    public void simpleUpdate(float tpf)
    {
        //TODO Depending on wich way you're going (XYZ) 
        //float afstand = AGV.GetMaxSpeed()*tpf;
        //AGV.SetLocalTranslation(afstand);
        //AGV.afstandToGo -= afstand;
        //This kinda works, but it doesn't, since I don't specify the X, Y or Z
    }

    @Override
    public void simpleRender(RenderManager rm)
    {
        
    }
    
    /**
     *
     * @param verplaatsing afstand die afgelegd moet worden
     * @param snelheid snelheid waarmee het object zich beweegt
     * @return
     */
    public float movementTijd(int verplaatsing,float snelheid)
    {
        //The AGV always moves at top speed, because reasons
        float tijd = verplaatsing/snelheid;
        return tijd;
>>>>>>> 0112b1c6549aff58a63c7bddbf7373d1b5fffea9
    }
}
