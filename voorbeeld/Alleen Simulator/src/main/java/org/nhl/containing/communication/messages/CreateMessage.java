package org.nhl.containing.communication.messages;

import java.util.List;
import org.nhl.containing.communication.ContainerBean;

/**
 * Data class that holds information pertaining the creation of a transporter.
 */
public class CreateMessage extends Message {
    private String transporterType;
    private int transporterIdentifier;
    private List<ContainerBean> containerBeans;

    public CreateMessage(int id, String transporterType, int transporterIdentifier,
                         List<ContainerBean> containerBeans) {
        super(id, Message.CREATE);
        this.transporterType = transporterType;
        this.transporterIdentifier = transporterIdentifier;
        this.containerBeans = containerBeans;
    }

    public String getTransporterType() {
        return transporterType;
    }

    public int getTransporterIdentifier() {
        return transporterIdentifier;
    }

    public List<ContainerBean> getContainerBeans() {
        return containerBeans;
    }
}
