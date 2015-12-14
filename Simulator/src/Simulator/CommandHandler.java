package Simulator;

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
        switch(command)
        {
            case "moveTo":
                //code for parsing moveto
                //get the vehicle ID
                int vehicleId = jsonObject.getInt("vehicleId");
                //get the route node
                JSONArray route = jsonObject.getJSONArray("Route");
                List Locations = new ArrayList<Vector3f>();
                int x,y,z;
                for(Object coordinate : route)
                {
                    JSONArray coord = (JSONArray)coordinate;
                    x = coord.getInt(0);
                    y = coord.getInt(1);
                    z = coord.getInt(2);
                    Locations.add(new Vector3f(x,y,z));
                }
                
                objectloader.agvs.get(vehicleId).setPath(Locations);
                break;
        }
    }
}