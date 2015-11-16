/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingcontroller;

import containing.xml.CustomVector3f;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 *
 * @author Hendrik
 */
public class XMLParser {

    /**
     * Parse inputstream from xml file to list of containers
     *
     * @param File inputstream from xmlfile
     * @return list of read containers
     */
    static public List<Container> parseXMLFile(InputStream File) {
        try {
            int id = 0;
            int maxX = 0;
            int maxY = 0;
            int maxZ = 0;
            Stack<String> sk = new Stack<>();
            Calendar c = Calendar.getInstance();
            CustomVector3f p = new CustomVector3f();
            List<Container> containersList = null;
            Container currentContainer = null;
            String tagContent = null;
            List<String> ID = new ArrayList<String>();
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLStreamReader reader
                    = factory.createXMLStreamReader(File);

            while (reader.hasNext()) {
                int event = reader.next();

                switch (event) {
                    case XMLStreamConstants.START_ELEMENT:

                        if ("record".equals(reader.getLocalName())) {
                            currentContainer = new Container();
                            String contId = reader.getAttributeValue(0);
                            if (ID.contains(contId)) {
                                id += 1;
                                contId = "ownid" + id;
                            }

                            currentContainer.setId(contId);

                        }
                        if ("recordset".equals(reader.getLocalName())) {
                            containersList = new ArrayList<>();
                        }

                        sk.push(reader.getLocalName());
                        break;

                    case XMLStreamConstants.CHARACTERS:
                        tagContent = reader.getText().trim();
                        break;

                    case XMLStreamConstants.END_ELEMENT:
                        sk.pop();
                        switch (reader.getLocalName().toLowerCase()) {
                            case "record":
                                containersList.add(currentContainer);
                                break;
                            case "d":
                                c = Calendar.getInstance();
                                c.set(Calendar.DAY_OF_MONTH, Integer.parseInt(tagContent));
                                break;
                            case "m":
                                c.set(Calendar.MONTH, Integer.parseInt(tagContent) - 1);
                                break;
                            case "j":
                                c.set(Calendar.YEAR, Integer.parseInt(tagContent) + 2000);
                                break;
                            case "van":
                                String[] time = tagContent.split("\\.");
                                c.set(Calendar.HOUR, Integer.parseInt(time[0]));
                                c.set(Calendar.MINUTE, Integer.parseInt(time[1]));
                                c.set(Calendar.SECOND, 0);
                                c.set(Calendar.MILLISECOND, 0);
                                break;
                            case "tijd":
                                if (sk.peek().equalsIgnoreCase("aankomst")) {
                                    currentContainer.setDateArrival(c.getTime());//wat te doen met tijd
                                } else {
                                    currentContainer.setDateDeparture(c.getTime());
                                }
                                break;
                            case "soort_vervoer":
                                if (sk.peek().equalsIgnoreCase("aankomst")) {
                                    currentContainer.setTransportTypeArrival(TransportTypes.getTransportType(tagContent));
                                } else {
                                    currentContainer.setTransportTypeDeparture(TransportTypes.getTransportType(tagContent));
                                }
                                break;
                            case "bedrijf":
                                if (sk.peek().equalsIgnoreCase("aankomst")) {
                                    currentContainer.setCargoCompanyArrival(tagContent);
                                } else if (sk.peek().equalsIgnoreCase("vertrek")) {
                                    currentContainer.setCargoCompanyDeparture(tagContent);
                                }

                                break;
                            case "x":
                                p = new CustomVector3f();
                                p.x = Integer.parseInt(tagContent);
                                if (p.x > maxX && currentContainer.getTransportTypeArrival() == TransportTypes.SEASHIP) {
                                    maxX = (int) p.x;
                                }
                                if (currentContainer.getTransportTypeArrival() == TransportTypes.LORREY) {
                                    p.x = 0;
                                }

                                break;
                            case "y":
                                p.z = Integer.parseInt(tagContent);
                                if (p.z > maxZ) {
                                    maxZ = (int) p.z;
                                }
                                break;
                            case "z":
                                p.y = Integer.parseInt(tagContent);
                                if (p.y > maxY) {
                                    maxY = (int) p.y;
                                }

                                break;
                            case "positie":
                                currentContainer.setPosition(p);
                                break;
                            case "naam":
                                if (sk.peek().equalsIgnoreCase("eigenaar")) {
                                    currentContainer.setOwner(tagContent);
                                } else {
                                    currentContainer.setContents(tagContent);
                                }
                                break;
                            case "containernr":
                                currentContainer.setContainerNumber(Integer.parseInt(tagContent));
                                break;
                            case "l":
                                currentContainer.setLenght(tagContent);
                                break;
                            case "h":
                                currentContainer.setHeight(tagContent);
                                break;
                            case "b":
                                currentContainer.setWidth(tagContent);
                                break;
                            case "leeg":
                                currentContainer.setWeightEmpty(Integer.parseInt(tagContent));
                                break;
                            case "inhoud":
                                if (sk.peek().equalsIgnoreCase("aankomst")) {
                                    if (!tagContent.isEmpty()) {
                                        currentContainer.setWeightLoaded(Integer.parseInt(tagContent));
                                    }
                                }
                                break;
                            case "soort":
                                currentContainer.setContentType(tagContent);
                                break;
                            case "gevaar":
                                currentContainer.setContentDanger(tagContent);
                                break;
                            case "iso":
                                currentContainer.setIso(tagContent);
                                break;

                        }
                        break;

                    case XMLStreamConstants.START_DOCUMENT:
                        containersList = new ArrayList<>();
                        break;
                }

            }
            System.out.println("maxX:" + maxX);
            System.out.println("maxY:" + maxY);
            System.out.println("maxZ:" + maxZ);
            return containersList;
        } catch (XMLStreamException ex) {
            Logger.getLogger(XMLParser.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }
}
