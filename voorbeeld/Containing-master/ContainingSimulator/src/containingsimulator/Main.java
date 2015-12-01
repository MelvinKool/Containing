package containingsimulator;

import containing.xml.SimContainer;
import containing.xml.Message;
import com.jme3.app.SimpleApplication;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.FogFilter;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.CameraControl.ControlDirection;
import com.jme3.scene.shape.Quad;
import com.jme3.scene.shape.Sphere;
import com.jme3.system.AppSettings;
import com.jme3.texture.Texture;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.dropdown.DropDownControl;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.awt.DisplayMode;
import java.awt.GraphicsEnvironment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * test
 *
 * @author normenhansen
 */
public class Main extends SimpleApplication implements ScreenController {

    public void bind(Nifty nifty, Screen screen) {
    }

    public void onStartScreen() {
        app.getFlyByCamera().setDragToRotate(false);
        setCrossHairs(true);
    }

    public void onEndScreen() {
    }
    static ServerListener listener;
    Spatial sky_geo;
    Spatial agvModel;
    /*
     Seacrane spatials
     */
    Spatial scModel;
    Spatial scSModel;
    Spatial scHModel;
    /*
     Buffercrane spatials
     */
    Spatial bcModel;
    Spatial bcSModel;
    Spatial bcHModel;
    /*
     Lorrycrane spatials
     */
    Spatial lcModel;
    Spatial lcHModel;
    /*
     Traincrane spatials
     */
    Spatial tcModel;
    Spatial tcSModel;
    Spatial tcHModel;
    /*
     Bargecrane spatials
     */
    Spatial barModel;
    Spatial barSModel;
    Spatial barHModel;
    /*
     crane 
     */
    Spatial env;
    int graphicsQuality = 1;
    FilterPostProcessor fpp;
    Material noTex;
    
    Crane[] seaCranes = new Crane[10];
    Crane[] bufCranes = new Crane[63];
    Crane[] lorCranes = new Crane[20];
    Crane[] trainCranes = new Crane[4];
    Crane[] barCranes = new Crane[8];
    ArrayList<Transporter> transporters;
    ArrayList<AGV> agvs;
    Buffer[] buffers;
    private static Main app = null;
    public static float globalSpeed = 1f;
    private boolean isPaused = false;
    private CameraNode cam2Node = new CameraNode();
    private Camera cam2;
    private Node cam2EndNode = new Node();
    BitmapText cam2Text;
    BitmapText crossHair;
    ViewPort view2;
    public static Material alpha;
    Nifty nifty;
    private boolean gameIsStarted = false;
    private ParticleEmitter fire;
    private boolean escPressed = false;
    private float count = 0;
    private float max = 2;

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        Logger.getLogger("com.jme3").setLevel(Level.SEVERE);
        app = new Main();

        app.setDisplayFps(false);
        app.setDisplayStatView(false);
        app.setShowSettings(false);

        AppSettings settings = new AppSettings(true);
        settings.put("Title", "Project Containing - by Sjaal");
        //Anti-Aliasing
        settings.put("Samples", 0);
        settings.put("VSync", true);

        app.setSettings(settings);

        app.start();
    }

    /**
     * Simulator initialization
     */
    @Override
    public void simpleInitApp() {

        setPauseOnLostFocus(false);
        loadGame();
        inputManager.deleteMapping(SimpleApplication.INPUT_MAPPING_EXIT);
        inputManager.addMapping("Pause", new KeyTrigger(KeyInput.KEY_ESCAPE), new KeyTrigger(KeyInput.KEY_PAUSE));
        inputManager.addListener(actionListener, "Pause");
        listener = new ServerListener(this);
        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(
                app.assetManager, app.inputManager, app.audioRenderer, app.guiViewPort);
        nifty = niftyDisplay.getNifty();
        guiViewPort.addProcessor(niftyDisplay);
        flyCam.setDragToRotate(true);
        Logger.getLogger("de.lessvoid.nifty").setLevel(Level.SEVERE);
        Logger.getLogger("NiftyInputEventHandlingLog").setLevel(Level.SEVERE);

        //nifty.loadStyleFile("nifty-default-styles.xml");
        // nifty.loadControlFile("nifty-default-controls.xml");
        Screen_Start startController = new Screen_Start(this);
        startController.initialize(stateManager, app);
        nifty.registerScreenController(this);
        nifty.registerScreenController(startController);
        nifty.fromXml("Interface/screen.xml", "start", startController);

        DropDownControl dropDown1 = nifty.getScreen("start").findControl("dropDownRes", DropDownControl.class);
        DisplayMode[] modes = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayModes();
        DropDownControl dropDown2 = nifty.getScreen("start").findControl("dropDownBPP", DropDownControl.class);
        DropDownControl dropDown3 = nifty.getScreen("start").findControl("dropDownQuality", DropDownControl.class);
        for (DisplayMode mode : modes) {
            String res = mode.getWidth() + "x" + mode.getHeight();
            if (!dropDown1.getItems().contains(res)) {
                dropDown1.addItem(res);
            }

        }
        dropDown2.addItem("32");
        dropDown2.addItem("24");
        dropDown2.addItem("16");
        dropDown2.addItem("8");

        dropDown2.selectItem(3);
        
        dropDown3.addItem("low");
        dropDown3.addItem("medium");
        dropDown3.addItem("high");
        
        dropDown3.selectItem(1);
        
        nifty.gotoScreen("start"); // start the screen
    }

    public void loadGame() {
        Path.createPath();
        loadAssets();
        init_Fire();
        init_Input();
        flyCam.setMoveSpeed(400f);
        cam.setFrustumFar(5000f);
        cam.setLocation(new Vector3f(-254, 416, 280));
        cam.lookAt(new Vector3f(300, 0, 300), Vector3f.UNIT_Y);
        init_SecondCam();
        flyCam.setDragToRotate(false);
    }

    private boolean setConnection(String ip, int port) {
        try {
            if (!listener.running) {
                listener.changeConnection(ip, port);
                return true;
            }
        } catch (Exception ex) {

            return false;
        }
        return false;
    }

    public void startGame(String ip, int port, int width, int height, int bbp, boolean vSync, boolean showFps, boolean showStats, int quality) {
        boolean newSet = false;
        
        setQuality(quality);
        
        if (app.settings.getWidth() != width) {
            newSet = true;
            app.settings.setWidth(width);
        }
        if (app.settings.getHeight() != height) {
            newSet = true;
            app.settings.setHeight(height);
        }
        if (app.settings.getBitsPerPixel() != bbp) {
            newSet = true;
            app.settings.setBitsPerPixel(bbp);
        }
        if (app.settings.getBoolean("VSync") != vSync) {
            newSet = true;
            app.settings.put("VSync", vSync);
        }
        if (newSet) {
            app.restart();
        }
        app.setDisplayFps(showFps);
        app.setDisplayStatView(showStats);
        setConnection(ip, port);

        if (!gameIsStarted) { //only once! 
            gameIsStarted = true;
            setCrossHairs(true);
        }
        updateCHPos();
        nifty.gotoScreen("hud");
    }

    /**
     * Update function
     *
     * @param tpf
     */
    @Override
    public void simpleUpdate(float tpf) {
        ((Screen_Start) nifty.getScreen("start").getScreenController()).update(tpf);
        if (escPressed) {
            if (count >= max) {
                escPressed = false;
                count = 0;
                app.flyCam.setDragToRotate(true);
                setCrossHairs(false);
                remScreen2();
                nifty.gotoScreen("start");
            } else {
                count += tpf * 10;
            }
        }

        if (!gameIsStarted) {
            return;
        }
        if (!isPaused) {
            ArrayList<AGV> gehad = new ArrayList<AGV>();
            for (AGV a : agvs) {
                if (a.pathWasPlaying && !gehad.contains(a) && !a.getDefect()) {
                    boolean collision = false;
                    for (AGV collidingWith : agvs) {
                        if (collidingWith.pathWasPlaying && collidingWith != a) {
                            if (a.boundingGeom.getWorldBound().intersects(collidingWith.getWorldBound())) {

                                a.motionEvent.pause();
                                gehad.add(collidingWith);
                                collision = true;
                                break;
                            }
                        }
                    }
                    if (collision == false) {
                        a.motionEvent.play();
                    }
                } else if (gehad.contains(a) && !a.getDefect()) {
                    a.motionEvent.play();
                }
            }


            for (Crane c : seaCranes) {
                c.update(tpf);
            }
            for (Crane c : lorCranes) {
                c.update(tpf);
            }
            for (Crane c : bufCranes) {
                c.update(tpf);
            }
            for (Crane c : barCranes) {
                c.update(tpf);
            }
            for (Crane c : trainCranes) {
                c.update(tpf);
            }
        }

        sky_geo.setLocalTranslation(cam.getLocation());

        for (String s
                : listener.getMessages()) {
            Message m = Message.decodeMessage(s);
            this.messageRecieved(m);
        }
    }

    /**
     *
     * @param rm
     */
    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }

    /**
     * Load all used assets and initialize simulator objects
     */
    void loadAssets() {

        noTex = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        noTex.setColor("Color", ColorRGBA.DarkGray);
        
        //Init of the AGV viewmodel.
        agvModel = assetManager.loadModel("Models/AGV/AGV.j3o");

        Material avgMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture agv_text = assetManager.loadTexture("Textures/AGV/AGV.png");
        avgMat.setTexture("ColorMap", agv_text);
        agvModel.setMaterial(avgMat);

        //Init of skybox geometry, material, and texture.
        sky_geo = assetManager.loadModel("Models/SkyBox/SkyBox.j3o");
        Material skyMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture sky_text = assetManager.loadTexture("Textures/SkyBox_1.jpg");
        skyMat.setTexture("ColorMap", sky_text);
        sky_geo.setMaterial(skyMat);
        sky_geo.setQueueBucket(RenderQueue.Bucket.Sky);
        sky_geo.scale(2000f);
        rootNode.attachChild(sky_geo);

        //Init of enviroments
        env = assetManager.loadModel("Models/env/env.j3o");
        Material env_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture env_text = assetManager.loadTexture("Textures/env.png");
        env_mat.setTexture("ColorMap", env_text);
        env.setMaterial(env_mat);
        rootNode.attachChild(env);
        env.scale(100f);
        env.setLocalTranslation(0f, 0f, 600f);

        //Init Container
        Container.makeGeometry(assetManager);

        //Init of the SeaCrane viewmodel
        scModel = assetManager.loadModel("Models/seacrane/seacrane.j3o");
        scSModel = assetManager.loadModel("Models/seacrane/seacrane_slider.j3o");
        scHModel = assetManager.loadModel("Models/seacrane/seacrane_slider_hook.j3o");
        Material scMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture sc_text = assetManager.loadTexture("Textures/seacrane/seacrane.png");
        scMat.setColor("Color", ColorRGBA.White);
        scMat.setTexture("ColorMap", sc_text);
        scModel.setMaterial(scMat);
        scSModel.setMaterial(noTex);
        scHModel.setMaterial(noTex);
        
        //Init of bargecrane viewmodel
        barModel = assetManager.loadModel("Models/seacrane/seacrane.j3o");
        barSModel = assetManager.loadModel("Models/seacrane/seacrane_slider.j3o");
        barHModel = assetManager.loadModel("Models/seacrane/seacrane_slider_hook.j3o");
        Material barMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture bar_text = assetManager.loadTexture("Textures/seacrane/bargecrane.png");
        barMat.setColor("Color", ColorRGBA.White);
        barMat.setTexture("ColorMap", bar_text);
        barModel.setMaterial(barMat);
        barSModel.setMaterial(noTex);
        barHModel.setMaterial(noTex);

        Material m = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        m.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        // m.setColor("Color", new ColorRGBA(0,0,0,100));
        m.setColor("Color", ColorRGBA.Orange);
        alpha = m;
        //Init of the BufferCrane viewmodel
        bcModel = assetManager.loadModel("Models/buffercrane/buffercrane.j3o");
        bcSModel = assetManager.loadModel("Models/buffercrane/buffercrane_slider.j3o");
        bcHModel = assetManager.loadModel("Models/buffercrane/buffercrane_slider_hook.j3o");
        Material bcMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture bc_text = assetManager.loadTexture("Textures/buffercrane/buffercrane.png");
        bcMat.setColor("Color", ColorRGBA.White);
        bcMat.setTexture("ColorMap", bc_text);
        bcModel.setMaterial(bcMat);
        bcSModel.setMaterial(bcMat);
        bcHModel.setMaterial(noTex);

        //Init lorryCrane
        lcModel = assetManager.loadModel("Models/lorrycrane/lorrycrane.j3o");
        Material lcMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        lcMat.setColor("Color", ColorRGBA.Yellow);
        lcModel.setMaterial(noTex);

        //Init trainCrane
        tcModel = assetManager.loadModel("Models/traincrane/traincrane.j3o");
        tcSModel = assetManager.loadModel("Models/traincrane/traincrane_slider.j3o");
        tcHModel = assetManager.loadModel("Models/traincrane/traincrane_slider_hook.j3o");
        Material tcMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture tc_text = assetManager.loadTexture("Textures/traincrane/traincrane.png");
        tcMat.setColor("Color", ColorRGBA.White);
        tcMat.setTexture("ColorMap", tc_text);
        tcModel.setMaterial(tcMat);
        tcSModel.setMaterial(tcMat);
        tcHModel.setMaterial(noTex);

        //Init empty buffers
        buffers = new Buffer[63];
        for (int i = 0; i < buffers.length; i++) {
            buffers[i] = new Buffer();
            rootNode.attachChild(buffers[i]);
            //some magic number abuse here, snap buffers to proper location on map:
            buffers[i].setLocalTranslation(111.76f + (i * 22.035f), 11, 115);
            buffers[i].addParkingSpots(buffers[i].getLocalTranslation().clone());
        }

        init_SeaCranes();
        init_BargeCranes();
        init_BufferCranes();
        init_LorryCranes();
        init_TrainCranes();


        //Init Transporters
        Transporter.makeGeometry(assetManager);
        transporters = new ArrayList<Transporter>();

        //Init AGVs
        agvs = new ArrayList<AGV>();
        init_AGVs();

        //Init of the small blue plane, representing water.
        Quad waterQuad = new Quad(1550f, 600f);
        Geometry waterGeo = new Geometry("Quad", waterQuad);
        waterGeo.rotate(-(float) Math.PI / 2, 0f, 0f);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", new ColorRGBA(64f / 255, 113f / 255, 150f / 255, 1f));
        mat.setColor("Color", new ColorRGBA(50f / 255, 50f / 255, 50f / 255, 1f));
        waterGeo.setMaterial(mat);
        rootNode.attachChild(waterGeo);
        waterGeo.setLocalTranslation(-10000f, -10f, 30000f);
        waterGeo.scale(100f);

        //Init of lightsources of the project.
        DirectionalLight sun = new DirectionalLight();
        sun.setColor(ColorRGBA.White.mult(2f));
        sun.setDirection(new Vector3f(.5f, -.5f, -.5f).normalizeLocal());
        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(10f));
        rootNode.addLight(al);
        rootNode.addLight(sun);

        //Init of the dock spatial, the base of the simulator.
        Spatial dock = assetManager.loadModel("Models/dockBase/dockBase.j3o");
        rootNode.attachChild(dock);
        dock.setLocalTranslation(0f, 0f, 600f);
        
                fpp = new FilterPostProcessor();

        switch(graphicsQuality){
            case(0):
                fpp=(FilterPostProcessor) assetManager.loadAsset("Effects/filter_FOG.j3f");
                break;
            case(1):
                fpp=(FilterPostProcessor) assetManager.loadAsset("Effects/filter_WATER+FOG.j3f");
                break;
            case(2):
                fpp=(FilterPostProcessor) assetManager.loadAsset("Effects/filter_WATER+FOG+DOF+BLOOM+AA.j3f");
                break;
            default:
                fpp=(FilterPostProcessor) assetManager.loadAsset("Effects/filter_FOG.j3f");
                break;
        }
    }

    /**
     * Send a message back to the controller
     *
     * @param Message the message which to send
     */
    static void sendMessage(Message Message) {
        try {
            listener.sendMessage(Message);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    /**
     * Process the incoming message
     *
     * @param decodedMessage the decoded message which to process
     */
    private void messageRecieved(Message decodedMessage) {
        Object[] params = decodedMessage.getParameters();
        Container cont = null;
        Crane crane;
        String transporterID;
        AGV agv;
        switch (decodedMessage.getCommand()) {

            case Commands.RETREIVE_INFO:

                StringBuilder sB = new StringBuilder();
                switch (((String) params[0]).toLowerCase().charAt(0)) {
                    case 'i':
                        sB.append("container: " + params[0] + "\n");
                        sB.append("owner: " + params[1] + "\n");
                        sB.append("arr. date: " + params[2] + "\n");
                        sB.append("arr. trans.: " + TransportTypes.getTransportType((Integer) params[3]) + "\n");
                        sB.append("arr. cargo comp.: " + params[4] + "\n");
                        sB.append("dep. date: " + params[5] + "\n");
                        sB.append("dep. trans.: " + TransportTypes.getTransportType((Integer) params[6]) + "\n");
                        sB.append("dep. cargo comp.: " + params[7] + "\n");
                        sB.append("contents: " + params[8] + "\n");
                        sB.append("content type: " + params[9] + "\n");
                        sB.append("content danger: " + params[10] + "\n");
                        break;
                    case 't':
                        sB.append("transporter: " + params[0] + "\n");
                        sB.append("carrying: " + params[1] + " containers\n");
                        sB.append("length: " + params[4] + "\n");
                        sB.append("arr. date: " + params[2] + "\n");
                        sB.append("docking point: " + params[3] + "\n");
                        sB.append("type: " + TransportTypes.getTransportType((Integer) params[5]) + "\n");
                        break;
                    case 'b':
                    case 'c':
                        sB.append("crane: " + params[0] + "\n");
                        sB.append("status: " + (((Boolean) params[2]) ? "idle\n" : "busy\n"));
                        sB.append((params[1] != null) ? "container: " + (params[1]) + "\n " : "\n");
                        break;
                    case 'a':
                        sB.append("agv: " + params[0] + "\n");
                        sB.append("status: " + (((Boolean) params[2]) ? "idle\n" : "busy\n"));
                        sB.append("buffer: " + params[1] + "\n");
                        break;

                }
                printSecondViewText(sB.toString());
                break;
            case Commands.PAUSE_PLAY:
                pausePlay();
                break;
            case Commands.SHUTDOWN:
                System.exit(1);
                break;
            case Commands.MOVE:
                agv = getAGVbyID((String) params[0]);
                Crane c = null;
                String[] pathIDs1 = new String[params.length - 1];
                for (int i = 1; i <= pathIDs1.length; i++) {
                    pathIDs1[i - 1] = (String) params[i];

                }
                c = getCraneByID(pathIDs1[pathIDs1.length - 1]);
                agv.addWaypoints(pathIDs1, c);
                break;
            case Commands.PICKUP_CONTAINER:
                crane = getCraneByID((String) params[0]);
                cont = getContainerByID((String) params[2]);
                if (crane == null || cont == null) {
                    System.out.println("Error: crane is null OR container is null");
                    break;
                }
                if (crane instanceof BufferCrane) { //after picking up the container go to the up- or downside of the buffer
                    boolean up = (Boolean) params[1];

                    ((BufferCrane) crane).pickupContainer(cont, up);
                } else {
                    Transporter trans = getTransporterByID((String) params[1]);
                    if (trans == null) {
                        System.out.println("Error: No transporter with this ID");
                        break;
                    } else {
                        crane.pickupContainer(cont, trans);
                    }
                }
                break;
            case Commands.GIVE_CONTAINER:
                crane = getCraneByID((String) params[0]);
                agv = getAGVbyID((String) params[1]);
                if (agv != null) {
                    crane.loadContainer(agv);
                } else {
                    System.err.println("Error: agv is null");
                }

                //TODO: put container on Transporter/Buffer
                break;
            case Commands.PUT_CONTAINER:
                crane = getCraneByID((String) params[0]);
                Vector3f indexPosition = new Vector3f((Float) params[1], (Float) params[2], (Float) params[3]);
                Vector3f realPosition = null;
                if (crane != null && crane instanceof BufferCrane) {
                    realPosition = ((BufferCrane) crane).getBuffer().getRealContainerPosition(indexPosition);
                    crane.putContainer(realPosition, indexPosition);
                } else if (crane != null) {
                    {
                        crane.transporter = getTransporterByID((String) params[4]);
                        realPosition = crane.transporter.getRealContainerPosition(indexPosition);
                        crane.putContainer(realPosition, indexPosition);
                    }
                } else {
                    System.err.println("Error: No crane/container with this ID");
                }
                break;
            case Commands.MOVE_CRANE:
                crane = getCraneByID((String) params[0]);
                crane.transporter = getTransporterByID((String) params[1]);
                Vector3f indexPos = new Vector3f((Float) params[2], (Float) params[3], (Float) params[4]);
                realPosition = crane.transporter.getRealContainerPosition(indexPos);
                crane.transporter = null;
                crane.moveToPos(realPosition);
                break;
            case Commands.GET_CONTAINER:
                agv = getAGVbyID((String) params[0]);
                crane = getCraneByID((String) params[1]);
                if (crane != null && agv != null) {
                    crane.getContainer(agv);
                } else {
                    System.err.println("Error: No crane/agv with this ID");
                }
                //TODO: take container from AGV
                break;
            case Commands.CREATE_TRANSPORTER:
                transporterID = (String) params[0];
                int transporterType = (Integer) params[1];
                Vector3f dockingPoint = getCraneByID((String) params[2]).getPos();

                ArrayList<SimContainer> simContainers = new ArrayList<SimContainer>();
                for (int i = 3; i < params.length; i++) {
                    simContainers.add((SimContainer) params[i]);
                }
                Transporter t = new Transporter(transporterID, simContainers, dockingPoint, transporterType);
                transporters.add(t);
                rootNode.attachChild(t);
                break;
            case Commands.REMOVE_TRANSPORTER:
                transporterID = (String) params[0];

                Transporter temp = null;
                for (Transporter transp : transporters) {
                    if (transp.id.equalsIgnoreCase(transporterID)) {
                        temp = transp;
                        break;
                    }
                }
                switch (temp.type) {
                    case TransportTypes.BARGE:
                        for (Crane cr : barCranes) {
                            cr.moveToHome();
                        }
                        break;
                    case TransportTypes.LORRY:
                        for (Crane cr : lorCranes) {
                            cr.moveToHome();
                        }
                        break;
                    case TransportTypes.SEASHIP:
                        for (Crane cr : seaCranes) {
                            cr.moveToHome();
                        }
                        break;
                    case TransportTypes.TRAIN:
                        for (Crane cr : trainCranes) {
                            cr.moveToHome();
                        }
                        break;
                }

                rootNode.detachChild(temp);
                transporters.remove(temp);
                break;
            case Commands.CHANGE_SPEED:
                float newspeed = (Integer) params[0];
                changeGlobalSpeed(newspeed);
                break;
            default:
                System.err.println("Error: Invalid command for simulator.");
        }
    }

    /**
     * Finds and returns a transporter by container ID
     *
     * @param id the id to search for
     * @return reference to a transporter that matches the ID
     */
    private Transporter getTransporterByID(String id) {
        for (Transporter trans : this.transporters) {
            if (trans.id.equalsIgnoreCase(id)) {
                return trans;
            }
        }
        return null;
    }

    /**
     * Finds and returns a crane by crane ID
     *
     * @param id the id to search for
     * @return reference to a crane that matches the ID
     */
    private Crane getCraneByID(String id) {

        String crane = id.substring(0, 3);
        if (crane.equalsIgnoreCase(Path.getSeaID())) {
            for (int i = 0; i < seaCranes.length; i++) {
                if (seaCranes[i].getID().equalsIgnoreCase(id)) {
                    return seaCranes[i];
                }
            }
        } else if (crane.equalsIgnoreCase(Path.getBufferAID()) || crane.equalsIgnoreCase(Path.getBufferBID())) {
            for (int i = 0; i < bufCranes.length; i++) {
                if (bufCranes[i].getId().equalsIgnoreCase(id) || bufCranes[i].getId().equalsIgnoreCase(Path.getBufferAID() + id.substring(3))) {
                    return bufCranes[i];
                }
            }
        } else if (crane.equalsIgnoreCase(Path.getLorryID())) {
            for (int i = 0; i < lorCranes.length; i++) {
                if (lorCranes[i].getID().equalsIgnoreCase(id)) {
                    return lorCranes[i];
                }
            }
        } else if (crane.equalsIgnoreCase(Path.getTrainID())) {
            for (int i = 0; i < trainCranes.length; i++) {
                if (trainCranes[i].getID().equalsIgnoreCase(id)) {
                    return trainCranes[i];
                }
            }
        } else if (crane.equalsIgnoreCase(Path.getBargeID())) {
            for (int i = 0; i < barCranes.length; i++) {
                if (barCranes[i].getID().equalsIgnoreCase(id)) {
                    return barCranes[i];
                }
            }
        }
        return null;
    }

    /**
     * Finds and returns a container by container ID
     *
     * @param id the id to search for
     * @return reference to a container that matches the ID
     */
    private Container getContainerByID(String id) {
        Container c = null;
        for (int i = 0; i < buffers.length; i++) {
            c = buffers[i].getContainerByID(id);
            if (c != null) {
                return c;
            }
        }

        if (c == null) {
            for (Transporter t : transporters) {
                c = t.getContainerByID(id);
                if (c != null) {
                    return c;
                }
            }
        }
        if (c == null) {
            for (AGV a : agvs) {
                if (a.container != null && a.container.id.equalsIgnoreCase(id)) {
                    return a.container;
                }
            }
            for (Crane d : trainCranes) {
                if (d.cont != null && d.cont.id.equalsIgnoreCase(id)) {
                    return d.cont;
                }
            }
            for (Crane d : bufCranes) {
                if (d.cont != null && d.cont.id.equalsIgnoreCase(id)) {
                    return d.cont;
                }
            }
            for (Crane d : barCranes) {
                if (d.cont != null && d.cont.id.equalsIgnoreCase(id)) {
                    return d.cont;
                }
            }
            for (Crane d : lorCranes) {
                if (d.cont != null && d.cont.id.equalsIgnoreCase(id)) {
                    return d.cont;
                }
            }
            for (Crane d : seaCranes) {
                if (d.cont != null && d.cont.id.equalsIgnoreCase(id)) {
                    return d.cont;
                }
            }
        }
        return c;
    }

    /**
     * Finds and returns an AGV by container ID
     *
     * @param id the id to search for
     * @return reference to an AGV that matches the ID
     */
    private AGV getAGVbyID(String id) {

        for (AGV a : agvs) {
            if (a.id.equalsIgnoreCase(id)) {
                return a;
            }
        }
        return null;
    }

    private void showPathNodes(boolean show) {
        if (!show) {
            return;
        }

        Sphere sphere = new Sphere(32, 32, 2f);

        Geometry geometry = new Geometry("PathNode", sphere);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        HashMap<String, Vector3f> map = Path.getPath();
        Node node = new Node("PathNode");

        for (String key : map.keySet()) {
            Geometry geom = geometry.clone();
            geom.setMaterial(mat);
            geom.setName(key);
            Node n = node.clone(false);
            n.setName(key);
            n.attachChild(geom);
            rootNode.attachChild(n);
            n.setLocalTranslation(map.get(key));
        }
    }

    /**
     * Initialize SeaCranes
     */
    private void init_SeaCranes() {
        String cID = Path.getSeaID();
        for (int i = 1; i <= 10; i++) {
            String id = cID + String.format("%03d", i);
            Crane c = new SeaCrane(id, Path.getVector(id), scModel, scSModel, scHModel);
            seaCranes[i - 1] = c;
            rootNode.attachChild(c);
            c.setLocalTranslation(Path.getVector(id));
        }
    }

    /**
     * Initialize BufferCranes
     */
    private void init_BufferCranes() {
        String cID = Path.getBufferAID();

        for (int i = 1; i <= 63; i++) {
            String id = cID + String.format("%03d", i);
            Crane c = new BufferCrane(id, Path.getVector(id).add(0, 0, 200), bcModel, bcSModel, bcHModel, buffers[i - 1]);
            bufCranes[i - 1] = c;
            rootNode.attachChild(c);
            c.setLocalTranslation(Path.getVector(id).add(0, 0, 200));
        }
    }

    /**
     * Initialize LorryCranes
     */
    private void init_LorryCranes() {
        String cID = Path.getLorryID();

        for (int i = 1; i <= 20; i++) {
            String id = cID + String.format("%03d", i);
            Vector3f pos = Path.getVector(id).add(new Vector3f(0, 0, 40f));
            LorryCrane c = new LorryCrane(id, pos, lcModel, scSModel, scHModel.clone().scale(0.4f).rotate(0, 90 * FastMath.DEG_TO_RAD, 0));
            lorCranes[i - 1] = c;
            rootNode.attachChild(c);
            c.setLocalTranslation(pos);
        }
    }

    /**
     * Initialize BargeCranes
     */
    private void init_BargeCranes() {
        String cID = Path.getBargeID();
        for (int i = 1; i <= 8; i++) {
            String id = cID + String.format("%03d", i);
            Crane c = new BargeCrane(id, Path.getVector(id), barModel, barSModel, barHModel);
            barCranes[i - 1] = c;

            rootNode.attachChild(c);
            c.setLocalTranslation(Path.getVector(id));
        }
    }

    /**
     * Initialize TrainCranes
     */
    private void init_TrainCranes() {
        String cID = Path.getTrainID();
        for (int i = 1; i <= 4; i++) {
            String id = cID + String.format("%03d", i);
            Crane c = new TrainCrane(id, Path.getVector(id), tcModel, tcSModel, tcHModel);
            trainCranes[i - 1] = c;
            rootNode.attachChild(c);
            c.setLocalTranslation(Path.getVector(id));
        }
    }

    /**
     * Initialize AGVs
     */
    private void init_AGVs() {
        for (int i = 0; i < buffers.length * 4; i++) {
            String id = "AGV" + String.format("%03d", i + 1);
            AGV agv = new AGV(id, agvModel.clone());
            agvs.add(agv);
            rootNode.attachChild(agv);
        }

        int j = 0;
        for (int i = 0; i < buffers.length * 4; i += 4) {
            if (j < 5) {
                agvs.get(i).jumpToPark(buffers[j].pSpots[3]);
                agvs.get(i + 1).jumpToPark(buffers[j].pSpots[2]);
                agvs.get(i + 2).jumpToPark(buffers[j].pSpots[1]);
                agvs.get(i + 3).jumpToPark(buffers[j].pSpots[0]);
                agvs.get(i).up = true;
                agvs.get(i + 1).up = true;
                agvs.get(i + 2).up = true;
                agvs.get(i + 3).up = true;
            } else {
                agvs.get(i).jumpToPark(buffers[j].pSpots[1]);
                agvs.get(i + 1).jumpToPark(buffers[j].pSpots[0]);
                agvs.get(i + 2).jumpToPark(buffers[j].pSpots[6]);
                agvs.get(i + 3).jumpToPark(buffers[j].pSpots[7]);
                agvs.get(i).up = true;
                agvs.get(i + 1).up = true;
                agvs.get(i + 2).up = false;
                agvs.get(i + 3).up = false;
            }
            j++;
        }
    }

    /**
     * Send a READY message back to the controller
     *
     * @param id the ID of the object.
     */
    public static void sendReady(String id) {
        try {
            Object[] objectArray = new Object[1];
            objectArray[0] = id;
            Message message = new Message(0, objectArray);
            sendMessage(message);
        } catch (Exception ex) {
            System.out.println("Connection problems");
        }
    }

    //FOR TESTING///////FOR TESTING///////FOR TESTING////CLICK TO TEST SOMETHING! 
    public void init_Input() {
        inputManager.addMapping("left-click",
                new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addMapping("right-click",
                new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
        inputManager.addMapping("esc-button",
                new KeyTrigger(KeyInput.KEY_ESCAPE));
        inputManager.addMapping("left-button",
                new KeyTrigger(KeyInput.KEY_J));
        inputManager.addMapping("right-button",
                new KeyTrigger(KeyInput.KEY_L));
        inputManager.addMapping("up-button",
                new KeyTrigger(KeyInput.KEY_I));
        inputManager.addMapping("down-button",
                new KeyTrigger(KeyInput.KEY_K));
        inputManager.addMapping("rotateFor-button",
                new KeyTrigger(KeyInput.KEY_O));
        inputManager.addMapping("rotateBac-button",
                new KeyTrigger(KeyInput.KEY_U));
        inputManager.addMapping("resetRot-button",
                new KeyTrigger(KeyInput.KEY_P));
        inputManager.addMapping("camSpeedPlus-button",
                new KeyTrigger(KeyInput.KEY_C));
        inputManager.addMapping("camSpeedMin-button",
                new KeyTrigger(KeyInput.KEY_X));
        inputManager.addMapping("defect-button",
                new KeyTrigger(KeyInput.KEY_DELETE));



        inputManager.addListener(actionListener, "defect-button");
        inputManager.addListener(actionListener, "left-click");
        inputManager.addListener(actionListener, "right-click");
        inputManager.addListener(analogListener, "left-button");
        inputManager.addListener(analogListener, "right-button");
        inputManager.addListener(analogListener, "up-button");
        inputManager.addListener(analogListener, "down-button");
        inputManager.addListener(analogListener, "rotateFor-button");
        inputManager.addListener(analogListener, "rotateBac-button");
        inputManager.addListener(actionListener, "resetRot-button");
        inputManager.addListener(analogListener, "camSpeedPlus-button");
        inputManager.addListener(analogListener, "camSpeedMin-button");
    }
    private AnalogListener analogListener = new AnalogListener() {
        public void onAnalog(String name, float value, float tpf) {

            if (name.equals("camSpeedPlus-button")) {
                flyCam.setMoveSpeed(flyCam.getMoveSpeed() + 300 * tpf);
            } else if (name.equals("camSpeedMin-button")) {
                flyCam.setMoveSpeed(flyCam.getMoveSpeed() - 300 * tpf);
            }

            if (cam2EndNode.getParent() == null) {
                return;
            }
            float rY = 0;
            float z = 0;
            float rZ = 0;

            if (name.equals("left-button")) {
                rY = -1f;
            } else if (name.equals("right-button")) {
                rY = 1f;
            } else if (name.equals("up-button")) {
                z = -1f;
            } else if (name.equals("down-button")) {
                z = 1f;
            } else if (name.equals("rotateFor-button")) {
                rZ = 1f;
            } else if (name.equals("rotateBac-button")) {
                rZ = -1f;
            }

            cam2EndNode.rotate(0, rY * 5 * tpf, rZ * 5 * tpf);
            cam2.lookAt(cam2EndNode.getParent().getWorldTranslation(), Vector3f.UNIT_Y);
            cam2EndNode.getChild(0).move(z * tpf * 10, 0, 0);

        }
    };
    private ActionListener actionListener = new ActionListener() {
        public void onAction(String name, boolean keyPressed, float tpf) {
            if (nifty.getCurrentScreen().equals(nifty.getScreen("start"))) {
                return;
            } else if (name.equals("defect-button") && keyPressed) {
                simulateDefect();

            } else if (name.equals("Pause")) {
                escPressed = true;
            } else if (name.equals("resetRot-button")) {
                cam2EndNode.setLocalRotation(new Quaternion(0, 0, 0, 1));
            } else if (name.equals("left-click") && !keyPressed) {
                CollisionResults results = new CollisionResults();
                Ray ray = new Ray(cam.getLocation(), cam.getDirection().normalize());
                rootNode.collideWith(ray, results);
                //check if point of click was on the iceberg (only penguins on iceberg allowed!)
                if (results.size() > 0) {
                    // The closest collision point is what was truly hit:
                    CollisionResult closest = results.getClosestCollision();
                    Node closestNode = closest.getGeometry().getParent();
                    if (closestNode != null && !closestNode.equals(rootNode)) {
                        if (closestNode.getName() == null) {
                            return;
                        }
                        nifty.gotoScreen("hud2");
                        init_SecondCam();
                        cam2EndNode.setLocalRotation(new Quaternion(0, 0, 0, 1));
                        cam2EndNode.attachChild(cam2Node);
                        cam2Node.setLocalTranslation(15, 1, 0);
                        closestNode.attachChild(cam2EndNode);
                        cam2Node.lookAt(closestNode.getWorldTranslation(), Vector3f.UNIT_Y);

                        if (closestNode instanceof Container || closestNode instanceof Crane
                                || closestNode instanceof AGV || closestNode instanceof Transporter) {
                            Message m = new Message(Commands.RETREIVE_INFO, null);
                            ArrayList<Object> params = new ArrayList();
                            params.add(closestNode.getName());
                            m.setParameters(params.toArray());
                            sendMessage(m);
                        } else {
                            printSecondViewText(closestNode.getName());
                        }
                        if (view2 == null) {
                            view2 = renderManager.createMainView("Top Right", cam2);
                        }
                        if (view2.getScenes().size() < 1) {
                            view2.attachScene(rootNode);
                        }
                        view2.setClearFlags(true, true, true);
                    } else {
                        remScreen2();
                        nifty.gotoScreen("hud");
                    }
                }
            }
        }
    };

    private void remScreen2() {

        if (view2 != null) {
            view2.clearScenes();
            view2.setClearFlags(false, false, false);

        }
        if (cam2Text != null) {
            guiNode.detachChild(cam2Text);
        }
    }

    private void printSecondViewText(String text) {
        if (cam2Text == null) {
            cam2Text = new BitmapText(guiFont, false);
            cam2Text.setColor(ColorRGBA.Yellow);
            cam2Text.setSize((settings.getWidth() + settings.getHeight()) / 80);
            cam2Text.setLocalTranslation(0, settings.getHeight() / 2, 0);
        } else {
            guiNode.detachChild(cam2Text);
        }
        cam2Text.setText(text); // crosshairs
        guiNode.attachChildAt(cam2Text, 1);
    }

    private void init_SecondCam() {

        cam2 = cam.clone();
        cam2.setViewPort(0f, .4f, .3f, 1f);
        cam2Node = new CameraNode("Camera", cam2);
        cam2Node.setControlDir(ControlDirection.SpatialToCamera);

    }

    private void changeGlobalSpeed(float acceleration) {

        globalSpeed = acceleration;

        for (AGV agv : agvs) {
            agv.globalSpeedChanged();
        }
        for (Transporter tran : transporters) {
            tran.globalSpeedChanged();
        }

    }

    private void pausePlay() {

        isPaused = !isPaused;

        for (Crane c : seaCranes) {
            c.pausePlay(isPaused);
        }
        for (Crane c : lorCranes) {

            c.pausePlay(isPaused);
        }
        for (Crane c : bufCranes) {
            c.pausePlay(isPaused);
        }
        for (Crane c : barCranes) {
            c.pausePlay(isPaused);
        }
        for (Crane c : trainCranes) {
            c.pausePlay(isPaused);
        }
        for (AGV agv : agvs) {
            agv.pausePlay(isPaused);
        }
    }

    private void setCrossHairs(boolean enabled) {
        if (enabled) {
            if (crossHair == null) {
                crossHair = new BitmapText(guiFont, false);
                crossHair.setSize(guiFont.getCharSet().getRenderedSize() * 2);
                crossHair.setText("+"); // crosshairs
                updateCHPos();
            }
            guiNode.attachChild(crossHair);

        } else {
            if (crossHair != null) {
                guiNode.detachChild(crossHair);
            }
        }
    }

    private void updateCHPos() {
        crossHair.setLocalTranslation( // center
                settings.getWidth() / 2 - crossHair.getLineWidth() / 2, settings.getHeight() / 2 + crossHair.getLineHeight() / 2, 0);
    }

    //select AGV or Crane and press DELETE-key to defect/repair
    private void simulateDefect() {

        if (!nifty.getCurrentScreen().equals(nifty.getScreen("hud2"))) {
            return;
        }
        if (!listener.running) {
            return;
        }
        Node node = cam2EndNode.getParent();
        if (!((node instanceof Transporter) || (node instanceof Container))) {

            String id = node.getName();
            boolean pathWasPlaying = false;
            boolean defect = false;
            String sub = id.substring(0, 1);

            if (sub.equalsIgnoreCase("c") || sub.equalsIgnoreCase("b")) {
                pathWasPlaying = ((Crane) node).simulateDefect();
                defect = ((Crane) node).getDefect();
            } else if (sub.equalsIgnoreCase("a")) {
                pathWasPlaying = ((AGV) node).simulateDefect();
                defect = ((AGV) node).getDefect();
            }
            if (defect) {
                ParticleEmitter f = fire.clone();
                if (node instanceof Crane) {
                    f.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 5, 0));
                }
                cam2EndNode.getParent().attachChild(f);
                if (node instanceof SeaCrane || node instanceof BargeCrane) {
                    f.setStartSize(10f);
                    f.setEndSize(15f);
                    f.setHighLife(20);
                    f.setLocalTranslation(cam2EndNode.getLocalTranslation().add(0, 30, 0));
                } else if (node instanceof TrainCrane) {
                    f.setLocalTranslation(cam2EndNode.getLocalTranslation().add(0, 16, 0));
                } else if (node instanceof BufferCrane) {
                    f.setLocalTranslation(cam2EndNode.getLocalTranslation().add(0, 22, 0));
                } else if (node instanceof LorryCrane) {
                    f.setLocalTranslation(cam2EndNode.getLocalTranslation().add(0, 8, 0));
                }
            } else {
                cam2EndNode.getParent().detachChildNamed("fire");
            }
            Message m = new Message(Commands.DEFECT, null);
            ArrayList<Object> params = new ArrayList();
            params.add(id);
            params.add(defect);
            params.add(pathWasPlaying);
            m.setParameters(params.toArray());
            sendMessage(m);

        }
    }

    private void init_Fire() {

        Texture fireTexture = assetManager.loadTexture("Textures/effects/flame.png");
        fire = new ParticleEmitter("fire", ParticleMesh.Type.Triangle, 30);
        Material mat_red = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
        mat_red.setTexture("Texture", fireTexture);
        fire.setMaterial(mat_red);
        fire.setImagesX(2);
        fire.setImagesY(2); // 2x2 texture animation
        fire.setEndColor(new ColorRGBA(1f, 0f, 0f, 1f));   // red
        fire.setStartColor(new ColorRGBA(1f, 1f, 0f, 0.5f)); // yellow
        fire.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 5, 5));
        fire.setStartSize(1.5f);
        fire.setEndSize(3f);
        fire.setGravity(0, 0, 0);
        fire.setLowLife(0.5f);
        fire.setHighLife(3f);

        fire.setQueueBucket(Bucket.Translucent);
        fire.getParticleInfluencer().setVelocityVariation(0.3f);


    }
    
    public void setQuality(int graphicsQuality) {
        viewPort.removeProcessor(fpp);
        switch(graphicsQuality){
            case(0):
                fpp=(FilterPostProcessor) assetManager.loadAsset("Effects/filter_FOG.j3f");
                break;
            case(1):
                fpp=(FilterPostProcessor) assetManager.loadAsset("Effects/filter_WATER+FOG.j3f");
                break;
            case(2):
                fpp=(FilterPostProcessor) assetManager.loadAsset("Effects/filter_WATER+FOG+DOF+BLOOM+AA.j3f");
                break;
            default:
                fpp=(FilterPostProcessor) assetManager.loadAsset("Effects/filter_FOG.j3f");
                break;
        }
        
        viewPort.addProcessor(fpp);
    }
}
