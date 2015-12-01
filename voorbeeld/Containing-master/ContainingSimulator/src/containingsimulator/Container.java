/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingsimulator;

import containing.xml.SimContainer;
import containing.xml.CustomVector3f;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * The container class for the simulation, only a visual representation with
 * some helper functions to make its usage inside the simulator easier
 *
 * @author Wessel
 */
public class Container extends Node {

    /**
     * container geometry, static to reduce memory load
     */
    public static Box b;
    /**
     * container material, static to reduce memory load
     */
    public static Material mat;
    /**
     * geometry object
     */
    public static Spatial geom;
    /**
     * RNG for color assignment
     */
    public static Random r;
    /**
     * container ID
     */
    public String id;
    /**
     * actual position in the simulation
     */
    public Vector3f realPosition;
    /**
     * position in transport/buffer
     */
    public Vector3f indexPosition;
    /**
     * size in m
     */
    public Vector3f size;
    /**
     * Container textures
     */
    public static HashMap<String, Texture> companyTextures = new HashMap<String, Texture>();
    /**
     * List of container colors
     */
    public static List<Texture> Colors = new ArrayList<Texture>();
    /**
     * Company name
     */
    public String company;

    /**
     * Constructor
     *
     * @param id container ID
     * @param realPosition actual position inside simulation
     * @param size size of this container
     */
    public Container(String id, Vector3f realPosition, Vector3f size) {
        //set properties
        this.id = id;
        this.realPosition = realPosition;
        this.size = size;

        setGeometry();
    }

    /**
     * Constructor for SimContainer
     *
     * @param sc SimContainer you want to create a Container for
     */
    public Container(SimContainer sc) {
        super(sc.getId());
        this.id = sc.getId();
        CustomVector3f cv = sc.getIndexPosition();
        setIndexPosition(new Vector3f(cv.x, cv.y, cv.z));
        this.size = new Vector3f(1.22f, 1.22f, 6.705f);
        this.realPosition = new Vector3f(indexPosition.z * 2 * size.x,
                indexPosition.y * 2 * size.y, indexPosition.x * 2 * size.z);
        company = sc.getBedrijf();
        setGeometry();
    }

    /**
     * Change indexPosition when moving container to buffer or back
     *
     * @param indexPosition position to set this container's index to
     */
    public void setIndexPosition(Vector3f indexPosition) {
        this.indexPosition = indexPosition;
    }

    /**
     * assign color,texture, geometry, etc. to this object
     */
    private void setGeometry() {
        this.setCullHint(Spatial.CullHint.Dynamic);
        this.setLocalTranslation(realPosition);
        //attach geometry to this object
        //     mat.setColor("Color", colors[r.nextInt(colors.length)]);
        if (!companyTextures.containsKey(this.company)) {
            mat.setTexture("ColorMap", Colors.get(r.nextInt(Colors.size())));
        } else {
            mat.setTexture("ColorMap", companyTextures.get(this.company));
        }
        geom.setMaterial(mat);
        this.attachChild(geom.clone());
    }

    /**
     * Count adjacent neighbours in a group of containers
     *
     * @param buffer the array in which to check for neighbours
     * @return the amount of neighbors the container has inside this array
     */
    private int getNeighbours(Container[][][] buffer) {
        int neighbours = 0;
        if (indexPosition.x + 1 < buffer.length && buffer[(int) indexPosition.x + 1][(int) indexPosition.y][(int) indexPosition.z] != null) {
            neighbours++;
        }
        if (indexPosition.x - 1 >= 0 && buffer[(int) indexPosition.x - 1][(int) indexPosition.y][(int) indexPosition.z] != null) {
            neighbours++;
        }
        if (indexPosition.y + 1 < buffer[0].length && buffer[(int) indexPosition.x][(int) indexPosition.y + 1][(int) indexPosition.z] != null) {
            neighbours++;
        }
        if (indexPosition.y - 1 >= 0 && buffer[(int) indexPosition.x][(int) indexPosition.y - 1][(int) indexPosition.z] != null) {
            neighbours++;
        }
        if (indexPosition.z + 1 < buffer[0][0].length && buffer[(int) indexPosition.x][(int) indexPosition.y][(int) indexPosition.z + 1] != null) {
            neighbours++;
        }
        if (indexPosition.z - 1 >= 0 && buffer[(int) indexPosition.x][(int) indexPosition.y][(int) indexPosition.z - 1] != null) {
            neighbours++;
        }
        return neighbours;
    }

    /**
     * Cull containers with 6 neighbours for optimization
     *
     * @param buffer buffer array in which to search for neighbours
     */
    public void updateRendering(Container[][][] buffer) {
        int neighbours = getNeighbours(buffer);
        if (neighbours == 6 || (neighbours == 5 && indexPosition.y == 0)) {
            this.setCullHint(Spatial.CullHint.Always);
        } else {
            this.setCullHint(Spatial.CullHint.Dynamic);
            //System.out.println("Cull at " + this.indexPosition.toString()); //debug
        }
    }

    /**
     * Prepare Geometry object IMPORTANT: call this before instantiating a
     * Container object!
     *
     * @param am the AssetManager to load materials from
     */
    public static void makeGeometry(AssetManager am) {
        geom = am.loadModel("Models/container/container.j3o");
        mat = new Material(am, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture Tex = am.loadTexture(
                "Textures/Container/Template/texture.png");


        Colors.add(am.loadTexture(
                "Textures/Container/blue.png"));
        Colors.add(am.loadTexture(
                "Textures/Container/green.png"));
        Colors.add(am.loadTexture(
                "Textures/Container/purple.png"));
        Colors.add(am.loadTexture(
                "Textures/Container/red.png"));
        companyTextures.put("UPS", am.loadTexture(
                "Textures/Container/UPS.png"));
        companyTextures.put("CocaCola", am.loadTexture(
                "Textures/Container/cocacola.png"));
        companyTextures.put("McDonalds", am.loadTexture(
                "Textures/Container/mac.png"));

        companyTextures.put("Grolsch", am.loadTexture(
                "Textures/Container/grolsch.png"));
        companyTextures.put("Heineken", am.loadTexture(
                "Textures/Container/heineken.png"));
        companyTextures.put("Nestle", am.loadTexture(
                "Textures/Container/nestle.png"));
        companyTextures.put("Shell", am.loadTexture(
                "Textures/Container/shell.png"));
        companyTextures.put("DeutschePost", am.loadTexture(
                "Textures/Container/post.png"));
        companyTextures.put("PostBank", am.loadTexture(
                "Textures/Container/postbank.png"));
        companyTextures.put("Airfrance", am.loadTexture(
                "Textures/Container/airfrance.png"));

        companyTextures.put("BallastNedam", am.loadTexture(
                "Textures/Container/ballastnedam.png"));
        companyTextures.put("Manpower", am.loadTexture(
                "Textures/Container/manpower.png"));

        companyTextures.put("Lufthansa", am.loadTexture(
                "Textures/Container/lufthansa.png"));
        companyTextures.put("Bayer", am.loadTexture(
                "Textures/Container/bayer.png"));


        companyTextures.put("Danone", am.loadTexture(
                "Textures/Container/danone.png"));
        companyTextures.put("Ericsson", am.loadTexture(
                "Textures/Container/ericsson.png"));

        companyTextures.put("Danone", am.loadTexture(
                "Textures/Container/danone.png"));
        companyTextures.put("Ericsson", am.loadTexture(
                "Textures/Container/ericsson.png"));


        companyTextures.put("OCE", am.loadTexture(
                "Textures/Container/oce.png"));
        companyTextures.put("BeterBed", am.loadTexture(
                "Textures/Container/beterbed.png"));

        companyTextures.put("TenCate", am.loadTexture(
                "Textures/Container/tencate.png"));

        companyTextures.put("FederalExpress", am.loadTexture(
                "Textures/Container/fedex.png"));
        companyTextures.put("IBM", am.loadTexture(
                "Textures/Container/IBM.png"));
        companyTextures.put("KraftFoods", am.loadTexture(
                "Textures/Container/kraft.png"));
        companyTextures.put("Hanjin", am.loadTexture(
                "Textures/Container/hanjin.png"));
        companyTextures.put("CargoTrans", am.loadTexture(
                "Textures/Container/cargotrans.png"));


        companyTextures.put("Metro", am.loadTexture(
                "Textures/Container/metro.png"));
        companyTextures.put("Carrefour", am.loadTexture(
                "Textures/Container/carefour.png"));
        companyTextures.put("Amstel", am.loadTexture(
                "Textures/Container/amstel.png"));
        companyTextures.put("TransNL", am.loadTexture(
                "Textures/Container/transnl.png"));
        companyTextures.put("Gilette", am.loadTexture(
                "Textures/Container/gillete.png"));


        companyTextures.put("WalMart", am.loadTexture(
                "Textures/Container/walmart.png"));
        companyTextures.put("Delhaize", am.loadTexture(
                "Textures/Container/delhaize.png"));
        companyTextures.put("BASF", am.loadTexture(
                "Textures/Container/basf.png"));
        companyTextures.put("SeaTrans", am.loadTexture(
                "Textures/Container/seatrans.png"));
        companyTextures.put("DowChemical", am.loadTexture(
                "Textures/Container/dow.png"));

        companyTextures.put("AXA", am.loadTexture(
                "Textures/Container/axe.png"));
        companyTextures.put("LLyod", am.loadTexture(
                "Textures/Container/lloyd.png"));
                
        companyTextures.put("GJMW", am.loadTexture(
                "Textures/Container/GJMW.png"));
        companyTextures.put("WoodNorge", am.loadTexture(
                "Textures/Container/woodnorge.png"));
        companyTextures.put("FlowersNL", am.loadTexture(
                "Textures/Container/flowersnl.png"));
        
        companyTextures.put("FruitINT", am.loadTexture(
                "Textures/Container/fruitint.png"));
        companyTextures.put("IntTrans", am.loadTexture(
                "Textures/Container/inttrans.png"));
        companyTextures.put("MaasHolland", am.loadTexture(
                "Textures/Container/maasholland.png"));

        mat.setTexture("ColorMap", Tex);
        r = new Random();

        mat.setColor("Color", ColorRGBA.White);

        geom.setMaterial(mat);
    }

    /**
     * Write this container as string
     *
     * @return container information inside a string
     */
    @Override
    public String toString() {
        return "Container{" + "id=" + id + ", realPosition=" + realPosition + ", indexPosition=" + indexPosition + ", size=" + size + '}';
    }
}