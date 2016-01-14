package Simulator;

import Simulator.cranes.Crane;
import Simulator.vehicles.AGV;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.CameraNode;
import com.jme3.scene.control.CameraControl;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class CommandHandler
{
    private ObjectLoader objectloader;
    private CameraNode camera;
   
    public CommandHandler(ObjectLoader objectloader, Camera cam)
    {
        this.objectloader = objectloader;
        this.camera = new CameraNode("cam", cam);
        camera.setControlDir(CameraControl.ControlDirection.SpatialToCamera);
        camera.rotate(0.0f, 90.0f * FastMath.DEG_TO_RAD, 0.0f);
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
            case "setCamera":
                int contId = jsonObject.getInt("container");
                Container container = this.objectloader.containers.get(contId);
                this.camera.removeFromParent();
                container.node.attachChild(camera);
                camera.setLocalTranslation(-30.0f, 5.0f, 0.0f);
                //camera.lookAt(camera.worldToLocal(container.node.getWorldTranslation(), null), new Vector3f(0,1,0));
                break;
            case "containerCommands":
                this.setContainerCommands(jsonObject);
                break;
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
            case "trainCam":
                this.camera.removeFromParent();
                this.objectloader.rootNode.attachChild(this.camera);
                this.camera.setLocalTranslation(1460.0f, 10.0f, -724.0f);
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

    private void spawnTruck(JSONObject jsonObject)
    {
        Container container = this.objectloader.addContainer(jsonObject.getInt("container"), this);
        JSONArray pos = jsonObject.getJSONArray("position");
        float x = (float) pos.getDouble(0);
        float y = (float) pos.getDouble(1);
        float z = (float) pos.getDouble(2);
        Vector3f position = new Vector3f(x, y, z);
        
        this.objectloader.spawnTruck(container, position);
    }
}