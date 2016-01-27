package Simulator;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.*;
import java.util.List;
import org.json.JSONObject;

public class Container extends WorldObject
{
    public String containerId;
    public List<JSONObject> commands;
    
    private WorldObject vehicle;
    private CommandHandler commandHandler;
    
    public Container(Node rootNode, AssetManager assetManager, Vector3f position, Spatial model, CommandHandler commandHandler)
    {
        super(rootNode, assetManager, position, model);
        this.commandHandler = commandHandler;
    }
    
    /**
     * Set a list of commands for this container
     * @param commands 
     */
    public void setCommands(List<JSONObject> commands) {
        this.commands = commands;
    }
    
    /**
     * Called by vehicles when they are done with their command
     */
    public void operationDone() {
        if (this.commands != null && !this.commands.isEmpty())
        {    
            System.out.println("container operation done. container: " + this.containerId);
            JSONObject command = this.commands.remove(0);
            this.commandHandler.queueCommand(command);
            this.vehicle = null;
        }
    }
    
    /**
     * Get vehicle currently carrying this container
     * @return vehicle
     */
    public WorldObject getVehicle() {
        return this.vehicle;
    }
    
    /**
     * Assign vehicle that is currently carrying container
     * @param vehicle 
     */
    public void setVehicle(WorldObject vehicle) {
        this.vehicle = vehicle;
    }
}