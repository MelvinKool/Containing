package Simulator;

import Simulator.cranes.Crane;
import Simulator.cranes.DockCrane;
import Simulator.cranes.SortCrane;
import Simulator.cranes.TrainCrane;
import Simulator.cranes.TruckCrane;
import Simulator.vehicles.AGV;
import Simulator.vehicles.FreightTruck;
import Simulator.vehicles.Ship;
import Simulator.vehicles.Train;
import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingBox;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONObject;

public class ObjectLoader 
{
    
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
    private Node rootNode;
    private Node craneNode;
    private Node dockCraneNode;
    private boolean canSpawn;
    
    public SortField[] sortFields;
    public HashMap<Integer, Crane> cranes = new HashMap<>();
    public HashMap<Integer, AGV> agvs = new HashMap<>();
    public HashMap<Integer, Container> containers = new HashMap<>();
    public HashMap<Integer, WorldObject> vehicles = new HashMap<>();
    public JSONArray spawnObjectList;
    public Train train;
    public Ship seaShip;
    public Ship bargeShip;
    

    public ObjectLoader(Node rootNode, AssetManager assetManager)
    {
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
        
        this.initSortFields();
    }
    
    public Container addContainer(int containerId, CommandHandler commandHandler) 
    {
        Container containerAdd = new Container(
                    containerId,
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
    private JSONObject loadJson(String filePath)
    {
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
            System.err.println("No such file: " + ex.getMessage());
            System.exit(1);
        }
        
        return new JSONObject(content);
    }
    
    /**
     * initialize sortFields array
     */    
    private void initSortFields() 
    {
        Vector3f positionVec;        
        Vector3f maxIndex;
        JSONObject sortFieldData = this.loadJson("assets/data/sortfields.json");
        JSONArray sortFieldPositions = sortFieldData.getJSONArray("positions");
        JSONArray maxContainers = sortFieldData.getJSONArray("containers");
        Vector3f containerSizeVec = new Vector3f();
        BoundingBox containerSize = (BoundingBox) this.container.getWorldBound();
        
        maxIndex = new Vector3f(
                (float) maxContainers.getInt(0),
                (float) maxContainers.getInt(1),
                (float) maxContainers.getInt(2));
        
        containerSize.getExtent(containerSizeVec);
        //System.out.println("container dimensions: " + containerSizeVec);
        
        this.sortFields = new SortField[sortFieldPositions.length()];
        
        for (int i = 0; i < sortFieldPositions.length(); i++) 
        {
            JSONArray posArray  = sortFieldPositions.getJSONArray(i);
            positionVec = new Vector3f(
                    (float) posArray.getDouble(0),
                    (float) posArray.getDouble(1),
                    (float) posArray.getDouble(2)
                );
            this.sortFields[i] = new SortField(positionVec, containerSizeVec.mult(2), maxIndex);
            //System.out.println("new sortfield " + i + " at: " + positionVec);
        }
    }
    
    /**
     * @return clone of dockcrane model
     */
    public Spatial getDockCraneModel() 
    {
       return this.dockCrane.clone();
    }
    
    /**
     * @return clone of container model
     */
    public Spatial getContainerModel()
    {
       return this.container.clone();
    }
    
    /**
     * @return clone of sort crane model
     */
    public Spatial getSortCraneModel()
    {
       return this.sortCrane.clone();
    }
    
    /**
     * @return clone of truck crane model
     */
    public Spatial getTruckCraneModel() 
    {
       return this.truckCrane.clone();
    }
    
    /**
     * @return clone of train crane model
     */
    public Spatial getTrainCraneModel() 
    {
       return this.trainCrane.clone();
    }
    
    /**
     * @return clone of trainCart model
     */
    public Spatial getTrainCartModel() 
    {
       return this.trainCart.clone();
    }
    
    /**
     * @return clone of train locomotive model
     */
    public Spatial getLocomotiveModel() 
    {
       return this.locomotive.clone();
    }
    
    /**
     * @return clone of ship model
     */
    public Spatial getShipModel()
    {
        return this.ship.clone();
    }
    
    /**
     * @return clone of truck model
     */
    public Spatial getTruckModel()
    {
        return this.truck.clone();
    }
    
    /**
     * @return clone of agv model
     */
    public Spatial getAgvModel()
    {
        return this.agv.clone();
    }
    
    public boolean checkObjects()
    {
        if (this.spawnObjectList != null && this.canSpawn) 
        {
            this.canSpawn = false;
            return true;
        }
        return false;
    }
    
    public void spawnTrain(JSONArray containers, CommandHandler commandHandler) 
    {
        this.train = new Train(50, this.rootNode, this.assetManager, this.getLocomotiveModel(), this.getTrainCartModel());
        for (Object containerId : containers) 
        {
            this.train.addContainer(this.addContainer((int) containerId, commandHandler));            
        }
        //this.train.moveIn();
    }
    
    public void spawnTruck(int id, Container container, Vector3f position)
    {
        FreightTruck frtruck = new FreightTruck(this.rootNode, this.assetManager, position, this.getTruckModel());
        frtruck.attachContainer(container);
        this.vehicles.put(id, frtruck);
    }
    
   public void spawnSeaShip(JSONArray containers, CommandHandler commandHandler)
   {
        this.seaShip = new Ship(this.rootNode, this.assetManager, new Vector3f(-35,0,-350), this.getShipModel(), 0, 1);
        for(Object containerId : containers)
         {
             this.seaShip.addContainer(this.addContainer((int)containerId, commandHandler));
         }
   }
   
   public void spawnBargeShip(JSONArray containers, CommandHandler commandHandler)
   {
        this.bargeShip = new Ship(this.rootNode, this.assetManager, new Vector3f(350,0,35), this.getShipModel(), 0, 0.5f);
        for(Object containerId : containers)
         {
             this.bargeShip.addContainer(this.addContainer((int)containerId, commandHandler));
         }
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
            JSONObject objectObject = new JSONObject(objects.getString(i)); // TODO: fix json in server
            this.spawnObject(objectObject);
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
        
        if (!type.equals("AGV"))
        {        
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