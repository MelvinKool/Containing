package org.nhl.containing.communication.messages;

/**
 *
 */
public class SpeedMessage extends Message {
    private float speed;
    private String dateString;

    public SpeedMessage(int id, float speed, String dateString) {
        super(id, Message.SPEED);
        this.speed = speed;
        this.dateString = dateString;
    }

    public String getDateString() {
        return dateString;
    }
    
    public float getSpeed() {
        return speed;
    }
}
