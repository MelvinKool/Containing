/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingsimulator;

import com.jme3.math.Vector3f;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Sietse
 */
public class OffsetPoint {

    Map<String, OffsetWay> wayMap;

    /**
     * Constructor
     */
    public OffsetPoint() {
        wayMap = new HashMap();
    }

    /**
     * adds new element to way hashmap
     * @param nextID next OffsetPoint ID
     * @param lastID previous OffsetPoint ID
     * @param slowLane position of point in slow lane
     * @param fastLane position of point in fast lane
     */
    public void addWay(String nextID, String lastID, Vector3f slowLane, Vector3f fastLane) {
        wayMap.put(((nextID == null) ? "NONE" : nextID) + ";" + ((lastID == null) ? "NONE" : lastID), new OffsetWay(slowLane, fastLane));
    }

    /**
     * adds new element to way hashmap
     * @param nextID next OffsetPoint ID
     * @param lastID previous OffsetPoint ID
     * @param lane position in lane to add OffsetPoint to
     */
    public void addWay(String nextID, String lastID, Vector3f lane) {
        addWay(nextID, lastID, lane, lane);
    }

    /**
     * Get position of OffsetPoint based on previous point, next point and lane
     * @param nextPoint next OffsetPoint ID
     * @param lastPoint previous OffsetPoint ID
     * @param lane the lane to get the OffsetPoint from
     * @return the position of the OffsetPoint
     */
    public Vector3f getPoint(String nextPoint, String lastPoint, int lane) {
        if (wayMap.containsKey(nextPoint + ";" + lastPoint)) {
            return wayMap.get(nextPoint + ";" + lastPoint).getPoint(lane);
        } else if (wayMap.containsKey("NONE" + ";" + lastPoint)) {
            return wayMap.get("NONE" + ";" + lastPoint).getPoint(lane);
        } else if(wayMap.containsKey(nextPoint + ";" + "NONE")) {
            return wayMap.get(nextPoint + ";" + "NONE").getPoint(lane);
        }
        return Vector3f.ZERO;
    }

    private class OffsetWay {

        private Vector3f fastLane;
        private Vector3f slowLane;

        /**
         * Constructor
         * @param slowLane position of slow lane
         * @param fastLane position of fast lane
         */
        public OffsetWay(Vector3f slowLane, Vector3f fastLane) {
            this.slowLane = slowLane;
            this.fastLane = fastLane;
        }

        /**
         * Get the position of a lane
         * @param lane the lane to get position of, 1 is the slow lane, anything else is the fast lane
         * @return position of the lane
         */
        public Vector3f getPoint(int lane) {
            if (lane == 1) {
                return slowLane;
            }
            return fastLane;
        }
    }
}