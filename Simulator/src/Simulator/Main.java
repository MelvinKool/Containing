package Simulator;

import Simulator.cranes.Crane;
import Simulator.vehicles.Train;
import com.jme3.app.SimpleApplication;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Plane;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.FogFilter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Quad;
import com.jme3.util.SkyFactory;
import com.jme3.water.SimpleWaterProcessor;
import java.util.ArrayList;
import java.util.List;

public class Main extends SimpleApplication
{
    boolean connected = false;
    private Spatial SimWorld;    
    private Node dockCraneNode;
    private List<Container> containers;
    private Connection connection;
    private Thread readThread;
    private Thread connectionAlive;
    private List<MotionEvent> motionControls = new ArrayList<MotionEvent>();
    private List<Vector3f> locations = new ArrayList<>();
    private ObjectLoader worldObjects;
    
    boolean playing;
    
    private Train train; // TODO: this is test code

    public static void main(String[] args)
    {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp()
    {
        long start = System.currentTimeMillis();
        this.worldObjects = new ObjectLoader(this.rootNode, this.assetManager, this.motionControls);
        long end = System.currentTimeMillis();

        System.out.println(end - start);

        this.dockCraneNode = new Node();
        this.playing = false;
        flyCam.setEnabled(true);
        flyCam.setMoveSpeed(200);
        cam.setFrustumFar(3000);
        
        this.train = new Train(80, this.rootNode, this.assetManager, this.worldObjects.getLocomotiveModel(), this.worldObjects.getTrainCartModel());
        
        this.containers = new ArrayList<>();
        this.containers.add(new Container(this.rootNode, this.assetManager, this.motionControls, new Vector3f(75, 0, 35), this.worldObjects.getContainerModel()));
        this.containers.add(new Container(this.rootNode, this.assetManager, this.motionControls, new Vector3f(235, 0, -200), this.worldObjects.getContainerModel()));
        this.containers.add(new Container(this.rootNode, this.assetManager, this.motionControls, new Vector3f(0, 0, 0), this.worldObjects.getContainerModel()));

        this.containers.get(0).node.rotate(0.0f, (float) Math.PI / 2, 0.0f);
        readThread = initReadThread();
        readThread.start();
        Thread t = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                while (true)
                {            
                    try
                    {
                        if(connected)
                        {
                            connectionAlive = connection.connectionThread();
                            connectionAlive.start();
                            break;
                        }
                        Thread.sleep(1000);
                    } 
                    catch (Exception e)
                    {
                    }
                }
            }
        });
        t.start();
        
        initWorld();
        initLight();
        initWater();
        initSkybox();
        initFog();
        initInputs();
    }
    
    public void TeleAgv(){
        //worldObjects.agvs.get(2).node.move(0,0,9.75f);
//        locations.add(new Vector3f(113.75f, 0.0f, -63.75f));
//        locations.add(new Vector3f(38.75f, 0.0f, -63.75f));
//        locations.add(new Vector3f(38.75f, 0.0f, -49.5f));
//        locations.add(new Vector3f(100.00f, 0.0f, -49.5f));
        locations.add(new Vector3f(100f, 0.0f, -50f));
        locations.add(new Vector3f(100f, 0.0f, -60f));
//        locations.add(new Vector3f(90f, 0.0f, -40f));
//        locations.add(new Vector3f(80f, 0.0f, -40f));
//        System.out.println(locations);
    }
    public void MoveAgv(){
        worldObjects.agvs.get(2).setPath(locations,2000f);
    }
    
    boolean test = false; //TODO: remove this line
    @Override
    public void simpleUpdate(float tpf)
    {
        if (this.test == false) {
            train.moveIn();
            this.test = true;
        }
        
        // Destroy train when it says it can (when it's out of view)
        if (this.train != null && this.train.canDestroy) {
            this.train = null;
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
        if(connectionAlive!= null)
        {
            connectionAlive.stop();    
        }
        readThread.stop();
        if(connection != null)
        {
            connection.stop();
        }
        //Dirty as fuck, fix it later...
        Runtime.getRuntime().exit(1);
    }

    public Crane getNearestCrane(Node obj)
    {
        float dist;
        float minDist = -1;
        Crane nCrane = null;
        for (Crane crane : this.worldObjects.cranes.values())
        {
            dist = obj.getLocalTranslation().distance(crane.getPosition());
            if (dist < minDist || minDist == -1)
            {
                minDist = dist;
                nCrane = crane;
            }
        }
        return nCrane;
    }
    
    private void initInputs()
    {
        inputManager.addMapping("play_stop", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("target", new KeyTrigger(KeyInput.KEY_T));
        inputManager.addMapping("target2", new KeyTrigger(KeyInput.KEY_Y));

        inputManager.addMapping("xp", new KeyTrigger(KeyInput.KEY_I));
        inputManager.addMapping("xm", new KeyTrigger(KeyInput.KEY_K));
        inputManager.addMapping("zp", new KeyTrigger(KeyInput.KEY_L));
        inputManager.addMapping("zm", new KeyTrigger(KeyInput.KEY_J));
        
        ActionListener acl = new ActionListener()
        {
            public void onAction(String name, boolean keyPressed, float tpf)
            {
                Container cont = containers.get(0);
                Container cont2 = containers.get(1);
                Container cont3 = containers.get(2);
                if(name.equals("play_stop") && keyPressed)
                {
                    if (playing) {
                        playing = false;
                        
                    } else {
                        playing = true;
                    }
                } else if (keyPressed) {
                    switch (name) {
                    case "target":
                        Crane crane = getNearestCrane(cont.node);
                        crane.moveContainer(cont, new Vector3f(55,0,-10));
                        break;
                    case "target2":
                        Crane crane2 = getNearestCrane(cont2.node);
                        crane2.moveContainer(cont2, new Vector3f(235, 0.0f, -100));
                        break;
                    case "xp":
                        cont.node.move(5,0,0);
//                        System.out.println(worldObjects.agvs.get(2).node.getLocalTranslation());
                        break;
                    case "xm":
                        //cont.node.move(-5,0,0);
                        if(!locations.isEmpty())
                        {
                            MoveAgv();
                        }
                        cont.node.move(-5,0,0);
                        break;
                    case "zp":
                        cont.node.move(0,0,5);
                        break;
                    case "zm":
                        cont.node.move(0,0,-5);
//                        TeleAgv();
                        //cont.node.move(0,0,-5);
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
        inputManager.addListener(acl, "target2");
    }
    
    private Thread initReadThread()
    {
        return new Thread(new Runnable()
        {
            public void run()
            {
                try
                {
                    //while (true)
                    //{                        
                    //    try 
                    //    {
                    //        Thread.sleep(5000);
                    //        connection = new Connection();
                    //        connected = true;
                    //        break;
                    //    } 
                    //    catch (Exception e) 
                    //    {
                    //        System.out.println("Creating connection");
                    //    }
                    //}
                    connection = new Connection();
                    CommandHandler commandHandler = new CommandHandler(worldObjects);
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
    
    private void initLight()
    {
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection((new Vector3f(-0.5f, -0.5f, -0.5f)).normalizeLocal());
        sun.setColor(ColorRGBA.White);
        rootNode.addLight(sun);
        
        DirectionalLight sun2 = new DirectionalLight();
        sun2.setDirection((new Vector3f(0.5f, -0.5f, 0.5f)).normalizeLocal());
        sun2.setColor(ColorRGBA.White);
        rootNode.addLight(sun2); 
    }
    private void initWater()
    {
        // we create a water processor
        SimpleWaterProcessor waterProcessor = new SimpleWaterProcessor(assetManager);
        waterProcessor.setReflectionScene(SimWorld);

        // we set the water plane
        Vector3f waterLocation=new Vector3f(0,-6,0);
        waterProcessor.setPlane(new Plane(Vector3f.UNIT_Y, waterLocation.dot(Vector3f.UNIT_Y)));
        viewPort.addProcessor(waterProcessor);

        // we set wave properties
        waterProcessor.setWaterDepth(40);         // transparency of water
        waterProcessor.setDistortionScale(0.05f); // strength of waves
        waterProcessor.setWaveSpeed(0.05f);       // speed of waves

        // we define the wave size by setting the size of the texture coordinates
        Quad quad = new Quad(5000,8000);
        quad.scaleTextureCoordinates(new Vector2f(6f,6f));

        // we create the water geometry from the quad
        Geometry water=new Geometry("water", quad);
        water.setLocalRotation(new Quaternion().fromAngleAxis(-FastMath.HALF_PI, Vector3f.UNIT_X));
        water.setLocalTranslation(-1000, -6, 3000);
        water.setShadowMode(ShadowMode.Receive);
        water.setMaterial(waterProcessor.getMaterial());
        rootNode.attachChild(water);
    }
    
    private void initWorld()
    {
        //Load World and attach to scene.
        SimWorld = assetManager.loadModel("Models/world/SimWorld.j3o");
        rootNode.attachChild(SimWorld);
        rootNode.attachChild(this.dockCraneNode);
    }
    
    private void initSkybox()
    {
        //Adds Skybox to the scene.
        rootNode.attachChild(SkyFactory.createSky(assetManager, "Models/BrightSky.dds", false));
    }
    
    private void initFog()
    {
        //Add fog to a scene
        FilterPostProcessor fpp=new FilterPostProcessor(assetManager);
        FogFilter fog=new FogFilter();
        fog.setFogColor(new ColorRGBA(0.9f, 0.9f, 0.9f, 0.5f));
        fog.setFogDistance(8000);
        fog.setFogDensity(1.5f);
        fpp.addFilter(fog);
        viewPort.addProcessor(fpp);
    }
}