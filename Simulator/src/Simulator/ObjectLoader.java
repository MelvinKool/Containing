/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulator;

import Simulator.cranes.Crane;
import Simulator.cranes.DockCrane;
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
    public Spatial agv;
    public Spatial freightTruck;
    public Spatial locomotive;
    public Spatial Ship;
    public Spatial trainCart;
    
    private AssetManager assetManager;
    private Node rootNode;
    private Node craneNode;
    private Node dockCraneNode;
    
    public List<Crane> cranes = new ArrayList<Crane>();
    
    public ObjectLoader(Node rootNode, AssetManager assetManager) {
        this.dockCrane = assetManager.loadModel("Models/crane/dockingcrane/crane.j3o");
        this.container = assetManager.loadModel("Models/container/container.j3o");
        this.assetManager = assetManager;
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
    
    private void initCranes() {
        JSONObject craneData = this.loadJson("assets/data/ObjectLocations.json");
        this.craneNode = new Node();
        this.dockCraneNode = new Node();
        
        for (String crane : craneData.keySet()) {
            System.out.println(crane);
            switch (crane) {
                case "Storage": 
                    break;
                case "Train": 
                    break;
                case "SeaShip":
                    this.spawnDockCranes(craneData.getJSONObject(crane));
                    break;
                case "AGV": 
                    break;
                case "TruckCrane": 
                    break;
                case "FreightShip": 
                    break;
            }
        }
        
        this.craneNode.attachChild(this.dockCraneNode);
        this.rootNode.attachChild(this.craneNode);
    }

    private void spawnDockCranes(JSONObject craneObject) {
        float rotX = (float) Math.toRadians(craneObject.getJSONArray("rotation").getDouble(0));
        float rotY = (float) Math.toRadians(craneObject.getJSONArray("rotation").getDouble(1));
        float rotZ = (float) Math.toRadians(craneObject.getJSONArray("rotation").getDouble(2));
        
        float posX;
        float posY;
        float posZ;
        
        Vector3f rotation = new Vector3f(rotX, rotY, rotZ);
        Vector3f positionVec;
        
        JSONArray position;
        DockCrane dockCraneObj;
        
        for (Object positionObj : craneObject.getJSONArray("positions")) {
            position = (JSONArray) positionObj;
            
            posX = (float) position.getDouble(0);
            posY = (float) position.getDouble(1);
            posZ = (float) position.getDouble(2);
            
            positionVec = new Vector3f(posX, posY, posZ);
            dockCraneObj = new DockCrane(this.dockCraneNode, this.assetManager, new ArrayList<MotionEvent>(), positionVec, this.getDockCraneModel());
            dockCraneObj.node.rotate(rotX, rotY, rotZ);
            this.cranes.add(dockCraneObj);
            
            System.out.println("new dockCrane at: " + rotation.toString());
        }
        
        System.out.println(this.cranes.size());
    }
    
}
