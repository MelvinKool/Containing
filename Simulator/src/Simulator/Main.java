package Simulator;

import com.jme3.app.SimpleApplication;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class Main extends SimpleApplication
{
    Object AGV = new Object();
    Connection connection;
    Thread readThread;
    Thread connectionAlive;

    public static void main(String[] args)
    {
        Main app = new Main();
        app.start();
    }
    
    @Override
    public void simpleInitApp()
    {
        //flyCam.setEnabled(false);
        flyCam.setMoveSpeed(250);
        cam.setFrustumFar(2000);
        
        readThread = initReadThread();
        readThread.start();
        while (true)
        {            
            try
            {
                connectionAlive = connection.connectionThread();
                connectionAlive.start();
                break;
            } 
            catch (Exception e)
            {
            }
        }
        
        
        initLight();
        
        Spatial SimWorld = assetManager.loadModel("Models/world/SimWorld.j3o");
        rootNode.attachChild(SimWorld);
        
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
    
    //This is important to properly close
    //the connection with the server.
    @Override
    public void destroy()
    {
        super.destroy();
        readThread.stop();
        connection.stop();
    }
    
    /**@param verplaatsing afstand die afgelegd moet worden
     * @param snelheid snelheid waarmee het object zich beweegt
     * @return
     */
    public float movementTijd(int verplaatsing,float snelheid)
    {
        //The AGV always moves at top speed, because reasons
        float tijd = verplaatsing/snelheid;
        return tijd;
    }
    
    private Thread initReadThread(){
        return new Thread(new Runnable()
        {
            public void run() {
                try
                {
                    while (true)
                    {                        
                        try 
                        {
                            connection = new Connection();
                            break;
                        } 
                        catch (Exception e) 
                        {
                            System.out.println("Creating connection");
                        }
                    }
                    
                    while(true)
                    {
                        //What to do with the input?
                        System.out.println(connection.read());
                    }
                }
                catch(Exception e)
                {
                    //Always throws a exception after the socket is closed.
                    //System.out.println(e);
                }
            }
        });
    }
    
    private void initLight(){
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection((new Vector3f(-0.5f, -0.5f, -0.5f)).normalizeLocal());
        sun.setColor(ColorRGBA.White);
        rootNode.addLight(sun);
        
        DirectionalLight sun2 = new DirectionalLight();
        sun2.setDirection((new Vector3f(0.5f, -0.5f, 0.5f)).normalizeLocal());
        sun2.setColor(ColorRGBA.White);
        rootNode.addLight(sun2); 
    }
}