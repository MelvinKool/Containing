package Simulator;

import Simulator.cranes.Crane;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class CommandHandler
{
    ObjectLoader objectloader;
    
    public CommandHandler(ObjectLoader objectloader)
    {
        this.objectloader = objectloader;
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
                //objectloader.agvs.get(vehicleId).setPath(Locations, totalDistance);
                break;   
            case "craneMoveContainer":
                this.craneMoveContainer(jsonObject);
                break;
            case "spawnObjects":
                System.out.println("spawn");
                this.objectloader.spawnObjectList = jsonObject.getJSONArray("objects");
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
        float x = (float) target.getDouble(0);
        float y = (float) target.getDouble(1);
        float z = (float) target.getDouble(2);
        Vector3f targetVec = new Vector3f(x, y, z);
        
        crane.moveContainer(container, targetVec);
    }
}