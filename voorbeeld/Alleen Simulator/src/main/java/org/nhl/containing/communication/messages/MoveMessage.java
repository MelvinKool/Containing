package org.nhl.containing.communication.messages;

public class MoveMessage extends Message {

    private int agvIdentifier;
    private float currentX = -1;
    private float currentY = -1;
    private String dijkstra = "";
    private String endLocationType = "";
    private int endLocationId = -1;

    public MoveMessage(int id, int agvIdentifier, float currentX, float currentY, String dijkstra, String endLocationType, int endLocationId) {
        super(id, Message.MOVE);
        this.agvIdentifier = agvIdentifier;
        this.currentX = currentX;
        this.currentY = currentY;
        this.dijkstra = dijkstra;
        this.endLocationType = endLocationType;
        this.endLocationId = endLocationId;
    }

    public int getAgvIdentifier() {
        return agvIdentifier;
    }

    public float getCurrentX() {
        return currentX;
    }

    public float getCurrentY() {
        return currentY;
    }

    public String getDijkstra() {
        return dijkstra;
    }

    public int getEndLocationId() {
        return endLocationId;
    }

    public String getEndLocationType() {
        return endLocationType;
    }
}
