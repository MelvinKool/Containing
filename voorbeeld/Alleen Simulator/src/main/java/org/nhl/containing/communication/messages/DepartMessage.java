package org.nhl.containing.communication.messages;

public class DepartMessage extends Message {

    private int transporterId;

    /**
     * Data class that holds information pertaining the arrival of a
     * transporter.
     */
    public DepartMessage(int id, int transporterId) {
        super(id, Message.DEPART);
        this.transporterId = transporterId;
    }

    public int getTransporterId() {
        return transporterId;
    }
}
