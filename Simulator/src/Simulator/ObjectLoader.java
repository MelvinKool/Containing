/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulator;

import Simulator.cranes.Crane;
import Simulator.cranes.DockCrane;
import Simulator.cranes.SortCrane;
import Simulator.cranes.TrainCrane;
import Simulator.cranes.TruckCrane;
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
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
/**
 *
 * @author erwin
 */
public class ObjectLoader {
    
    public Spatial container;
    public Spatial sortCrane;
    public Spatial dockCrane;
    public Spatial truckCrane;
    public Spatial trainCrane;
    public Spatial agv;
    public Spatial freightTruck;
    public Spatial locomotive;
    public Spatial Ship;
    public Spatial trainCart;
    
    private AssetManager assetManager;
    private List<MotionEvent> motionControls;
    private Node rootNode;
    private Node craneNode;
    private Node dockCraneNode;
    
    public List<Crane> cranes = new ArrayList<Crane>();
    
    public ObjectLoader(Node rootNode, AssetManager assetManager, List<MotionEvent> motionControls) {
        this.dockCrane = assetManager.loadModel("Models/crane/dockingcrane/crane.j3o");
        this.sortCrane = assetManager.loadModel("Models/crane/storagecrane/crane.j3o");
        this.truckCrane = assetManager.loadModel("Models/crane/truckcrane/crane.j3o");
        this.trainCrane = assetManager.loadModel("Models/crane/traincrane/crane.j3o");
        this.container = assetManager.loadModel("Models/container/container.j3o");
        this.assetManager = assetManager;
        this.motionControls = motionControls;
        this.rootNode = rootNode;
        this.initCranes();
    }
    
    private JSONObject loadJson(String filePath) {
        BufferedReader reader;
        String content = "";
        String line;
        Path path = Paths.get(filePath);
        try {
            reader = Files.newBufferedReader(path, Charset.forName("utf-8"));
            
            while ((line = reader.readLine()) != null) {
                content += line;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        return new JSONObject(content);
    }
    
    public Spatial getDockCraneModel() {
       return this.dockCrane.clone();
    }
    
    public Spatial getContainerModel() {
       return this.container.clone();
    }
    
    public Spatial getSortCraneModel() {
       return this.sortCrane.clone();
    }
    
    public Spatial getTruckCraneModel() {
       return this.truckCrane.clone();
    }
    
    public Spatial getTrainCraneModel() {
       return this.trainCrane.clone();
    }
    
    private void initCranes() {
        JSONObject craneData = this.loadJson("assets/data/ObjectLocations.json");
        this.craneNode = new Node();
        this.dockCraneNode = new Node();
        
        for (String crane : craneData.keySet()) {
            this.spawnCranes(craneData.getJSONObject(crane), crane);
        }
        
        this.craneNode.attachChild(this.dockCraneNode);
        this.rootNode.attachChild(this.craneNode);
    }

    private void spawnCranes(JSONObject craneObject, String type) {
        float rotX = (float) Math.toRadians(craneObject.getJSONArray("rotation").getDouble(0));
        float rotY = (float) Math.toRadians(craneObject.getJSONArray("rotation").getDouble(1));
        float rotZ = (float) Math.toRadians(craneObject.getJSONArray("rotation").getDouble(2));
        
        float posX;
        float posY;
        float posZ;
        
        Vector3f rotation = new Vector3f(rotX, rotY, rotZ);
        Vector3f positionVec;
        
        JSONArray position;
        Crane craneObj = null;
        
        for (Object positionObj : craneObject.getJSONArray("positions")) {
            position = (JSONArray) positionObj;
            
            posX = (float) position.getDouble(0);
            posY = (float) position.getDouble(1);
            posZ = (float) position.getDouble(2);
            
            positionVec = new Vector3f(posX, posY, posZ);
            switch (type) {
                case "Storage":
                    craneObj = new SortCrane(this.rootNode, this.assetManager, this.motionControls, positionVec, this.getSortCraneModel());
                    craneObj.node.rotate(rotX, rotY, rotZ);
                    break;
                case "Train": 
                    craneObj = new TrainCrane(this.rootNode, this.assetManager, this.motionControls, positionVec, this.getTrainCraneModel());
                    craneObj.node.rotate(rotX, rotY, rotZ);
                    break;
                case "SeaShip":
                    craneObj = new DockCrane(this.rootNode, this.assetManager, this.motionControls, positionVec, this.getDockCraneModel());
                    craneObj.node.rotate(rotX, rotY, rotZ);
                    break;
                case "TruckCrane": 
                    craneObj = new TruckCrane(this.rootNode, this.assetManager, this.motionControls, positionVec, this.getTruckCraneModel());
                    craneObj.node.rotate(rotX, rotY, rotZ);
                    break;
                case "FreightShip": 
                    craneObj = new DockCrane(this.rootNode, this.assetManager, this.motionControls, positionVec, this.getDockCraneModel());
                    craneObj.node.rotate(rotX, rotY, rotZ);
                    break;
            }
            if (craneObj != null) {
                this.cranes.add(craneObj);
            }
        }
    }
    
}
