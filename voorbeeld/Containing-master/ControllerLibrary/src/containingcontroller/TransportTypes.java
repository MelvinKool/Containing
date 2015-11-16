/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingcontroller;

/**
 *
 * @author Hendrik
 */
public class TransportTypes {

    /**
     * SEASHIP identifier
     */
 final   static public int SEASHIP = 0;
    /**
     * BARGE identifier
     */
   final static public int BARGE = 1;
    /**
     * LORREY identifier
     */
   final static public int LORREY = 2;
    /**
     * TRAIN identifier
     */
   final static public int TRAIN = 3;

    /**
     * Get transporttype by type text
     *
     * @param type expects vrachtauto, trein, zeeship or binnenschip
     * @return returns 0, if non of the above is entered
     */
    public static int getTransportType(String type) {
        if (type.toLowerCase().contains("vrachtauto")) {
            return LORREY;
        } else if (type.toLowerCase().contains("trein")) {
            return TRAIN;
        } else if (type.toLowerCase().contains("zeeschip")) {
            return SEASHIP;
        } else if (type.toLowerCase().contains("binnenschip")) {
            return BARGE;
        }
        return 0;

    }

    /**
     * Get the text for a type
     *
     * @param type number between 0 and 3
     * @return
     */
    public static String getTransportType(int type) {
        if (type == LORREY) {
            return "vrachtauto";
        } else if (type == TRAIN) {
            return "trein";
        } else if (type == SEASHIP) {
            return "zeeschip";
        } else if (type == BARGE) {
            return "binnenschip";
        }
        return "";

    }
}
