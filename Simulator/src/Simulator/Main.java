package Simulator;

import Simulator.cranes.Crane;
import Simulator.vehicles.AGV;
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

public class Main extends SimpleApplication
{
    private Node dockCraneNode;
    private List<Container> containers;
    private Connection connection;
    private Thread readThread;
    private List<MotionEvent> motionControls = new ArrayList<MotionEvent>();
    private ObjectLoader objectLoader;
    
    
    boolean playing;

    public static void main(String[] args)
    {
        Main app = new Main();
        app.start();
    }
    
    @Override
    public void simpleInitApp()
    {
        long start = System.currentTimeMillis();
        this.objectLoader = new ObjectLoader(this.rootNode, this.assetManager, this.motionControls);
        long end = System.currentTimeMillis();

        System.out.println(end - start);
        
        this.dockCraneNode = new Node();
        this.playing = false;
        flyCam.setEnabled(true);
        flyCam.setMoveSpeed(200);
        cam.setFrustumFar(2000);
        
        this.containers = new ArrayList<>();
        this.containers.add(new Container(this.rootNode, this.assetManager, this.motionControls, new Vector3f(0, 0, 0), this.objectLoader.getContainerModel()));
        this.containers.get(0).node.rotate(0.0f, (float) Math.PI / 2, 0.0f);
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

    public Crane getNearestCrane(Node obj) {
        float dist;
        float minDist = -1;
        Crane nCrane = null;
        for (Crane crane : this.objectLoader.cranes){
            dist = obj.getLocalTranslation().distance(crane.getPosition());
            if (dist < minDist || minDist == -1) {
                minDist = dist;
                nCrane = crane;
            }
        }
        return nCrane;
    }
    
    private void initInputs() {
        inputManager.addMapping("play_stop", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("target", new KeyTrigger(KeyInput.KEY_T));
        inputManager.addMapping("xp", new KeyTrigger(KeyInput.KEY_I));
        inputManager.addMapping("xm", new KeyTrigger(KeyInput.KEY_K));
        inputManager.addMapping("zp", new KeyTrigger(KeyInput.KEY_L));
        inputManager.addMapping("zm", new KeyTrigger(KeyInput.KEY_J));
        
        ActionListener acl = new ActionListener() {

            public void onAction(String name, boolean keyPressed, float tpf) {
                Container cont = containers.get(0);
                
                if (name.equals("play_stop") && keyPressed) {
                    if (playing) {
                        playing = false;
                        
                    } else {
                        playing = true;
                    }
                } else if (keyPressed) {
                    switch (name) {
                    case "target":
                        Crane crane = getNearestCrane(cont.node);
                        crane.targetContainer(cont);
                        break;
                    case "xp":
                        cont.node.move(1,0,0);
                        break;
                    case "xm":
                        cont.node.move(-1,0,0);
                        break;
                    case "zp":
                        cont.node.move(0,0,1);
                        break;
                    case "zm":
                        cont.node.move(0,0,-1);
                        break;
                    }
                }

            }
        };

        inputManager.addListener(acl, "play_stop");
        inputManager.addListener(acl, "xp");
        inputManager.addListener(acl, "zp");
        inputManager.addListener(acl, "xm");
        inputManager.addListener(acl, "zm");
        inputManager.addListener(acl, "target");
    }
    
    private Thread initReadThread(){
        return new Thread(new Runnable()
        {
            public void run() {
                try
                {
                    connection = new Connection();
                    CommandHandler commandHandler = new CommandHandler(objectLoader);
                    while(true)
                    {
                        //What to do with the input?
                        String input = connection.read();
                        System.out.println(input);
                        commandHandler.ParseJSON(input);
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