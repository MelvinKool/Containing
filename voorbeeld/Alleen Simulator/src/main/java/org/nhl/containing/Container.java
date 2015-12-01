package org.nhl.containing;

import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingBox;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.Date;

public class Container extends Node {

    private AssetManager assetManager;
    private String owner;
    private int containerID;
    private int spawnX;
    private int spawnY;
    private int spawnZ;
    private BoundingBox boundingBox;
    private Date departureDate;

    public Container(AssetManager assetManager, String owner, int containerID, int spawnX, int spawnY, int spawnZ, Date departureDate) {
        this.assetManager = assetManager;
        this.owner = owner;
        this.containerID = containerID;
        this.spawnX = spawnX;
        this.spawnY = spawnY;
        this.spawnZ = spawnZ;
        this.departureDate = departureDate;
        initContainer();
    }

    /**
     * Initialize a container.
     */
    private void initContainer() {
        // Load a model.
        Spatial container = assetManager.loadModel("Models/low/container/container.j3o");
        Material mat = new Material(assetManager,
                "Common/MatDefs/Misc/Unshaded.j3md");  // create a simple material
        mat.setColor("Color", ColorRGBA.White);   // set color of material to blue
        mat.setColor("Color", ColorRGBA.randomColor());
        container.setMaterial(mat);
        container.setCullHint(CullHint.Dynamic);
        this.attachChild(container);
        //create a random text color
        ColorRGBA textColor = ColorRGBA.Black;
        //place the company name on the side
        drawText(1.2f, 2.6f, 6, textColor, new Quaternion().fromAngleAxis(FastMath.PI / 2, new Vector3f(0, 1, 0)));
        drawText(-1.2f, 2.6f, -6, textColor, new Quaternion().fromAngleAxis(FastMath.PI * 1.5f, new Vector3f(0, 1, 0)));
        boundingBox = (BoundingBox) container.getWorldBound();
    }

    /**
     * This method writes the company name on the side of the container Has
     * parameters for placing the text on the right and left side.
     */
    public void drawText(float x, float y, float z, ColorRGBA color, Quaternion rotation) {
        BitmapFont guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        BitmapText companyText = new BitmapText(guiFont, false);
        companyText.setSize(1);      // font size
        companyText.setColor(color);                             // font color
        companyText.setText(owner);             // the text
        companyText.rotate(rotation);
        companyText.setLocalTranslation(x, y, z); // position
        companyText.setQueueBucket(RenderQueue.Bucket.Translucent);
        this.attachChild(companyText);
    }

    public int getContainerID() {
        return containerID;
    }

    public String getOwner() {
        return owner;
    }

    public int getSpawnX() {
        return spawnX;
    }

    public int getSpawnY() {
        return spawnY;
    }

    public int getSpawnZ() {
        return spawnZ;
    }

    public Date getDepartureDate() {
        return departureDate;
    }
    
    
     /**
     * Debug method, displays object name,owner, container ID, transporttype and location.
     * @return information about this object
     */
    public String getDebugInfo(){
        return this.getClass().getSimpleName() + "\nOwner: " + owner + "\nContainerID: " 
                + containerID + "\nLocation: " + this.getLocalTranslation() + 
                "\nLocallocation (X,Y,Z): " + spawnX + "," + spawnY + "," + spawnZ + "\n";
    }
    public BoundingBox getBoundingBox() {
        return boundingBox;
    }
    
}