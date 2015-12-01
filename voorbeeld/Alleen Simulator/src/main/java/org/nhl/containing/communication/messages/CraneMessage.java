package org.nhl.containing.communication.messages;

public class CraneMessage extends Message{
    private String craneType;
    private int craneIdentifier;
    private String transporterType;
    private int transporterIdentifier;
    private int agvIdentifier;
    private int storageIdentifier;
    private int containerNumber;

    public CraneMessage(int id, String craneType , int craneIdentifier ,String transporterType, int transporterIdentifier, int agvIdentifier, int storageIdentifier, int containerNumber) {
        super(id, Message.CRANE);
        this.craneType = craneType;
        this.craneIdentifier = craneIdentifier;
        this.transporterType = transporterType;
        this.transporterIdentifier = transporterIdentifier;
        this.agvIdentifier = agvIdentifier;
        this.storageIdentifier = storageIdentifier;
        this.containerNumber = containerNumber;
    }

    public String getCraneType() {
        return craneType;
    }
    
    public int getCraneIdentifier() {
        return craneIdentifier;
    }
    
    public String getTransporterType() {
        return transporterType;
    }

    public int getTransporterIdentifier() {
        return transporterIdentifier;
    }

    public int getAgvIdentifier() {
        return agvIdentifier;
    }

    public int getStorageIdentifier() {
        return storageIdentifier;
    }

    public int getContainerNumber() {
        return containerNumber;
    }
}
