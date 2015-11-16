/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingcontroller;

import containing.xml.CustomVector3f;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Hendrik
 */
class Transporter {

    public HashMap<Container, CustomVector3f> reservedSpace;

    private List<Container> containers;
    private int transportType;
    String id;
    static int tranporterID = 0;
    private Crane dockingPoint;
    private Date dateArrival;

    public Transporter(int transportType, Date dateArrival) {
        this.transportType = transportType;
        this.id = "TRS" + String.format("%03d", tranporterID++);
        this.dateArrival = dateArrival;
        this.reservedSpace = new HashMap<Container, CustomVector3f>();
        this.containers = new ArrayList<Container>();
    }

    public int getTransportType() {
        return transportType;
    }

    public CustomVector3f getFreeLocation(int start, int range) {
        if (transportType == TransportTypes.SEASHIP) {
            for (int x = start; x < start + range; x++) {
                for (int z = 0; z < 20; z++) {
                    for (int y = 0; y < 6; y++) {
                        CustomVector3f currentposition = new CustomVector3f(x, y, z);
                        boolean posistionTaken = false;
                        for (Container c : containers) {
                            if (c.getPosition().x == currentposition.x
                                    && c.getPosition().y == currentposition.y
                                    && c.getPosition().z == currentposition.z) {
                                posistionTaken = true;
                                break;
                            }
                        }
                        for (CustomVector3f c : reservedSpace.values()) {
                            if (c.x == currentposition.x
                                    && c.y == currentposition.y
                                    && c.z == currentposition.z) {
                                posistionTaken = true;
                                break;
                            }
                        }
                        if (!posistionTaken) {
                            return currentposition;
                        }
                    }
                }
            }
        } else if (transportType == TransportTypes.BARGE) {
            for (int x = start; x < start + range; x++) {
                for (int z = 0; z < 4; z++) {
                    for (int y = 0; y < 4; y++) {
                        CustomVector3f currentposition = new CustomVector3f(x, y, z);
                        boolean posistionTaken = false;
                        for (Container c : containers) {
                            if (c.getPosition().x == currentposition.x
                                    && c.getPosition().y == currentposition.y
                                    && c.getPosition().z == currentposition.z) {
                                posistionTaken = true;
                                break;
                            }
                        }
                        for (CustomVector3f c : reservedSpace.values()) {
                            if (c.x == currentposition.x
                                    && c.y == currentposition.y
                                    && c.z == currentposition.z) {
                                posistionTaken = true;
                                break;
                            }
                        }
                        if (!posistionTaken) {
                            return currentposition;
                        }
                    }
                }
            }

        } else if (transportType == TransportTypes.TRAIN) {
            for (int x = 0; x < 30; x++) {
                boolean posistionTaken = false;
                for (Container c : containers) {
                    if (c.getPosition().x == x) {
                        posistionTaken = true;
                        break;
                    }
                }

                for (CustomVector3f c : reservedSpace.values()) {
                    if (c.x == x) {
                        posistionTaken = true;
                        break;
                    }
                }

                if (!posistionTaken) {
                    return new CustomVector3f(x, 0, 0);
                }
            }
        } else if (transportType == TransportTypes.LORREY) {
            if (containers.size() > 0) {
                return null;
            } else {
                return new CustomVector3f(0, 0, 0);
            }
        } else {
            return null;
        }
        return null;
    }

    public boolean checkSpaceReserved(float x, float y, float z) {
        for (Map.Entry<Container, CustomVector3f> e : reservedSpace.entrySet()) {
            CustomVector3f value = e.getValue();
            if (value.x == x && value.y == y && value.z == z) {
                return true;
            }
        }
        return false;
    }

    public void loadContainer(Container container) {
        if (containers == null) {
            containers = new ArrayList<Container>();
        }
        /*
         boolean posistionTaken = false;
         for (Container c : containers) {
         if (c.getPosition().x == container.getPosition().x
         && c.getPosition().y == container.getPosition().y
         && c.getPosition().z == container.getPosition().z) {
         posistionTaken = true;
         break;
         }
         }
         */

        if (reservedSpace.containsKey(container)) {

            containers.add(container);
            reservedSpace.remove(container);

        }

    }

    public void RemoveContainer(Container c) {
        for (int i = 0; i < containers.size(); i++) {
            if (containers.get(i).getId().trim().equalsIgnoreCase(c.getId().trim())) {
                containers.remove(i);
                i--;
            }
        }
    }

    public int getContainerCount() {
        if (containers != null) {
            return containers.size();
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        String s = "Transporter{ ID=" + id + ",containers={";

        if (containers != null) {
            for (Container c : containers) {
                s += c.toString() + ",";
            }
        }
        s = s.substring(0, s.length() - 1);
        s += "}, transportType=" + TransportTypes.getTransportType(transportType) + '}';
        return s;
    }

    /**
     * Get containers from transporter
     *
     * @return
     */
    public List<Container> getContainers() {
        return containers;
    }

    /**
     * Get specific container from transporter
     *
     * @param i
     * @return
     */
    public Container getContainer(int i) {
        if (containers != null && i < containers.size() && i >= 0) {
            return containers.get(i);
        } else {
            return null;
        }
    }

    public Crane getDockingPoint() {
        return dockingPoint;
    }

    public void setDockingPoint(Crane dockingPoint) {
        this.dockingPoint = dockingPoint;
    }

    int getLenghtTransporter() {
        int maxX = 0;
        for (Container c : containers) {
            if (c.getPosition().x > maxX) {
                maxX = (int) c.getPosition().x;
            }
        }
        return maxX;
    }

    public boolean reservePosition(Container container) {
        if (container.getDateArrival().before(container.getDateDeparture())) {
            if (containers == null) {
                containers = new ArrayList<Container>();
            }
            boolean posistionTaken = false;
            for (Container c : containers) {
                if (c.getPosition().x == container.getPosition().x
                        && c.getPosition().y == container.getPosition().y
                        && c.getPosition().z == container.getPosition().z) {
                    posistionTaken = true;
                    break;
                }
            }
            if (!posistionTaken) {
                reservedSpace.put(container, container.getBufferPosition());
                return true;
            } else {
                return false;
            }

        } else {
            return false;
        }
    }

    public Date getDateArrival() {
        return this.dateArrival;
    }
}
