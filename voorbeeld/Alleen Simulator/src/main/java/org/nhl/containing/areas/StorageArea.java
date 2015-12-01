package org.nhl.containing.areas;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import org.nhl.containing.cranes.StorageCrane;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import org.nhl.containing.Container;

public class StorageArea extends Area {

    private List<StorageCrane> storageCranes = new ArrayList();
    private List<Container> containers = new ArrayList();
    private List<List<Vector3f>> storageLanes = new ArrayList();
    private List<List<Container>> filledPositions = new ArrayList();
    private List<Container> bottomContainers;
    private AssetManager assetManager;
    private int craneXAxis = 0;
    private float craneRailsXAxis = 2.5f;
    private int craneRailsZAxis = 0;
    private int cranes;
    private int lane;
    private int n;
    private int index;
    private int beginLocation;
    private int containerCounter = 0;
    private float loc = -8;
    private float x;
    private float y = 0;
    private float z = 6.5f;
    private Vector3f position;
    private Calendar calNew = GregorianCalendar.getInstance();
    private Calendar calOld = GregorianCalendar.getInstance();

    public StorageArea(AssetManager assetManager, int cranes) {
        this.assetManager = assetManager;
        this.cranes = cranes;
        initStorageArea();
    }

    /**
     * Initialize a storage area.
     */
    private void initStorageArea() {

        // Add storage cranes to the list and scene.
        for (int i = 0; i < cranes; i++) {
            storageCranes.add(new StorageCrane(assetManager, this));
            storageCranes.get(i).setLocalTranslation(craneXAxis, 0, 0);
            storageCranes.get(i).setId(i);
            this.attachChild(storageCranes.get(i));
            craneXAxis += 50;
        }

        // Add crane rails.
        Spatial craneRails = assetManager.loadModel("Models/rails/stripRails.j3o");
        for (int i = 0; i < cranes; i++) {
            for (int j = 0; j < 22; j++) {
                Spatial nextRail = craneRails.clone();
                nextRail.setLocalTranslation(craneRailsXAxis, 0, craneRailsZAxis);
                this.attachChild(nextRail);
                craneRailsZAxis += 11;
            }
            craneRailsZAxis = 0;
            craneRailsXAxis += 50;
        }

        // Create for every crane a list with vectors for containers.
        for (int i = 0; i < cranes; i++) {
            n = i;
            List<Vector3f> containerSpots = new ArrayList();
            x = getStorageCranes().get(i).getLocalTranslation().x + loc;
            for (int j = 0; j < 540; j++) {
                containerSpots.add(getNextSpot());
            }
            storageLanes.add(containerSpots);
            y = 0;
            z = 20;
        }

        // Fill filledSpots List with dummy containers to make it possible to compare them with other containers.
        for (int i = 0; i < storageLanes.size(); i++) {
            List<Container> filledSpots = new ArrayList();
            for (int j = 0; j < 540; j++) {
                filledSpots.add(null);
            }
            filledPositions.add(filledSpots);
        }

    }

    /**
     * Get the next location for the containers.
     */
    private Vector3f getNextSpot() {

        z += 13.5f;
        if (z >= 222.5f) {
            z = 20;
            x += 2.5f;
        }

        if (x >= getStorageCranes().get(n).getLocalTranslation().x + 15f + loc) {
            y += 2.9f;
            x = getStorageCranes().get(n).getLocalTranslation().x + loc;
            z = 20;

        }

        containerCounter++;
        return new Vector3f(x, y, z);
    }

    /**
     * Get a spot for the container in the storage.
     *
     * @param container Container that needs a spot.
     * @return Vector3f of the spot.
     */
    public Vector3f getPosition(Container container) {
        this.position = null;
        this.lane = 0;
        this.beginLocation = 0;

        calNew.setTime(container.getDepartureDate());
        calNew.set(Calendar.MILLISECOND, 0);
        int location = 0;

        // Get starting lane and location.
        for (int i = 0; i < calNew.get(Calendar.HOUR_OF_DAY); i++) {
            if (location <= 60) {
                location += 15;
            } else {
                location = 0;
                lane += 1;
            }
        }
        checkLane(lane);
        if (!bottomContainers.isEmpty()) {
            index = filledPositions.get(lane).indexOf(bottomContainers.get(0));
            getHeight(container, 0, lane, index);
        } else {
            this.position = getEmptySpot(lane);
        }
        filledPositions.get(lane).set(storageLanes.get(lane).indexOf(position), container);
        return this.position;

    }

    /**
     * Check the bottom of the storage for containers with the same departure
     * time. And put them all into a List.
     *
     * @param lane The lane of the current container.
     */
    private void checkLane(int lane) {
        bottomContainers = new LinkedList();
        for (int i = 0; i < 89; i++) {
            if (filledPositions.get(lane).get(i) != null) {
                calOld.setTime(filledPositions.get(lane).get(i).getDepartureDate());
                calOld.set(Calendar.MILLISECOND, 0);
            }
            if (filledPositions.get(lane).get(i) != null && calOld.equals(calNew)) {
                bottomContainers.add(filledPositions.get(lane).get(i));
            }
        }
        for (int i = 0; i < 89; i++){
                if(filledPositions.get(lane).get(i) != null && calOld.after(calNew)){
                bottomContainers.add(filledPositions.get(lane).get(i));
            }
        }
    }

    /**
     * Check how high the container can get. If there's already 6 containers on
     * top of eachother try for another container or find an empty spot.
     *
     * @param container Container that needs a spot.
     * @param heightMod Starting height.
     * @param lane Current lane.
     * @param location Location of the container with the same departureTime.
     */
    private void getHeight(Container container, int heightMod, int lane, int location) {
        
        if (!bottomContainers.isEmpty()) {
            beginLocation = filledPositions.get(lane).indexOf(bottomContainers.get(0));
            if(filledPositions.get(lane).get(beginLocation + heightMod) != null) {
                calOld.setTime(filledPositions.get(lane).get(beginLocation + heightMod).getDepartureDate());
                calOld.set(Calendar.MILLISECOND, 0);
            }
        }
        if (filledPositions.get(lane).get(beginLocation + heightMod) == null) {
            filledPositions.get(lane).set(beginLocation + heightMod, container);
            this.position = storageLanes.get(lane).get(beginLocation + heightMod);
        } else if (filledPositions.get(lane).get(beginLocation + heightMod) != null && heightMod <= beginLocation + 360 && (calOld.equals(calNew) || calOld.after(calNew))) {
            getHeight(container, heightMod + 90, lane, beginLocation);
        } else if (bottomContainers.size() > 1 && beginLocation + heightMod > 360 || bottomContainers.size() > 1 && !(calOld.equals(calNew) || calOld.after(calNew))) {
            bottomContainers.remove(0);
            getHeight(container, 0, lane, filledPositions.get(lane).indexOf(bottomContainers.get(0)));
        } else {
            this.position = getEmptySpot(lane);
        }
    }

    /**
     * Find an empty spot in the surrent lane.
     *
     * @param lane Current lane.
     * @return The first empty spot found.
     */
    private Vector3f getEmptySpot(int lane) {
        int a = 0;
        for (int j = 0; j <= 3; j++) {
            for (int i = 0; i < 89; i++) {

                if (lane + a > 3) {
                    a = 0;
                }

                if (filledPositions.get(lane + a).get(i) == null) {
                    return storageLanes.get(lane + a).get(i);
                }
            }
            a++;
        }
        return null;
    }

    public List<StorageCrane> getStorageCranes() {
        return storageCranes;
    }

    public List<List<Container>> getFilledPositions() {
        return filledPositions;
    }

    public void addContainer(Container container) {
        containers.add(container);
    }

    public void removeContainer(Container container) {
        containers.remove(container);
    }
}
