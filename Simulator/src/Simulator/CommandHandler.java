package Simulator;

import Simulator.cranes.Crane;
import Simulator.vehicles.AGV;
import com.jme3.math.Vector3f;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class CommandHandler
{
    ObjectLoader objectloader;
    private List<String> commandQueue;
    
    public CommandHandler(ObjectLoader objectloader)
    {
        this.objectloader = objectloader;
        this.commandQueue = new ArrayList<>();
    }
    
    public void setContainerCommands(JSONObject commands) {
        int containerId = commands.getInt("container");
        Container container = this.objectloader.containers.get(containerId);
        List<JSONObject> commandList = (List<JSONObject>) commands.getJSONArray("commands").iterator();
        container.setCommands(commandList);        
    }
    
    public void executeCommand(JSONObject jsonObject) {
         String command = jsonObject.getString("Command");
        int vehicleId;
        switch(command)
        {
            case "containerCommands":
                this.setContainerCommands(jsonObject);
                break;
            case "moveTo":
                //code for parsing moveto
                //get the vehicle ID
                vehicleId = jsonObject.getInt("vehicleId");
                //get the route node
                JSONArray route = jsonObject.getJSONArray("Route");
                List Locations = new ArrayList<>();
                float x,y,z;
                for(Object coordinate : route)
                {
                    JSONArray coord = (JSONArray)coordinate;
                    x = (float)coord.getDouble(0);
                    y = (float)coord.getDouble(1);
                    z = (float)coord.getDouble(2);
                    Locations.add(new Vector3f(x,y,z));
                }
                float totalDistance = (float)jsonObject.getDouble("totalDistance");
                System.out.println("Locations list: "+Locations);
                objectloader.agvs.get(vehicleId).setPath(Locations, totalDistance);
                break;
            case "agvAttachContainer":
                int agvId = jsonObject.getInt("agvId");
                int containerId = jsonObject.getInt("containerId");
                AGV agv = this.objectloader.agvs.get(agvId);
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
                this.objectloader.spawnTrain(jsonObject.getJSONArray("containers"));
                break;
//            case "teleportObject":
//                vehicleId = jsonObject.getInt("vehicleId");
//                float rotation = (float)jsonObject.getDouble("rotation");
//                JSONObject teleportVector = jsonObject.getJSONObject("teleportLocation");
//                float telX,telY,telZ;
//                telX = (float)teleportVector.getDouble("X");
//                telY = (float)teleportVector.getDouble("Y");
//                telZ = (float)teleportVector.getDouble("Z");
//                objectloader.agvs.get(vehicleId).node.rotate(0, rotation*FastMath.DEG_TO_RAD, 0);
//                objectloader.agvs.get(vehicleId).node.setLocalTranslation(new Vector3f(telX, telY, telZ));                
//                break;
        }
    }
    
    public void ParseJSON(String json)
    {
        JSONObject jsonObject = new JSONObject(json);
        this.executeCommand(jsonObject);
    }

    private void craneMoveContainer(JSONObject jsonObject)
    {
        int craneId = jsonObject.getInt("craneId");
        int containerId = jsonObject.getInt("containerId");
        Crane crane = this.objectloader.cranes.get(craneId);
        Container container = this.objectloader.containers.get(containerId);
        JSONArray target = jsonObject.getJSONArray("target");
        int sortFieldId = jsonObject.getInt("sortField");
        int x = target.getInt(0);
        int y = target.getInt(1);
        int z = target.getInt(2);
        Vector3f targetVec = this.objectloader.sortFields[sortFieldId].indexToCoords(x, y, z);
        
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

    public void queueCommand(String input)
    {
        this.commandQueue.add(input);
    }
    
    public void executeQueued() {
        if (!this.commandQueue.isEmpty()) {
            for(int i = 0; i < this.commandQueue.size(); i++){
                String cmd = commandQueue.get(i);
                this.ParseJSON(cmd);
                this.commandQueue.remove(cmd);
            }
        }
    }
}