package Simulator;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import java.util.ArrayList;
import java.util.List;

public class Main extends SimpleApplication
{
    Object AGV = new Object();
    
    List<Container> containers = new ArrayList<Container>();

    public static void main(String[] args)
    {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        Box b = new Box(1, 1, 1);
        Geometry geom = new Geometry("Box", b);

        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        geom.setMaterial(mat);

        rootNode.attachChild(geom);
        
        this.containers.add(new Container(this.rootNode, this.assetManager, new Vector3f(0, 0, 0)));
    }

    @Override
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
     * @param movement afstand die afgelegd moet worden
     * @param speed snelheid waarmee het object zich beweegt
     * @return
     */
    public float movementTime(int movement,float speed)
    {
        //The AGV always moves at top speed, because reasons
        float tijd = movement/speed;
        return tijd;
    }
}