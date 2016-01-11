package Simulator;

import Simulator.cranes.Crane;
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
import java.util.List;

public class Main extends SimpleApplication
{
    private Node dockCraneNode;
    private List<Container> containers;
    private Connection connection;
    private List<MotionEvent> motionControls = new ArrayList<MotionEvent>();
    private List<Vector3f> locations = new ArrayList<>();
    private ObjectLoader worldObjects;
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
        this.worldObjects = new ObjectLoader(this.rootNode, this.assetManager, this.motionControls);
        long end = System.currentTimeMillis();

        System.out.println(end - start);
        
        locations.add(new Vector3f(0,0,0));
        
        this.dockCraneNode = new Node();
        this.playing = false;
        flyCam.setEnabled(false);
        flyCam.setMoveSpeed(200);
        cam.setFrustumFar(2000);
        
        this.containers = new ArrayList<>();
        this.containers.add(new Container(this.rootNode, this.assetManager, this.motionControls, new Vector3f(75, 0, 35), this.worldObjects.getContainerModel()));
        this.containers.add(new Container(this.rootNode, this.assetManager, this.motionControls, new Vector3f(235, 0, -200), this.worldObjects.getContainerModel()));
        this.containers.add(new Container(this.rootNode, this.assetManager, this.motionControls, new Vector3f(0, 0, 0), this.worldObjects.getContainerModel()));

        this.containers.get(0).node.rotate(0.0f, (float) Math.PI / 2, 0.0f);
        
        initLight();
        initInputs();
        
        Spatial SimWorld = assetManager.loadModel("Models/world/SimWorld.j3o");
        rootNode.attachChild(SimWorld);
        rootNode.attachChild(this.dockCraneNode);
        
        try { connection = new Connection(worldObjects); }
        catch (Exception e) { System.out.println(e); }
    }
    
    @Override
    public void simpleUpdate(float tpf)
    {
//        if (this.test == false) {
//            this.test = true;
//            AGV tagv = this.worldObjects.agvs.get(0);
//            List<float[]> path = new ArrayList<>();
//            path.add(new float[] {0.0f, 0.0f, 0.0f});
//            tagv.setPath(path);
//            
//        }
        
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
    
    //This is important to properly close the connection
    //with the server when you try to close the simulator.
    @Override
    public void destroy()
    {
        super.destroy();
        if(connection != null) connection.stop();
    }

    public Crane getNearestCrane(Node obj)
    {
        float dist;
        float minDist = -1;
        Crane nCrane = null;
        for (Crane crane : this.worldObjects.cranes)
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
                        break;
                    case "xm":
                        cont.node.move(-5,0,0);
                        break;
                    case "zp":
                        cont.node.move(0,0,5);
                        break;
                    case "zm":
                        cont.node.move(0,0,-5);
//                        worldObjects.agvs.get(0).setPath(locations);
//                        worldObjects.agvs.get(1).setPath(locations);
//                        worldObjects.agvs.get(2).setPath(locations);
//                        worldObjects.agvs.get(3).setPath(locations);
//                        worldObjects.agvs.get(4).setPath(locations);
//                        worldObjects.agvs.get(5).setPath(locations);
//                        worldObjects.agvs.get(6).setPath(locations);
//                        worldObjects.agvs.get(7).setPath(locations);
//                        worldObjects.agvs.get(8).setPath(locations);
//                        worldObjects.agvs.get(9).setPath(locations);
//                        worldObjects.agvs.get(10).setPath(locations);
//                        worldObjects.agvs.get(11).setPath(locations);
//                        worldObjects.agvs.get(12).setPath(locations);
//                        worldObjects.agvs.get(13).setPath(locations);
//                        worldObjects.agvs.get(14).setPath(locations);
//                        worldObjects.agvs.get(15).setPath(locations);
                        break;
                    }
                }
                System.out.println(cont.getPosition());

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
}