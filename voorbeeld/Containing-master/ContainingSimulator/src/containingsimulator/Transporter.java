/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingsimulator;

import containing.xml.SimContainer;
import com.jme3.asset.AssetManager;
import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.MotionPathListener;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Spline;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import containing.xml.CustomVector3f;
import java.util.ArrayList;

/**
 *
 * @author Ruben
 */
public class Transporter extends Node implements MotionPathListener {

    /**
     * Container array
     */
    private Container[][][] containers;
    /**
     * position of transporter
     */
    public Vector3f position;
    /**
     * transporter type
     */
    public int type;
    /**
     * transporter id
     */
    public String id;
    /**
     * SEASHIP Geometry
     */
    public static Spatial SEASHIP;
    /**
     * BARGE Geometry
     */
    public static Spatial BARGE;
    /**
     * TROLLEY Geometry
     */
    public static Spatial LORRY;
    /**
     * TRAIN Geometry
     */
    public static Geometry TRAIN;
    /**
     * SEASHIP Box
     */
    public static Box SEASHIPb;
    /**
     * BARGE Box
     */
    public static Box BARGEb;
    /**
     * TROLLEY Box
     */
    public static Box LORRYb;
    /**
     * TRAIN Box
     */
    public static Box TRAINb;
    private MotionEvent motionEvent;
    private float speed;
    MotionPath path = new MotionPath();
    Vector3f size;

    /**
     * Constructor
     *
     * @param containersList a list of SimContainers to create Containers from
     * @param position position of this transporter inside the simulation
     * @param type type of transporter, see TransportTypes class for identifiers
     */
    public Transporter(String id, ArrayList<SimContainer> containersList, Vector3f position, int type) {
        super(id);
        this.id = id;
        this.position = position.clone();
        this.type = type;

        Geometry currentGeometry = null;
        Spatial currentSpatial = null;



        switch (type) {
            case TransportTypes.SEASHIP:
                containers = new Container[20][6][16];
                currentSpatial = SEASHIP.clone();
                size = new Vector3f(18.3f, 12.2f, 134.1f);
                this.position.y -= 10f;
                this.position.x -= 40f;
                this.speed = 11;
                break;
            case TransportTypes.BARGE:
                containers = new Container[12][4][4];
                currentSpatial = BARGE.clone();
                size = new Vector3f(4.88f, -10f, 90f);
                this.rotate(0, 90 * FastMath.DEG_TO_RAD, 0);
                //position.y -= 10f;
                this.position.z += 20f;
                this.speed = 11;
                break;
            case TransportTypes.TRAIN:
                containers = new Container[30][1][1];
                currentGeometry = TRAIN.clone();
                size = new Vector3f(TRAINb.xExtent, TRAINb.yExtent, TRAINb.yExtent);
                this.position.z -= 13f;
                this.rotate(0, 90 * FastMath.DEG_TO_RAD, 0);
                this.speed = 300;
                break;
            default:
            case TransportTypes.LORRY:
                containers = new Container[2][1][1];
                currentSpatial = LORRY.clone();
                size = new Vector3f(1.22f, 1.5f, 6.705f);
                this.speed = 22;
                break;
        }

        Vector3f first = new Vector3f(this.position);

        switch (type) {
            case TransportTypes.SEASHIP:
                first.z -= 200f;
                break;
            case TransportTypes.BARGE:
                first.x -= 200f;
                break;
            case TransportTypes.TRAIN:
                first.x += 1000f;
                break;
            default:
            case TransportTypes.LORRY:
                first.z += 50f;
                break;
        }


        for (SimContainer container : containersList) {

            try {
                this.containers[(int) container.getIndexPosition().x][(int) container.getIndexPosition().y][(int) container.getIndexPosition().z] = new Container(container);
            } catch (Exception e) {
                System.err.println(e.getMessage() + " " + container.getIndexPosition());
            }

        }
        path.setCycle(false);
        path.setPathSplineType(Spline.SplineType.Linear);
        path.addListener(this);
        path.addWayPoint(first);
        if (type == TransportTypes.TRAIN) {
            path.addWayPoint(first.subtract(this.position.x, 0, 0));
        }
        path.addWayPoint(this.position);

        motionEvent = new MotionEvent(this, this.path);

        motionEvent.setInitialDuration(path.getLength() / speed / Main.globalSpeed);




        for (int z = 0; z < containers[0][0].length; z++) {
            for (int x = 0; x < containers.length; x++) {
                for (int y = 0; y < containers[0].length; y++) {
                    if (containers[x][y][z] != null) {
                        Container con = containers[x][y][z];
                        Vector3f vec = con.getLocalTranslation();
                        vec.y += 1.5f + size.y;
                        vec.x -= size.x - 1.22f;
                        //vec.z += size.z;

                        this.attachChild(con);
                    }
                }
            }
        }
        setRendering();

        this.setLocalTranslation(this.position);
        if (currentGeometry != null) {
            this.attachChild(currentGeometry);
        } else if (currentSpatial != null) {
            this.attachChild(currentSpatial);
        }

        this.motionEvent.play();
    }

    /**
     * When called, places a container on the Transporter. null can be sent.
     *
     * @param container The container to be placed on the Transporter.
     * @return True if the container was received, False if there already is a
     * container.
     */
    public boolean setContainer(Container container) {

        Vector3f pos = new Vector3f(container.indexPosition.x, container.indexPosition.y, container.indexPosition.z);
        if (containers[(int) pos.x][(int) pos.y][(int) pos.z] == null) {
            containers[(int) pos.x][(int) pos.y][(int) pos.z] = container;
            Vector3f vec = container.getLocalTranslation();
            vec.y += 1.5f + size.y + (pos.y * 2.44f);
            vec.x -= size.x - 1.22f - (pos.z * 2.44f);
            vec.z += (pos.x * 14f);
            if (this.type == TransportTypes.BARGE || this.type == TransportTypes.TRAIN) {
                container.setLocalRotation(new Quaternion().fromAngles(0f, 0f, 0f));
            }
            setRendering();
            this.attachChild(container);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Gets the container currently on the Transporter, and removes it from the
     * Transporter.
     *
     * @return the container that was once on the AGV. Null if the Transporter
     * was empty.
     */
    public Container getContainer(Vector3f position) {
        Container tempCont = null;
        if (this.containers[(int) position.x][(int) position.y][(int) position.z] != null) {
            tempCont = (Container) this.containers[(int) position.x][(int) position.y][(int) position.z].clone();
            this.detachChild(this.containers[(int) position.x][(int) position.y][(int) position.z]);
            this.containers[(int) position.x][(int) position.y][(int) position.z] = null;
            setRendering();
        }

        return tempCont;
    }

    /**
     * Finds and returns a container by container ID
     *
     * @param id the id to search for
     * @return reference to a container that matches the ID
     */
    public Container getContainerByID(String id) {
        Container tempCont = null;
        for (int i = 0; i < containers.length; i++) {
            for (int j = 0; j < containers[0].length; j++) {
                for (int k = 0; k < containers[0][0].length; k++) {
                    if (containers[i][j][k] != null && containers[i][j][k].id.equalsIgnoreCase(id)) {
                        tempCont = containers[i][j][k];
                        return tempCont;
                    }
                }
            }
        }
        return tempCont;
    }

    /**
     * Find the actual position of a container inside this simulation
     *
     * @param indexpos index of the container for which to find the position
     * inside the simulation
     * @return the position of the container with this index inside the
     * simulation
     */
    public Vector3f getRealContainerPosition(Vector3f indexpos) {
        if (this.containers[(int) indexpos.x][(int) indexpos.y][(int) indexpos.z] != null) {
            return this.containers[(int) indexpos.x][(int) indexpos.y][(int) indexpos.z].getWorldTranslation();
        } else {
            Container c = new Container("temp", Vector3f.ZERO, new Vector3f(1.22f, 1.22f, 6.705f));
            c.setIndexPosition(indexpos);
            setContainer(c);
            Vector3f res = c.getWorldTranslation().clone();
            getContainer(indexpos);
            return res;
        }
    }

    /**
     * Set which geometries have to be drawn and which not
     */
    private void setRendering() {
        //cull walled-in containers
        for (int i = 0; i < containers.length; i++) {
            for (int j = 0; j < containers[0].length; j++) {
                for (int k = 0; k < containers[0][0].length; k++) {
                    if (containers[i][j][k] != null) {
                        containers[i][j][k].updateRendering(containers);
                    }
                }
            }
        }
    }

    public void globalSpeedChanged() {
        motionEvent.setInitialDuration(path.getLength() / speed / Main.globalSpeed);
    }

    /**
     * Move transporter to next waypoint and rotate it accordingly
     *
     * @param wayPointIndex the index of the waypoint to move towards
     */
    private void nextWaypoint(int wayPointIndex) {
        // this.lookAt(path.getWayPoint(wayPointIndex),Vector3f.UNIT_Y);
        motionEvent.setSpeed(0.5f);


    }

    /**
     * Moves transporter to next waypoint, stops transporter and sends READY to
     * controller once the end is reached
     *
     * @param motionControl MotionEvent class
     * @param wayPointIndex current index of list of waypoints
     */
    public void onWayPointReach(MotionEvent motionControl, int wayPointIndex) {
        if (wayPointIndex == path.getNbWayPoints() - 1) {
            path.clearWayPoints();
            this.setLocalTranslation(this.position);
            Main.sendReady(id);
        } else {
            nextWaypoint(wayPointIndex);
        }
    }

    /**
     * Prepare Geometry object IMPORTANT: call this before instantiating a
     * Transporter object!
     *
     * @param am the AssetManager to load materials from
     */
    public static void makeGeometry(AssetManager am) {
        SEASHIP = am.loadModel("Models/seaShip/seaShip.j3o");
        SEASHIP.setLocalScale(134.1f, 7f, 19.52f);
        SEASHIP.rotate(0, -90f * FastMath.DEG_TO_RAD, 0);
        SEASHIP.setLocalTranslation(0, 19.5f, 128f);

        BARGE = am.loadModel("Models/barge/barge.j3o");
        BARGE.setLocalScale(50f, 1f, 5.5f);
        BARGE.rotate(0, -90f * FastMath.DEG_TO_RAD, 0);
        BARGE.setLocalTranslation(0, -11f, 40f);

        LORRY = am.loadModel("Models/lorry/lorry.j3o");
        LORRY.setLocalScale(1f, 1f, 1f);
        LORRY.setLocalTranslation(0, 1f, 0f);

        //LORRYb = new Box(Vector3f.ZERO, 1.22f, 0.5f, 6.705f); //container size in m divided by 2 because box size grows both ways
        //LORRY = new Geometry("Lorry", LORRYb);
        TRAINb = new Box(new Vector3f(0, 0, 190f), 1.22f, 0.5f, 200f); //container size in m divided by 2 because box size grows both ways
        TRAIN = new Geometry("Train", TRAINb);

        Material mat = new Material(am, "Common/MatDefs/Misc/Unshaded.j3md");
        Material seaShipMat = new Material(am, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture seaShipText = am.loadTexture("Textures/seaShip/seaShip.png");
        seaShipMat.setTexture("ColorMap", seaShipText);
        Material bargeMat = new Material(am, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture bargeText = am.loadTexture("Textures/barge/barge.png");
        bargeMat.setTexture("ColorMap", bargeText);
        mat.setColor("Color", ColorRGBA.Gray);



        SEASHIP.setMaterial(seaShipMat);
        BARGE.setMaterial(bargeMat);
        LORRY.setMaterial(mat);
        TRAIN.setMaterial(mat);
    }
}