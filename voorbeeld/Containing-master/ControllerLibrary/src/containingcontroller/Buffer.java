/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingcontroller;

import containing.xml.CustomVector3f;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Ruben
 */
public class Buffer {

    /**
     * ID of buffer incremental
     */
    public static int ID = 1;
    /**
     * Id of buffer
     */
    public String id;
    /**
     * Array of all containers
     */
    public Container[][][] containers;
    /**
     * List of all owned AGVs
     */
    public ArrayList<AGV> ownedAGV;
    /**
     * Buffer crane
     */
    public Crane crane;
    /**
     * Hashmap for reserved spaces
     */
    public HashMap<Container, CustomVector3f> reservedSpace;
    /**
     * PathNode
     */
    public PathNode pathNodeUp;
    /**
     * PathNode
     */
    public PathNode pathNodeDown;

    /**
     * Constructor
     *
     * @param crane
     * @param ownedAGV
     */
    public Buffer(ArrayList<AGV> ownedAGV, Crane crane) {
        this.id = "BFR" + String.format("%03d", Buffer.ID++);
        this.containers = new Container[26][6][6];
        this.ownedAGV = new ArrayList<AGV>(ownedAGV);
        this.reservedSpace = new HashMap<Container, CustomVector3f>();
        this.crane = crane;
    }

    /**
     * Create buffer with id
     */
    public Buffer() {
        this.id = "BFR" + String.format("%03d", Buffer.ID++);
        this.ownedAGV = new ArrayList<AGV>();
        this.reservedSpace = new HashMap<Container, CustomVector3f>();
        this.containers = new Container[26][6][6];
    }

    /**
     * Checks the list if there are containers to depart
     *
     * @param date
     * @return ArrayList<Container>
     */
    public ArrayList<Container> checkDepartingContainers(Date date) {
        ArrayList<Container> departingContainers = new ArrayList<Container>();
        for (Container[][] containerArray3 : containers) {
            for (Container[] containerArray2 : containerArray3) {
                for (Container container : containerArray2) {
                    if (container != null) {
                        if (container.getDateDeparture().before(date) || container.getDateDeparture().equals(date)) {
                            departingContainers.add(container);
                        }
                    }
                }
            }
        }
        return departingContainers;
    }

    /**
     * Checks the best place for container
     *
     * @param container
     * @param up set true if the posistion must be on the up side
     * @return Vector3f
     */
    public CustomVector3f findBestBufferPlace(Container container, boolean up) {
        if (up) {
            for (int x = 0; x < 26; x++) {
                for (int z = 0; z < 6; z++) {
                    for (int y = 0; y < 6; y++) {
                        if (containers[x][y][z] == null && !checkSpaceReserved(x, y, z)) {
                            if (y > 0 && containers[x][y - 1][z] != null) {
                                if (containers[x][y - 1][z].getDateDeparture().after(container.getDateDeparture())) {
                                    return new CustomVector3f(x, y, z);
                                }
                            } else if (y == 0) {
                                return new CustomVector3f(x, y, z);
                            }
                        }
                    }
                }
            }
        } else {
            for (int x = 25; x >= 0; x--) {
                for (int z = 0; z < 6; z++) {
                    for (int y = 0; y < 6; y++) {
                        if (containers[x][y][z] == null && !checkSpaceReserved(x, y, z)) {
                            if (y > 0 && containers[x][y - 1][z] != null) {
                                if (containers[x][y - 1][z].getDateDeparture().after(container.getDateDeparture())) {
                                    return new CustomVector3f(x, y, z);
                                }
                            } else if (y == 0) {
                                return new CustomVector3f(x, y, z);
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * Check if space has been reserverd for other container
     *
     * @param x
     * @param y
     * @param z
     * @return
     */
    public boolean checkSpaceReserved(float x, float y, float z) {
        for (Map.Entry<Container, CustomVector3f> e : reservedSpace.entrySet()) {
            CustomVector3f value = e.getValue();
            if (value.x == x && value.y == y && value.z == z) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds a container to buffer
     *
     * @param container
     */
    public void addContainer(Container container) {
        if (reservedSpace.containsKey(container)) {
            containers[(int) container.getBufferPosition().x][(int) container.getBufferPosition().y][(int) container.getBufferPosition().z] = container;
            reservedSpace.remove(container);
        }
    }

    /**
     * Removes container
     *
     * @param container
     */
    public void removeContainer(Container container) {
        for (int x = 0; x < 26; x++) {
            for (int z = 0; z < 6; z++) {
                for (int y = 0; y < 6; y++) {
                    if (containers[x][y][z] != null) {
                        if (containers[x][y][z].getId().equalsIgnoreCase(container.getId())) {
                            containers[x][y][z] = null;
                        }
                    }
                }
            }
        }
    }

    /**
     * Reserves container position
     *
     * @param container
     */
    public void reservePosition(Container container) {
        reservedSpace.put(container, container.getBufferPosition());
    }

    @Override
    public String toString() {
        return "Buffer{" + "id=" + id + ", containers=" + containers + ", crane=" + crane.id + ", reservedSpace=" + reservedSpace + ", pathNodeUp=" + pathNodeUp.getId() + ", pathNodeDown=" + pathNodeDown.getId() + '}';
    }

    /**
     * Check if there is an AGV Abailable from up or down-side
     *
     * @param up
     * @return
     */
    public AGV AGVAvailable(boolean up) {
        for (AGV a : ownedAGV) {
            if (a.isIsHome() && a.isReady()
                    && ((up && a.home.getId().toLowerCase().contains("BFA".toLowerCase()))
                    || (!up && a.home.getId().toLowerCase().contains("BFB".toLowerCase())))) {

                return a;
            }
        }
        return null;
    }

    /**
     * get number of containers in buffer
     *
     * @return
     */
    public int getContainerCount() {
        int counter = 0;
        for (int x = 0; x < 26; x++) {
            for (int z = 0; z < 6; z++) {
                for (int y = 0; y < 6; y++) {
                    if (containers[x][y][z] != null) {
                        counter++;
                    }
                }
            }
        }
        return counter;
    }
}
