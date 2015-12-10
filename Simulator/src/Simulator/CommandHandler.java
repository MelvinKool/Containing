/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulator;

import com.jme3.math.Vector3f;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.json.JSONObject;
import org.json.JSONString;
import org.json.JSONStringer;

/**
 *
 * @author Klaas
 */
public class CommandHandler {
    ObjectLoader objectloader;
    public CommandHandler(ObjectLoader objectloader){
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
                
                JSONObject route = jsonObject.getJSONObject("Route");
                System.out.println(route.toString());
                Iterator x = route.keys();
                List Locations = new ArrayList<Vector3f>();
                float X,Y,Z;
                while(x.hasNext())
                {
                    System.out.println("test");
                    JSONObject node = route.getJSONObject((String)x.next());
                    X = (float)node.getDouble("X");
                    Y = (float)node.getDouble("Y");
                    Z = (float)node.getDouble("Z");
                    System.out.println("X = " + X + "Y = " + Y + "Z=" + Z);
                    Locations.add(new Vector3f(X,Y,Z));
//                    JSONObject node = (JSONObject)x.next();
//                    System.out.println(node.toString());
                }
                objectloader.agvs.get(vehicleId).setPath(Locations);
                break;
        }
    }
}
