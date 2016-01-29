package Simulator;

import Simulator.cranes.Crane;
import Simulator.vehicles.AGV;
import Simulator.vehicles.FreightTruck;
import com.jme3.math.Vector3f;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

public class CommandHandler
{
    ObjectLoader objectloader;
    private List<JSONObject> commandQueue;
    
    public CommandHandler(ObjectLoader objectloader)
    {
        this.objectloader = objectloader;
        this.commandQueue = new ArrayList<>();
    }
    
    public void setContainerCommands(JSONObject commands) {
        int containerId = commands.getInt("container");
        Container container = this.objectloader.containers.get(containerId);
        JSONArray commandArr = commands.getJSONArray("commands");
        List<JSONObject> commandList = new ArrayList<>();
        
        for (int i = 0; i < commandArr.length(); i++) 
        {
            String cmd = commandArr.getString(i);
            if (!cmd.isEmpty()) 
            {
                JSONObject objectObject = new JSONObject(cmd); // TODO: fix json in server
                commandList.add(objectObject);
            }
        }
        try {
            container.setCommands(commandList);
            container.operationDone();
            System.out.println("Set life for: " + container + ", id: " + containerId);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        } 
    }
    
    public void executeCommand(JSONObject jsonObject) {
        String command = jsonObject.getString("Command");
        int vehicleId;
        int containerId;
        AGV agv = null;
        
        System.out.println(jsonObject);
        
        switch(command)
        {
            case "containerCommands":
                this.setContainerCommands(jsonObject);
                break;
            case "moveAGV":
                vehicleId = jsonObject.getInt("vehicleId");
                JSONArray route = jsonObject.getJSONArray("Route");
                agv = objectloader.agvs.get(vehicleId);
                if (agv == null) {
                    return;
                }
                if (jsonObject.has("container") && jsonObject.getInt("container") != -1) {
                    containerId = jsonObject.getInt("container");
                    Container container = this.objectloader.containers.get(containerId);
                    agv.container = container;
                }
                List Locations = new ArrayList<>();
                float x,y,z;
                float totalDistance;
                for(Object coordinate : route)
                {
                    JSONArray coord = (JSONArray)coordinate;
                    x = (float)coord.getDouble(0);
                    y = (float)coord.getDouble(1);
                    z = (float)coord.getDouble(2);
                    Locations.add(new Vector3f(x,y,z));
                }
                totalDistance = (float)jsonObject.getDouble("totalDistance");
                agv.setPath(Locations, totalDistance);
                break;
            case "agvAttachContainer":
                int agvId = jsonObject.getInt("agvId");
                containerId = jsonObject.getInt("containerId");
                agv = this.objectloader.agvs.get(agvId);
                Container cont = this.objectloader.containers.get(containerId);
                agv.attachContainer(cont);
                break;
            case "craneMoveContainer":
                this.craneMoveContainer(jsonObject);
                break;
            case "spawnObjects":
                this.objectloader.spawnObjects(jsonObject.getJSONArray("objects"));
                break;
            case "spawnTruck":
                this.spawnTruck(jsonObject);
                break;
            case "spawnTrain":
                this.objectloader.spawnTrain(jsonObject.getJSONArray("containers"), this);
                break;
            case "spawnSeaShip":
                this.objectloader.spawnSeaShip(jsonObject.getJSONArray("containers"), this);
                break;
            case "spawnBargeShip":
                this.objectloader.spawnBargeShip(jsonObject.getJSONArray("containers"), this);
                break;
            case "despawnVehicle":
                int id = jsonObject.getInt("vehicleId");
                containerId = jsonObject.getInt("container");
                String vehicleType = jsonObject.getString("vehicleType");
                despawnObjects(vehicleType, id, containerId);
                break;
        }
    }
    
    public JSONObject ParseJSON(String json)
    {
        return new JSONObject(json);
    }
    
    private void despawnObjects(String vehicleType, int vehicleId, int containerId)
    {
        List<Container> attachedContainers;
        Container callingContainer = this.objectloader.containers.get(containerId);
        switch(vehicleType)
        {
            case "AGV" : 
                break;
            case "train" : 
//                for(int i = 0; i < objectloader.train.trainCarts.size(); i++){
//                    objectloader.train.trainCarts.remove(i);
//                }
//                objectloader.train.node.removeFromParent();
//                for(WorldObject traincart: objectloader.train.trainCarts){
//                    attachedContainers = getAttachedContainers(traincart);
//                    for(Container container : attachedContainers)
//                    {
//                        container.operationDone();
//                    }
//                }
                objectloader.train.moveOut();
                callingContainer.operationDone();
                break;
            case "seaShip" : 
                break;
            case "bargeShip" : 
                break;
            case "truck" : 
                WorldObject truck = this.objectloader.vehicles.remove(vehicleId);
                truck.node.removeFromParent();
                callingContainer.operationDone();
                break;
        };
    }
    
    private List<Container> getAttachedContainers(WorldObject vehicleObject){
        List<Container> containerList = new ArrayList<>();
        for(Container container :  this.objectloader.containers.values()){
            //check if this container is attached to the vehicle
            System.out.println(container.getVehicle() + " == " + vehicleObject);
            if(container.getVehicle() == vehicleObject){
                //despawn vehicle and container
                containerList.add(container);
            }
        }
        return containerList;
    }
    private void craneMoveContainer(JSONObject jsonObject)
    {
        Vector3f targetVec = null;
        int craneId = jsonObject.getInt("craneId");
        int containerId = jsonObject.getInt("containerId");
        Crane crane = this.objectloader.cranes.get(craneId);
        Container container = this.objectloader.containers.get(containerId);
        Object target = jsonObject.get("target");
        try
        {
            JSONArray targetIndex = (JSONArray) target;
            int sortFieldId = jsonObject.getInt("sortField");
            int x = targetIndex.getInt(0);
            int y = targetIndex.getInt(1);
            int z = targetIndex.getInt(2);
            targetVec = this.objectloader.sortFields[sortFieldId].indexToCoords(x, y, z);
        } catch (ClassCastException ex) {
            int agvId = (int) target;
            targetVec = this.objectloader.agvs.get(agvId).getPosition().add(0.0f, 1.2f, 0.0f);
        }        
        crane.moveContainer(container, targetVec);
    }

    private void spawnTruck(JSONObject jsonObject)
    {
        Container container = this.objectloader.addContainer(jsonObject.getInt("container"), this);
        int id = jsonObject.getInt("id");
        JSONArray pos = jsonObject.getJSONArray("position");
        float x = (float) pos.getDouble(0);
        float y = (float) pos.getDouble(1);
        float z = (float) pos.getDouble(2);
        Vector3f position = new Vector3f(x, y, z);
        
        this.objectloader.spawnTruck(id, container, position);
    }

    public void queueCommand(JSONObject input)
    {
        this.commandQueue.add(input);
    }
    
    public void executeQueued()
    {
        if (!this.commandQueue.isEmpty())
        {
            for(; 0 < commandQueue.size(); )
            {
                this.executeCommand(commandQueue.get(0));
                this.commandQueue.remove(0);
            }
        }
    }
}