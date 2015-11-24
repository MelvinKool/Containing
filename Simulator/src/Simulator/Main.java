package Simulator;

import Simulator.cranes.DockCrane;
import com.jme3.app.SimpleApplication;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main extends SimpleApplication
{
    Object AGV = new Object();
    public Node dockCraneNode;
    DockCrane dockCrane1;
    Connection connection;
    Thread readThread;
    List<MotionEvent> motionControls = new ArrayList<MotionEvent>();
    
    boolean playing;

    public static void main(String[] args)
    {
        Main app = new Main();
        app.start();
    }
    
    @Override
    public void simpleInitApp()
    {
        this.dockCraneNode = new Node();
        this.dockCrane1 = new DockCrane(this.dockCraneNode, this.assetManager, this.motionControls, new Vector3f(0,0,0));
        this.playing = false;
        flyCam.setEnabled(true);
        flyCam.setMoveSpeed(250);
        cam.setFrustumFar(2000);
        
        readThread = initReadThread();
        readThread.start();
        
        initLight();
        initInputs();
        
        Spatial SimWorld = assetManager.loadModel("Models/world/SimWorld.j3o");
        rootNode.attachChild(SimWorld);
        rootNode.attachChild(this.dockCraneNode);
        
        
    }
    
    boolean test = false;
    @Override
    public void simpleUpdate(float tpf)
    {
        if (this.test == false) {
            this.test = true;
            this.dockCrane1.setTarget(new Vector3f(2, 3, 4));
        }
        
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
    
    private void initInputs() {
        inputManager.addMapping("play_stop", new KeyTrigger(KeyInput.KEY_SPACE));
        ActionListener acl = new ActionListener() {

            public void onAction(String name, boolean keyPressed, float tpf) {
                if (name.equals("play_stop") && keyPressed) {
                    if (playing) {
                        playing = false;
                        for (MotionEvent motionControl : motionControls) {
                            motionControl.play();                            
                        }
                    } else {
                        playing = true;
                        for (MotionEvent motionControl : motionControls) {
                            motionControl.play();                            
                        }
                    }
                }

            }
        };

        inputManager.addListener(acl, "play_stop");

    }
    
    private Thread initReadThread(){
        return new Thread(new Runnable()
        {
            public void run() {
                try
                {
                    connection = new Connection();
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