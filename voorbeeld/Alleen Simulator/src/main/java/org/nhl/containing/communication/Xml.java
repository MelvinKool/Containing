package org.nhl.containing.communication;

import org.nhl.containing.communication.messages.SpeedMessage;
import org.nhl.containing.communication.messages.Message;
import org.nhl.containing.communication.messages.CreateMessage;
import org.nhl.containing.communication.messages.ArriveMessage;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import org.nhl.containing.communication.messages.CraneMessage;
import org.nhl.containing.communication.messages.DepartMessage;
import org.nhl.containing.communication.messages.MoveMessage;

/**
 * Parses provided XML files and returns Containers.
 */
public class Xml {

    /**
     * Parse an XML instruction from the backend, and represent it as program-
     * readable Message object.
     *
     * @param xmlMessage An XML instruction as defined in the project's XML
     * protocol.
     * @return (Mostly) one-on-one conversion towards an instance of Message.
     */
    public static Message parseXmlMessage(String xmlMessage) throws
            ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        InputSource source = new InputSource();
        source.setCharacterStream(new StringReader(xmlMessage));

        Document doc = db.parse(source);

        // Optional, but recommended.
        // Read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
        doc.getDocumentElement().normalize();

        NodeList nodes = doc.getElementsByTagName("id");
        if (nodes.getLength() != 1) {
            throw new IllegalArgumentException(xmlMessage + " is not a valid message");
        }

        Element line = (Element) nodes.item(0);
        int id = Integer.parseInt(Xml.getCharacterDataFromElement(line));

        // The following is trying to find, match and parse the correct message
        // type. There is probably a better way of achieving this, but this
        // appears to be the simplest way.
        // There don't seem to be any drawbacks by doing this, other than
        // ugly code.

        NodeList messageTypeNodes = doc.getElementsByTagName("Create");
        if (messageTypeNodes.getLength() > 0) {
            return parseCreateMessage(messageTypeNodes.item(0), id);
        }

        messageTypeNodes = doc.getElementsByTagName("Arrive");
        if (messageTypeNodes.getLength() > 0) {
            return parseArriveMessage(messageTypeNodes.item(0), id);
        }

        messageTypeNodes = doc.getElementsByTagName("Depart");
        if (messageTypeNodes.getLength() > 0) {
            return parseDepartMessage(messageTypeNodes.item(0), id);
        }

        messageTypeNodes = doc.getElementsByTagName("SpeedMessage");
        if (messageTypeNodes.getLength() > 0) {
            return parseSpeedMessage(messageTypeNodes.item(0), id);
        }

        messageTypeNodes = doc.getElementsByTagName("Crane");
        if (messageTypeNodes.getLength() > 0) {
            return parseCraneMessage(messageTypeNodes.item(0), id);
        }

        messageTypeNodes = doc.getElementsByTagName("Move");
        if (messageTypeNodes.getLength() > 0) {
            return parseMoveMessage(messageTypeNodes.item(0), id);
        }

        // etc etc etc. TODO

        throw new IllegalArgumentException("Could not find valid tag in " + xmlMessage);
    }

    private static CreateMessage parseCreateMessage(Node createNode, int id) {
        List<ContainerBean> containerBeans = new ArrayList<ContainerBean>();

        // There is only one child node, which is the transporter.
        Node transporterNode = createNode.getFirstChild();
        int identifier = Integer.parseInt(transporterNode.getAttributes().
                getNamedItem("identifier").getNodeValue());
        String type = transporterNode.getAttributes().getNamedItem("type").
                getNodeValue();

        NodeList transporterNodes = transporterNode.getChildNodes();

        for (int i = 0; i < transporterNodes.getLength(); i++) {
            Node node = transporterNodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE
                    && node.getNodeName().equals("Container")) {
                containerBeans.add(parseContainerXml(node));
            }
        }
        return new CreateMessage(id, type, identifier, containerBeans);
    }

    private static ContainerBean parseContainerXml(Node containerNode) {
        ContainerBean containerBean = new ContainerBean();

        NodeList containerNodes = containerNode.getChildNodes();

        for (int i = 0; i < containerNodes.getLength(); i++) {
            Node node = containerNodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                String content = node.getTextContent();

                switch (node.getNodeName()) {
                    case "containernr":
                        containerBean.setContainerNr(Integer.parseInt(content));
                        break;
                    case "owner":
                        containerBean.setOwner(content);
                        break;
                    case "xLoc":
                        containerBean.setxLoc(Integer.parseInt(content));
                        break;
                    case "yLoc":
                        containerBean.setyLoc(Integer.parseInt(content));
                        break;
                    case "zLoc":
                        containerBean.setzLoc(Integer.parseInt(content));
                        break;
                    case "date":
                        containerBean.setDepartureDate(content);
                        break;
                    default:
                        throw new IllegalArgumentException(node.getNodeName()
                                + " is not a legal node name");
                }
            }
        }
        return containerBean;
    }

    private static ArriveMessage parseArriveMessage(Node arriveNode, int id) {
        int transporterId = -1;
        int depotIndex = -1;

        NodeList arriveNodes = arriveNode.getChildNodes();

        for (int i = 0; i < arriveNodes.getLength(); i++) {
            Node node = arriveNodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                String content = node.getTextContent();

                switch (node.getNodeName()) {
                    case "transporterId":
                        transporterId = Integer.parseInt(content);
                        break;
                    case "depotIndex":
                        depotIndex = Integer.parseInt(content);
                        break;
                    default:
                        throw new IllegalArgumentException(node.getNodeName()
                                + " is not a legal node name");
                }
            }
        }
        return new ArriveMessage(id, transporterId, depotIndex);
    }

    private static DepartMessage parseDepartMessage(Node arriveNode, int id) {
        int transporterId = -1;

        NodeList departNodes = arriveNode.getChildNodes();

        for (int i = 0; i < departNodes.getLength(); i++) {
            Node node = departNodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                String content = node.getTextContent();

                switch (node.getNodeName()) {
                    case "transporterId":
                        transporterId = Integer.parseInt(content);
                        break;
                    default:
                        throw new IllegalArgumentException(node.getNodeName()
                                + " is not a legal node name");
                }
            }
        }
        return new DepartMessage(id, transporterId);
    }

    private static SpeedMessage parseSpeedMessage(Node speedNode, int id) {
        float speed = 0;
        String dateString = "";
        NodeList speedNodes = speedNode.getChildNodes();

        for (int i = 0; i < speedNodes.getLength(); i++) {
            Node node = speedNodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                String content = node.getTextContent();
                switch (node.getNodeName()) {
                    case "Speed":
                        speed = Float.parseFloat(content);
                        break;
                    case "DateString":
                        dateString = content;
                        break;
                    default:
                        throw new IllegalArgumentException(node.getNodeName()
                                + " is not a legal node name");
                }
            }
        }
        return new SpeedMessage(id, speed, dateString);
    }

    private static CraneMessage parseCraneMessage(Node craneNode, int id) {
        String craneType = "";
        int craneIdentifier = -1;
        String transporterType = "";
        int transporterIdentifier = -1;
        int agvIdentifier = -1;
        int storageIdentifier = -1;
        int containerNumber = -1;


        NodeList craneNodes = craneNode.getChildNodes();

        for (int i = 0; i < craneNodes.getLength(); i++) {
            Node node = craneNodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                String content = node.getTextContent();
                switch (node.getNodeName()) {
                    case "CraneType":
                        craneType = content;
                        break;
                    case "CraneId":
                        craneIdentifier = Integer.parseInt(content);
                        break;
                    case "TransporterType":
                        transporterType = content;
                        break;
                    case "TransporterId":
                        transporterIdentifier = Integer.parseInt(content);
                        break;
                    case "Storage":
                        storageIdentifier = Integer.parseInt(content);
                        break;
                    case "AgvId":
                        agvIdentifier = Integer.parseInt(content);
                        break;
                    case "Container":
                        containerNumber = Integer.parseInt(content);
                        break;
                    default:
                        throw new IllegalArgumentException(node.getNodeName()
                                + " is not a legal node name");
                }
            }
        }
        return new CraneMessage(id, craneType, craneIdentifier, transporterType, transporterIdentifier, agvIdentifier, storageIdentifier, containerNumber);
    }

    private static MoveMessage parseMoveMessage(Node moveNode, int id) {
        int agvIdentifier = -1;
        float currentX = -1;
        float currentY = -1;
        String dijkstra = "";
        String endLocationType = "";
        int endLocationId = -1;


        NodeList moveNodes = moveNode.getChildNodes();

        for (int i = 0; i < moveNodes.getLength(); i++) {
            Node node = moveNodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                String content = node.getTextContent();
                switch (node.getNodeName()) {
                    case "AgvId":
                        agvIdentifier = Integer.parseInt(content);
                        break;
                    case "CurrentX":
                        currentX = Float.parseFloat(content);
                        break;
                    case "CurrentY":
                        currentY = Float.parseFloat(content);
                        break;
                    case "Dijkstra":
                        dijkstra = content;
                        break;
                    case "EndLocationType":
                        endLocationType = content;
                        break;
                    case "EndLocationId":
                        endLocationId = Integer.parseInt(content);
                        break;
                    default:
                        throw new IllegalArgumentException(node.getNodeName()
                                + " is not a legal node name");
                }
            }
        }
        return new MoveMessage(id, agvIdentifier, currentX, currentY, dijkstra, endLocationType, endLocationId);
    }

    /**
     * Gets the characterdata from the specified element
     */
    private static String getCharacterDataFromElement(Element e) {
        Node child = e.getFirstChild();
        if (child instanceof CharacterData) {
            CharacterData cd = (CharacterData) child;
            return cd.getData();
        }
        return "";
    }
}