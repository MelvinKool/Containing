package Simulator;

import Simulator.cranes.Crane;
import com.jme3.app.SimpleApplication;
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
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Quad;
import com.jme3.util.SkyFactory;
import com.jme3.water.SimpleWaterProcessor;

public class Main extends SimpleApplication
{
    boolean connected = false;
    private Spatial SimWorld;
    private Connection connection;
    private ObjectLoader objectLoader;
    private CommandHandler commandHandler;

//    private Train train; // TODO: this is test code

    public static void main(String[] args)
    {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp()
    {
        this.objectLoader = new ObjectLoader(this.rootNode, this.assetManager);
        this.commandHandler = new CommandHandler(this.objectLoader);
        this.speed = 20;
        this.setDisplayStatView(false);

        flyCam.setEnabled(true);    //flycam lets you move the camera with wasd and mouse.
        flyCam.setMoveSpeed(200);   //set the movespeed if the flycam is enabled
        cam.setFrustumFar(3000);    //sets render distance of the scene
        this.setPauseOnLostFocus(false); // don't pause automatically on lost focus
        
        //Initialize the world with light, water, skybox and fog.
        initWorld();
        initLight();
//        initInputs();
        initWater();
        initSkybox();
        initFog();
        
        try 
        { 
            connection = new Connection("127.0.0.1", 1337, this.objectLoader, commandHandler);
        }
        catch (Exception e) { System.out.println(e); }
    }
    
//    public String readJsonFile() throws FileNotFoundException, IOException{
//        BufferedReader br = new BufferedReader(new FileReader("assets\\data\\spawns.json"));
//        String temp = "";
//        try {
//            StringBuilder sb = new StringBuilder();
//            String line = br.readLine();
//
//            while (line != null) {
//                temp += line;
//                line = br.readLine();
//            }
//            String everything = sb.toString();
//        } finally {
//            br.close();
//        }
//        return temp;
//    }
//    
//    boolean test = false;
//    // TODO: romove this function
//    private int[] rangeArray(int b, int e) {
//        int[] array = new int[e - b];
//        for (int i = 0; i < array.length; i++) {
//            array[i] = b++;
//        }
//        return array;
//    }
    
    @Override
    public void simpleUpdate(float tpf)
    {
//        if (test) {
//            test = false;
//            this.objectLoader.spawnSeaShip(new JSONArray(this.rangeArray(0, 1400)), commandHandler);
//        }
        // Destroy train when it says it can (when it's out of map)
        if (this.objectLoader.train != null && this.objectLoader.train.canDestroy) {
            this.objectLoader.train.node.removeFromParent();
            this.objectLoader.train = null;
        }
        
        this.commandHandler.executeQueued();
        
        for (Crane crane : this.objectLoader.cranes.values()) {
            crane.executeQueued();
        }
    }

    @Override
    public void simpleRender(RenderManager rm)
    {
        
    }
    
    //This is important to properly close the connection
    //with the server when you try to close the simulator.
    @Override
    public void destroy()
    {
        super.destroy();
        if(connection != null) connection.interrupt();
    }

//   public Crane getNearestCrane(Node obj){
////        float dist;
////        float minDist = -1;
//        Crane nCrane = null;
////        for (Crane crane : this.objectLoader.cranes.values())
////        {
////            dist = obj.getLocalTranslation().distance(crane.getPosition());
////            if (dist < minDist || minDist == -1)
////            {
////                minDist = dist;
////                nCrane = crane;
////            }
////        }
//        return nCrane;
//   }
//   
//    private void initInputs(){
//        inputManager.addMapping("play_stop", new KeyTrigger(KeyInput.KEY_SPACE));
//        inputManager.addMapping("target", new KeyTrigger(KeyInput.KEY_T));
//        inputManager.addMapping("target2", new KeyTrigger(KeyInput.KEY_Y));
//
//        inputManager.addMapping("xp", new KeyTrigger(KeyInput.KEY_I));
//        inputManager.addMapping("xm", new KeyTrigger(KeyInput.KEY_K));
//        inputManager.addMapping("zp", new KeyTrigger(KeyInput.KEY_L));
//        inputManager.addMapping("zm", new KeyTrigger(KeyInput.KEY_J));
//        
//        ActionListener acl = new ActionListener()
//        {
//            public void onAction(String name, boolean keyPressed, float tpf)
//            {
////                Container cont = containers.get(0);
////                Container cont2 = containers.get(1);
////                Container cont3 = containers.get(2);
////                if(name.equals("play_stop") && keyPressed)
////                {
////                    if (playing) {
////                        playing = false;
////                        
////                    } else {
////                        playing = true;
////                    }
//                //else if
//                if (keyPressed) {
//                    switch (name) {
//                    case "target":
////                        Crane crane = getNearestCrane(cont.node);
////                        crane.moveContainer(cont, new Vector3f(55,0,-10));
//                        break;
//                    case "target2":
////                        Crane crane2 = getNearestCrane(cont2.node);
////                        crane2.moveContainer(cont2, new Vector3f(235, 0.0f, -100));
//                        break;
//                    case "xp":
//                        break;
//                    case "xm":
//                        break;
//                    case "zp":
//                        commandHandler.queueCommand(commandHandler.ParseJSON("{'Command': 'moveTo', 'vehicleId': 1, 'Route': [[835.75, 0.0, -51.5], [793.75, 0.0, -51.5], [793.75, 0.0, -73.5]], 'totalDistance': 1000}"));
//                            
//                        break;
//                    case "zm":
//                        Container container = objectLoader.addContainer(1, commandHandler);
//                        
//                        
//                        try 
//                        {
//                            commandHandler.executeCommand(commandHandler.ParseJSON(readJsonFile()));                            
//                        }
//                        catch (FileNotFoundException ex) {
//                            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//                        }
//                        catch (IOException ex) {
//                            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//                        }
//                        objectLoader.agvs.get(1).attachContainer(container);
//                        break;
//                    }
//                }
//            }
//        };
//
//        inputManager.addListener(acl, "play_stop");
//        inputManager.addListener(acl, "xp");
//        inputManager.addListener(acl, "zp");
//        inputManager.addListener(acl, "xm");
//        inputManager.addListener(acl, "zm");
//        inputManager.addListener(acl, "target");
//        inputManager.addListener(acl, "target2");
//   }
    
    private void initLight()
    {
        //Light from right back
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection((new Vector3f(-0.5f, -0.5f, -0.5f)).normalizeLocal());
        sun.setColor(ColorRGBA.White);
        rootNode.addLight(sun);
        //Light from left front
        DirectionalLight sun2 = new DirectionalLight();
        sun2.setDirection((new Vector3f(0.5f, -0.5f, 0.5f)).normalizeLocal());
        sun2.setColor(ColorRGBA.White);
        rootNode.addLight(sun2); 
    }
    private void initWater()
    {
        //create a water processor
        SimpleWaterProcessor waterProcessor = new SimpleWaterProcessor(assetManager);
        waterProcessor.setReflectionScene(SimWorld);

        //set the water plane
        Vector3f waterLocation=new Vector3f(0,-6,0);
        waterProcessor.setPlane(new Plane(Vector3f.UNIT_Y, waterLocation.dot(Vector3f.UNIT_Y)));
        viewPort.addProcessor(waterProcessor);

        //set wave properties
        waterProcessor.setWaterDepth(40);         // transparency of water
        waterProcessor.setDistortionScale(0.05f); // strength of waves
        waterProcessor.setWaveSpeed(0.05f);       // speed of waves

        //define the wave size by setting the size of the texture coordinates
        Quad quad = new Quad(5000,8000);
        quad.scaleTextureCoordinates(new Vector2f(6f,6f));

        //create the water geometry from the quad
        Geometry water=new Geometry("water", quad);
        water.setLocalRotation(new Quaternion().fromAngleAxis(-FastMath.HALF_PI, Vector3f.UNIT_X));
        water.setLocalTranslation(-1000, -6, 3000);
        water.setShadowMode(ShadowMode.Receive);
        water.setMaterial(waterProcessor.getMaterial());
        rootNode.attachChild(water);
    }
    
    private void initWorld()
    {
        //Load world and attach to scene.
        SimWorld = assetManager.loadModel("Models/world/SimWorld.j3o");
        rootNode.attachChild(SimWorld);
    }
    
    private void initSkybox()
    {
        //Adds Skybox to the scene.
        rootNode.attachChild(SkyFactory.createSky(assetManager, "Models/BrightSky.dds", false));
    }
    
    private void initFog()
    {
        //Add fog to the scene
        FilterPostProcessor fpp=new FilterPostProcessor(assetManager);
        FogFilter fog=new FogFilter();
        fog.setFogColor(new ColorRGBA(0.9f, 0.9f, 0.9f, 0.5f));
        fog.setFogDistance(3000);
        fog.setFogDensity(1.5f);
        fpp.addFilter(fog);
        viewPort.addProcessor(fpp);
    }
}