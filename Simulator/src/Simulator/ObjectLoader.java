package Simulator;

import Simulator.cranes.Crane;
import Simulator.cranes.DockCrane;
import Simulator.cranes.SortCrane;
import Simulator.cranes.TrainCrane;
import Simulator.cranes.TruckCrane;
import Simulator.vehicles.AGV;
import Simulator.vehicles.FreightTruck;
import Simulator.vehicles.Train;
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
    private Spatial truck;
    private Spatial locomotive;
    private Spatial ship;
    private Spatial trainCart;
    
    private AssetManager assetManager;
    private SortField[] sortFields;
    private Node rootNode;
    private Node craneNode;
    private Node dockCraneNode;
    private boolean canSpawn;
    
    public HashMap<Integer, Crane> cranes = new HashMap<>();
    public HashMap<Integer, AGV> agvs = new HashMap<>();
    public HashMap<Integer, Container> containers = new HashMap<>();
    public List<WorldObject> vehicles = new ArrayList<>();
    public JSONArray spawnObjectList;
    private Train train;

    public ObjectLoader(Node rootNode, AssetManager assetManager, List<MotionEvent> motionControls) {
        this.dockCrane = assetManager.loadModel("Models/crane/dockingcrane/crane.j3o");
        this.sortCrane = assetManager.loadModel("Models/crane/storagecrane/crane.j3o");
        this.truckCrane = assetManager.loadModel("Models/crane/truckcrane/crane.j3o");
        this.trainCrane = assetManager.loadModel("Models/crane/traincrane/crane.j3o");
        this.container = assetManager.loadModel("Models/container/container.j3o");
        this.locomotive = assetManager.loadModel("Models/train/train.j3o");
        this.trainCart = assetManager.loadModel("Models/train/wagon.j3o");
        this.agv = assetManager.loadModel("Models/agv/agv.j3o");
        this.truck = assetManager.loadModel("Models/truck/truck.j3o");
        this.ship = assetManager.loadModel("Models/ship/seaship.j3o");
        this.assetManager = assetManager;
        this.rootNode = rootNode;
        this.canSpawn = true;
    }
    
    public Container addContainer(int containerId, CommandHandler commandHandler) {
        Container containerAdd = new Container(
                    this.rootNode,
                    this.assetManager,
                    new Vector3f(0, 0, 0),
                    this.getContainerModel(),
                    commandHandler);

        this.containers.put(containerId, containerAdd);
        return containerAdd;
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
    
    private void initSortFields() {
        JSONObject sortFieldData = this.loadJson("assets/data/sortfields.json");
        JSONArray containerSize = sortFieldData.getJSONArray("containerSize");
        JSONArray sortFields = sortFieldData.getJSONArray("sortFields");
        JSONArray position;
        JSONArray size;
        Vector3f containerSizeVec = new Vector3f(
                (float) containerSize.getDouble(0),
                (float) containerSize.getDouble(1),
                (float) containerSize.getDouble(2)
            );
        Vector3f positionVec;
        Vector3f sizeVec;
        
        
        this.sortFields = new SortField[sortFields.length()];
        
        for (int i = 0; i < sortFields.length(); i++) {
            position = sortFields.getJSONObject(i).getJSONArray("position");
            size = sortFields.getJSONObject(i).getJSONArray("size");
            
            positionVec = new Vector3f(
                    (float) position.getDouble(0),
                    (float) position.getDouble(1),
                    (float) position.getDouble(2)
                );
            sizeVec = new Vector3f(
                    (float) size.getDouble(0),
                    (float) size.getDouble(1),
                    (float) size.getDouble(2)
                );
            this.sortFields[i] = new SortField(positionVec, sizeVec, containerSizeVec);
        }
            
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
     * @return clone of trainCart model
     */
    public Spatial getTrainCartModel() {
       return this.trainCart.clone();
    }
    
    /**
     * @return clone of train locomotive model
     */
    public Spatial getLocomotiveModel() {
       return this.locomotive.clone();
    }
    
    /**
     * @return clone of ship model
     */
    public Spatial getShipModel() {
        return this.ship.clone();
    }
    
    /**
     * @return clone of truck model
     */
    public Spatial getTruckModel() {
        return this.truck.clone();
    }
    
    /**
     * @return clone of agv model
     */
    public Spatial getAgvModel() {
        return this.agv.clone();
    }
    
    public boolean checkObjects() {
        if (this.spawnObjectList != null && this.canSpawn) {
            this.canSpawn = false;
            return true;
        }
        return false;
    }
    
    public void spawnTrain(JSONArray containers) {
        this.train = new Train(50, this.rootNode, this.assetManager, this.getLocomotiveModel(), this.getTrainCartModel());
        for (Object containerId : containers) {
            this.train.addContainer(new Container(
                    this.rootNode,
                    this.assetManager,
                    new Vector3f(0, 0, 0),
                    this.getContainerModel(),
                    null));            
        }
        this.train.moveIn();
    }
    
    public void spawnTruck(Container container, Vector3f position) {
        FreightTruck frtruck = new FreightTruck(this.rootNode, this.assetManager, position, this.getTruckModel());
        frtruck.attachContainer(container);
        this.vehicles.add(frtruck);
    }

    /**
     * get spawn information from file and spawn objects
     */
    public void spawnObjects(JSONArray objects)
    {
        this.craneNode = new Node();
        this.dockCraneNode = new Node();
        
        for(int i = 0; i < objects.length(); i++)
        {
            this.spawnObject(objects.getJSONObject(i));
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
        
        JSONArray position = object.getJSONArray("position");
        
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