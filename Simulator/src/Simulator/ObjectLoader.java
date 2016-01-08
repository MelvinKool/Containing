package Simulator;

import Simulator.cranes.Crane;
import Simulator.cranes.DockCrane;
import Simulator.cranes.SortCrane;
import Simulator.cranes.TrainCrane;
import Simulator.cranes.TruckCrane;
import Simulator.vehicles.AGV;
import com.jme3.asset.AssetManager;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class ObjectLoader {
    
    private Spatial container;
    private Spatial sortCrane;
    private Spatial dockCrane;
    private Spatial truckCrane;
    private Spatial trainCrane;
    private Spatial agv;
    private Spatial freightTruck;
    private Spatial locomotive;
    private Spatial Ship;
    private Spatial trainCart;
    
    private AssetManager assetManager;
    private List<MotionEvent> motionControls;
    private Node rootNode;
    private Node craneNode;
    private Node dockCraneNode;
    
    public HashMap<Integer, Crane> cranes = new HashMap<>();
    public HashMap<Integer, AGV> agvs = new HashMap<>();

    public ObjectLoader(Node rootNode, AssetManager assetManager, List<MotionEvent> motionControls) {
        this.dockCrane = assetManager.loadModel("Models/crane/dockingcrane/crane.j3o");
        this.sortCrane = assetManager.loadModel("Models/crane/storagecrane/crane.j3o");
        this.truckCrane = assetManager.loadModel("Models/crane/truckcrane/crane.j3o");
        this.trainCrane = assetManager.loadModel("Models/crane/traincrane/crane.j3o");
        this.container = assetManager.loadModel("Models/container/container.j3o");
        this.agv = assetManager.loadModel("Models/agv/agv.j3o");
        this.assetManager = assetManager;
        this.motionControls = motionControls;
        this.rootNode = rootNode;
    }
    
    /**
     * Load json from filePath
     * @param filePath path to json file
     * @return JSONObject
     */
    private JSONObject loadJson(String filePath) {
        BufferedReader reader;
        String content = "";
        String line;
        Path path = Paths.get(filePath);
        try
        {
            reader = Files.newBufferedReader(path, Charset.forName("utf-8"));
            while((line = reader.readLine()) != null)
            {
                content += line;
            }
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        
        return new JSONObject(content);
    }
    
    /**
     * @return clone of dockcrane model
     */
    public Spatial getDockCraneModel() {
       return this.dockCrane.clone();
    }
    
    /**
     * @return clone of container model
     */
    public Spatial getContainerModel() {
       return this.container.clone();
    }
    
    /**
     * @return clone of sort crane model
     */
    public Spatial getSortCraneModel() {
       return this.sortCrane.clone();
    }
    
    /**
     * @return clone of truck crane model
     */
    public Spatial getTruckCraneModel() {
       return this.truckCrane.clone();
    }
    
    /**
     * @return clone of train crane model
     */
    public Spatial getTrainCraneModel() {
       return this.trainCrane.clone();
    }
    
    /**
     * @return clone of agv model
     */
    public Spatial getAgvModel() {
        return this.agv.clone();
    }
    
    /**
     * get spawn information from file and spawn objects
     */
    public void spawnObjects(JSONArray objects)
    {
        this.craneNode = new Node();
        this.dockCraneNode = new Node();
        
        for(Object object : (Iterable) objects.iterator())
        {
            this.spawnObject((JSONObject) object);
        }
        
        this.craneNode.attachChild(this.dockCraneNode);
        this.rootNode.attachChild(this.craneNode);
    }

    /**
     * spawn cranes and agvs from json information
     * @param object
     * @param type 
     */
    private void spawnObject(JSONObject object)
    {
        String type = object.getString("type");
        int id = object.getInt("id");
        
        JSONArray position = object.getJSONArray("spawnPosition");
        
        float speed = (float) object.getDouble("speed");
        float holderSpeed = 0.0f;
        float grabberSpeed = 0.0f;
        float grabberYOffset = 0.0f;
        
        float rotX = (float) Math.toRadians(object.getJSONArray("rotation").getDouble(0));
        float rotY = (float) Math.toRadians(object.getJSONArray("rotation").getDouble(1));
        float rotZ = (float) Math.toRadians(object.getJSONArray("rotation").getDouble(2));
        
        float posX = (float) position.getDouble(0);
        float posY = (float) position.getDouble(1);
        float posZ = (float) position.getDouble(2);
        
        Vector3f positionVec = new Vector3f(posX, posY, posZ);
        Vector3f holderPosition = null;
        
        Crane craneObj = null;
        AGV agvObj = null;
        
        if (!type.equals("AGV")) {        
            JSONObject grabberInfo = object.getJSONObject("grabber");
            JSONArray grabberPosition = grabberInfo.getJSONArray("position");
            boolean hasHolder = grabberInfo.getBoolean("has_holder");
            
            grabberYOffset = (float) grabberInfo.getDouble("y_offset");
            grabberSpeed = (float) grabberInfo.getDouble("speed");
            holderSpeed = (float) grabberInfo.getDouble("holderSpeed");
            
             holderPosition = new Vector3f(
                    (float) grabberPosition.getDouble(0),
                    (float) grabberPosition.getDouble(1),
                    (float) grabberPosition.getDouble(2)
            );
        }
        
        switch (type)
        {
            case "Storage":
                craneObj = new SortCrane(this.rootNode, this.assetManager, positionVec, this.getSortCraneModel(), "storagecrane", speed);
                break;
            case "Train": 
                craneObj = new TrainCrane(this.rootNode, this.assetManager, positionVec, this.getTrainCraneModel(), "traincrane", speed);
                break;
            case "SeaShip":
                craneObj = new DockCrane(this.rootNode, this.assetManager, positionVec, this.getDockCraneModel(), "dockingcrane", speed);
                break;
            case "TruckCrane": 
                craneObj = new TruckCrane(this.rootNode, this.assetManager, positionVec, this.getTruckCraneModel(), "truckcrane", speed);
                break;
            case "FreightShip": 
                craneObj = new DockCrane(this.rootNode, this.assetManager, positionVec, this.getDockCraneModel(), "dockingcrane", speed);
                break;
            case "AGV":
                agvObj = new AGV(this.rootNode, this.assetManager, positionVec, this.getAgvModel());
        }
        if (craneObj != null)
        {
            craneObj.node.rotate(rotX, rotY, rotZ);
            craneObj.initGrabber(holderPosition, grabberSpeed, holderSpeed, grabberYOffset);
            this.cranes.put(id, craneObj);
        }
        else if (agvObj != null)
        {
            agvObj.node.rotate(rotX, rotY, rotZ);
            this.agvs.put(id, agvObj);
        }
    } 
}