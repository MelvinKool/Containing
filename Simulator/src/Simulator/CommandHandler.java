package Simulator;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONString;
import org.json.JSONStringer;

public class CommandHandler
{
    ObjectLoader objectloader;
    
    public CommandHandler(ObjectLoader objectloader)
    {
        this.objectloader = objectloader;
    }
    
    public void ParseJSON(String json)
    {
        JSONObject jsonObject = new JSONObject(json);
        String command = jsonObject.getString("Command");
        int vehicleId;
        switch(command)
        {
            case "moveTo":
                //code for parsing moveto
                //get the vehicle ID
                vehicleId = jsonObject.getInt("vehicleId");
                //get the route node
                JSONArray route = jsonObject.getJSONArray("Route");
                List Locations = new ArrayList<Vector3f>();
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
            case "transferContainer":
                break;
            case "spawnObject":
                break;
            case "teleportObject":
                vehicleId = jsonObject.getInt("vehicleId");
                float rotation = (float)jsonObject.getDouble("rotation");
                JSONObject teleportVector = jsonObject.getJSONObject("teleportLocation");
                float telX,telY,telZ;
                telX = (float)teleportVector.getDouble("X");
                telY = (float)teleportVector.getDouble("Y");
                telZ = (float)teleportVector.getDouble("Z");
                objectloader.agvs.get(vehicleId).node.rotate(0, rotation*FastMath.DEG_TO_RAD, 0);
                objectloader.agvs.get(vehicleId).node.setLocalTranslation(new Vector3f(telX, telY, telZ));                
                break;
        }
    }
}