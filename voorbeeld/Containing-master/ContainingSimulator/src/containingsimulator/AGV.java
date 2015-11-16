/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingsimulator;

import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.MotionPathListener;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Spline;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Line;
import java.util.ArrayList;

/**
 * Class which represents the AGV in simulator.
 *
 * @author Sietse
 */
public class AGV extends Node implements MotionPathListener {
    
    /**
     * The contianer that is mounted on the AGV.
     */
    public Container container;
    /**
     * ID string of the AGV
     */
    public String id;
    /**
     * Holds the current parking spot of the AGV.
     */
    public ParkingSpot pSpot;
    /**
     * Holds (if the parking spot is in a buffer) if the parking spot is in the up, or down position.
     */
    boolean up;
    /**
     * The crane at the end of the path.
     */
    private Crane targetCrane;
    /**
     * Controls the motionpath that the AGV uses.
     */
    public MotionEvent motionEvent;
    /**
     * Holds the path of the AGV.
     */
    private MotionPath path;
    /**
     * Model of the AGV.
     */
    private Spatial viewModel;
    /**
     * Playing boolean
     */
    public boolean pathWasPlaying = false;
    /**
     * Holds the geometry
     */
    public Geometry boundingGeom;
    /**
     * Holds the defect
     */
    private boolean defect = false;

    /**
     * Constructor. Inits all variable required.
     *
     * @param ID ID of the AGV.
     * @param viewModel Model of the AGV.
     */
    public AGV(String ID, Spatial viewModel) {
        super(ID);
        this.id = ID;
        this.viewModel = viewModel.clone();
        path = new MotionPath();
        path.setCycle(false);
        path.setPathSplineType(Spline.SplineType.Linear);
        path.setCurveTension(1f);
        this.path.addListener(this);
        motionEvent = new MotionEvent(this, this.path);
        Line l = new Line(new Vector3f(0, 2.5f, 5f), new Vector3f(0, 2.5f, 15f));
        l.setLineWidth(5f);
        
        boundingGeom = new Geometry("collisionLine", l);
        boundingGeom.setMaterial(Main.alpha);
        boundingGeom.setCullHint(cullHint.Always);
        this.attachChild(boundingGeom);
        this.attachChild(viewModel);
    }
    /**
     * Gets if the agv has a defect.
     * @return
     */
    public boolean getDefect()
    {
        return this.defect;
    }

    /**
     * Adds waypoints to the current list of waypoints.
     *
     * @param waypoints The waypoints to be added.
     * @param targetCrane  
     */
    public void addWaypoints(String[] waypoints, Crane targetCrane) {
        this.path.clearWayPoints();
        ArrayList<Vector3f> points = new ArrayList();
        points = OffsetRoute.applyOffset(waypoints, this.container == null);
        for (int i = 0; i < points.size(); i++) {
            this.path.addWayPoint(points.get(i));
        }
        this.targetCrane = targetCrane;
        motionEvent.setInitialDuration(path.getLength() / 11.11f / Main.globalSpeed);
        this.motionEvent.play();
        pathWasPlaying = true;
        if (this.pSpot != null) {
            this.pSpot.occupied = false;
        }
        this.setLocalRotation(Quaternion.ZERO.fromAngles(0f, OffsetRoute.getAngle(points.get(0), points.get(1)) + (FastMath.PI * 0.5f), 0f));
    }

    /**
     * Removes the current waypoint, the one reached, and makes the AGV snap
     * towards the next waypoint on the list.
     */
    private void nextWaypoint(int wayPointIndex) {
        this.lookAt(path.getWayPoint(wayPointIndex + 1), Vector3f.UNIT_Y);
        this.motionEvent.setSpeed((this.container == null ? 2f : 1f));

        /**
         * NOTE: Beware when setting speed. Speed is calculated by dividing the
         * speed var with base speed. actualSpeed =
         * motionPath.initialSpeed/motionPath.speed
         *
         * 11.11 m/s = 40 kph 5.55 m/s = 20 kph
         */
    }

    /**
     * When called, places a container on the AGV. null can be sent.
     *
     */
    public void globalSpeedChanged() {
        motionEvent.setInitialDuration(path.getLength() / 11.11f / Main.globalSpeed);
    }
    
    /**
     * pause or play this AGV's MotionPath
     * @param pause whether the simulation is paused or not
     */
    public void pausePlay(boolean pause) {
        if (pathWasPlaying) {
            if (!pause) {
                motionEvent.play();
            } else {
                motionEvent.pause();
            }
        }
        
        
    }
    
    /**
     * Loads a container onto this AGV
     * @param container the container to load to this AGV
     * @return true if loading container was successful, false if this AGV already carries a container
     */
    public boolean setContainer(Container container) {
        if (this.container == null) {
            this.container = container;
            this.attachChild(container);
            this.container.setLocalTranslation(0f, 4f, 0f);
            this.container.setLocalRotation(this.viewModel.getLocalRotation());
            return true;
        } else {
            return false;
        }
    }

    /**
     * Gets the container currently on the AGV, and removes it from the AGV.
     *
     * @return the container that was once on the AGV. Null if the AGV was
     * empty.
     */
    public Container getContainer() {
        Container tempCont;
        tempCont = (Container) this.container.clone();
        this.detachChild(this.container);
        this.container = null;
        
        return tempCont;
    }

    /**
     * @return null if there is no container. Returns the ID if there is one.
     */
    public Container getContainerObject() {
        return this.container;
    }

    /**
     * @return null if there is no container. Returns the ID if there is one.
     */
    public String getContainerID() {
        return container == null ? null : container.id;
    }

    /**
     * Moves AGV to next waypoint, stops AGV and sends READY to controller once
     * the end is reached
     *
     * @param motionControl MotionEvent class
     * @param wayPointIndex current index of list of waypoints
     */
    public void onWayPointReach(MotionEvent motionControl, int wayPointIndex) {
        if (wayPointIndex + 1 == path.getNbWayPoints()) {
           if(targetCrane != null){
            if (targetCrane instanceof BufferCrane) {
                jumpToPark(((BufferCrane) targetCrane).getBuffer().getBestParkingSpot(up));
            } else if (targetCrane instanceof LorryCrane) {
                jumpToPark(targetCrane.getParkingspot());
            } else {
                jumpToPark(targetCrane.getParkingspot());
                this.setLocalRotation(targetCrane.getBaseRotation());
            }
           }
            pathWasPlaying = false;
            Main.sendReady(id);
        } else {
            nextWaypoint(wayPointIndex);
        }
        
    }

    /**
     * Snaps AGV to parking spot location and sets its rotation
     *
     * @param spot the parking spot which to snap the AGV to
     */
    public void jumpToPark(ParkingSpot spot) {
        this.setLocalTranslation(spot.translation);
        this.setLocalRotation(new Quaternion().fromAngles(0f, spot.rotation, 0f));
        this.pSpot = spot;
        pSpot.occupied = true;
    }
    
    /**
     *simulates defect for this AGV
     * switches boolean defect
     * pauses motionpath if was playing
     * returns boolean motionpath playing
     * @return
     */
    public boolean simulateDefect()
    {
        this.defect = !defect;
        this.pausePlay(defect);
        return pathWasPlaying;
    }
}