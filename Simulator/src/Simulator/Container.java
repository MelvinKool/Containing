package Simulator;

import com.jme3.asset.AssetManager;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.math.Vector3f;
import com.jme3.scene.*;
import java.util.List;
import org.json.JSONObject;

public class Container extends WorldObject
{
    public String containerId;
    public List<JSONObject> commands;
    
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
        JSONObject command = this.commands.remove(0);
        this.commandHandler.executeCommand(command);
    }
}