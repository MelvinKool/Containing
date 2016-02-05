package Simulator;

import Simulator.cranes.Crane;
import Simulator.vehicles.Train;
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
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Quad;
import com.jme3.util.SkyFactory;
import com.jme3.water.SimpleWaterProcessor;
import org.json.JSONObject;

public class Main extends SimpleApplication
{
    boolean connected = false;
    private Spatial SimWorld;
    private Connection connection;
    private ObjectLoader objectLoader;
    private CommandHandler commandHandler;
    private static int stSpeed;

    private Train train; // TODO: this is test code
    
    public static int getSpeed() {
        return stSpeed;
    }
    
    public void setSpeed(int speed) {
        this.speed = speed;
        stSpeed = speed;
    }

    public static void main(String[] args)
    {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp()
    {
        this.objectLoader = new ObjectLoader(this.rootNode, this.assetManager);
        
        JSONObject config = this.objectLoader.loadJson("resources/config.json");
        
        this.commandHandler = new CommandHandler(this.objectLoader);
        this.setSpeed(config.getInt("simulation_speed"));
        this.setDisplayStatView(false);

        flyCam.setEnabled(true);    //flycam lets you move the camera with wasd and mouse.
        flyCam.setMoveSpeed(200);   //set the movespeed if the flycam is enabled
        cam.setFrustumFar(3000);    //sets render distance of the scene
        this.setPauseOnLostFocus(false); // don't pause automatically on lost focus
        
        //Initialize the world with light, water, skybox and fog.
        initWorld();
        initLight();
        initWater();
        initSkybox();
        initFog();
        
        try 
        { 
            connection = new Connection(config.getString("server_ip"), config.getInt("server_port"), this.objectLoader, commandHandler);
        }
        catch (Exception e) { System.out.println(e); }
    }
    
    @Override
    public void simpleUpdate(float tpf)
    {
        // Destroy train when it says it can (when it's out of map)        
        if (this.objectLoader.train != null && this.objectLoader.train.canDestroy) {
            rootNode.detachChild(this.objectLoader.train.node);
            this.objectLoader.train = null;
        }
        
        // execute queued commands
        this.commandHandler.executeQueued();
        
        // execute queued commands for each crane
        for (Crane crane : this.objectLoader.cranes.values()) {
            crane.executeQueued();
        }
    }
    
    //This is important to properly close the connection
    //with the server when you try to close the simulator.
    @Override
    public void destroy()
    {
        super.destroy();
        if(connection != null) connection.interrupt();
    }
    
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