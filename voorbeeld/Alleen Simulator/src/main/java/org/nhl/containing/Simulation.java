package org.nhl.containing;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.nhl.containing.areas.*;
import org.nhl.containing.communication.Client;
import org.nhl.containing.vehicles.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.xml.parsers.ParserConfigurationException;
import org.nhl.containing.communication.messages.ArriveMessage;
import org.nhl.containing.communication.ContainerBean;
import org.nhl.containing.communication.messages.CreateMessage;
import org.nhl.containing.communication.messages.Message;
import org.nhl.containing.communication.messages.SpeedMessage;
import org.nhl.containing.communication.Xml;
import org.nhl.containing.communication.messages.CraneMessage;
import org.nhl.containing.communication.messages.DepartMessage;
import org.nhl.containing.communication.messages.MoveMessage;
import org.nhl.containing.cranes.Crane;
import org.nhl.containing.cranes.DockingCrane;
import org.nhl.containing.cranes.StorageCrane;
import org.nhl.containing.cranes.TrainCrane;
import org.nhl.containing.cranes.TruckCrane;
import org.xml.sax.SAXException;

/**
 * test
 * 
* @author normenhansen
 */
public class Simulation extends SimpleApplication {

    private List<Transporter> transporterPool;
    private List<Transporter> transporters;
    private List<ArriveMessage> arriveMessages;
    private List<DepartMessage> departMessages;
    private List<CraneMessage> craneMessages;
    private List<MoveMessage> moveMessages;
    private List<Crane> craneList;
    private List<Container> containerList;
    private TrainArea trainArea;
    private LorryArea lorryArea;
    private BoatArea boatArea;
    private BoatArea inlandBoatArea;
    private StorageArea boatStorageArea;
    private StorageArea trainStorageArea;
    private StorageArea lorryStorageArea;
    private Train train;
    private List<Agv> agvList;
    private Client client;
    private HUD HUD;
    private Calendar cal;
    private Date currentDate;
    private long lastTime;
    private float speedMultiplier;
    private final int MAX_IDLE_AGV = 111;
    private final int MAXAGV = 144;
    private List<Float> agvIdleParkingX;
    private List<Float> agvIdleParkingY;
    private List<Float> agvParkingX;
    private List<Float> agvParkingY;
    Agv agvtest;

    public Simulation() {
        client = new Client();
        transporterPool = new ArrayList<>();
        craneMessages = new ArrayList<>();
        agvList = new ArrayList<>();
        craneList = new ArrayList<>();
        arriveMessages = new ArrayList<>();
        departMessages = new ArrayList<>();
        transporters = new ArrayList<>();
        containerList = new ArrayList<>();
        agvIdleParkingX = new ArrayList<>();
        agvIdleParkingY = new ArrayList<>();
        agvParkingX = new ArrayList<>();
        agvParkingY = new ArrayList<>();
        moveMessages = new ArrayList<>();
    }

    @Override
    public void simpleInitApp() {
        guiFont = assetManager.loadFont("Interface/Fonts/TimesNewRoman.fnt");
        speed = 5;
        setPauseOnLostFocus(false);
        initCam();
        initUserInput();
        initScene();
        initDate();
        HUD = new HUD(this.guiNode, guiFont);
        Thread clientThread = new Thread(client);
        clientThread.setName("ClientThread");
        clientThread.start();
        try {
            Thread.sleep(1000);
        } catch (Throwable e) {
        }
    }

    @Override
    public void simpleUpdate(float tpf) {
        handleMessages();
        updateDate();
        handleProcessingMessage();
        handleAgv();
    }

    @Override
    public void simpleRender(RenderManager rm) {
//TODO: add render code
    }

    @Override
    public void destroy() {
        super.destroy();
        client.stop();
    }

    private void handleAgv() {
        if (!moveMessages.isEmpty()) {
            Iterator<MoveMessage> itrMoveMessage = moveMessages.iterator();
            while (itrMoveMessage.hasNext()) {
                MoveMessage moveMsg = itrMoveMessage.next();
                Agv agv = findAGV(moveMsg.getAgvIdentifier());
//If we have found an Agv & it's at the end of his Dijkstra path
                if (agv != null) {

                    if (agv.isAtDepot()) {
                        sendOkMessage(moveMsg);
                        itrMoveMessage.remove();
                        agv.setReadyToLeave(true);
                        break;
                    }

                    if (agv.isArrived()) {
                        Crane crane = findCrane(moveMsg.getEndLocationId(), moveMsg.getEndLocationType());
                        if (crane != null) {
                            //if the agv is parked at the storage
                            if (agv.getParkingSpot() != -1) {
                                agv.leaveStoragePlatform();
                                agv.setParkingSpot(-1);
                            } //If the agv is ready to leave the platform
                            else if (agv.isReadyToLeave()) {
                                switch (crane.getName()) {
                                    case "DockingCraneSeaShip":
                                        agv.leaveSeashipPlatform();
                                        agv.setReadyToLeave(false);
                                        break;
                                    case "DockingCraneInlandShip":
                                        agv.leaveInlandshipPlatform();
                                        agv.setReadyToLeave(false);
                                        break;
                                    case "StorageCrane":
                                        agv.leaveStoragePlatform();
                                        agv.setParkingSpot(-1);
                                        agv.setReadyToLeave(false);
                                        break;
                                    case "TrainCrane":
                                        agv.leaveTrainPlatform();
                                        agv.setReadyToLeave(false);
                                        break;
                                    case "TruckCrane":
                                        agv.leaveLorryPlatform();
                                        agv.setReadyToLeave(false);
                                        break;
                                }
                            } else //If the agv is entering the platform instead, park it 
                            {
                                switch (crane.getName()) {
                                    case "DockingCraneSeaShip":
                                        agv.parkAtSeashipPlatform(moveMsg.getEndLocationId());
                                        break;
                                    case "DockingCraneInlandShip":
                                        agv.parkAtInlandshipPlatform(moveMsg.getEndLocationId());
                                        break;
                                    case "StorageCrane":
                                        agv.parkAtStoragePlatform(moveMsg.getEndLocationId());
                                        break;
                                    case "TrainCrane":
                                        agv.parkAtTrainPlatform(moveMsg.getEndLocationId());
                                        break;
                                    case "TruckCrane":
                                        agv.parkAtLorryPlatform(moveMsg.getEndLocationId());
                                        break;
                                }
                            }
                        }
                    }

                }
            }
        }
    }

    private void handleMessages() {
        List<String> xmlMessages = new ArrayList<>();
        while (true) {
            String xmlMessage = client.getMessage();
            if (xmlMessage == null) {
                break;
            }
            xmlMessages.add(xmlMessage);
        }
        for (String xmlMessage : xmlMessages) {
            handleMessage(xmlMessage);
        }
    }

    private void handleMessage(String xmlMessage) {
        Message message = null;
        try {
            message = Xml.parseXmlMessage(xmlMessage);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
// Maybe just stacktrace here? Depends on how robust we want this to
// be. With the current code, it just discards the erroneous
// message.
            System.out.println(xmlMessage + " is not a valid message");
            return;
        }
        switch (message.getMessageType()) {
            case Message.CREATE:
                handleCreateMessage((CreateMessage) message);
                break;
            case Message.ARRIVE:
                handleArriveMessage((ArriveMessage) message);
                break;
            case Message.DEPART:
                handleDepartMessage((DepartMessage) message);
                break;
            case Message.SPEED:
                handleSpeedMessage((SpeedMessage) message);
                break;
            case Message.CRANE:
                handleCraneMessage((CraneMessage) message);
                break;
            case Message.MOVE:
                handleMoveMessage((MoveMessage) message);
                break;
            default:
                throw new IllegalArgumentException(message.getMessageType()
                        + " is not a legal message type");
        }
    }

    private void handleCreateMessage(CreateMessage message) {
        List<Container> containers = new ArrayList<>();
        for (ContainerBean containerBean : message.getContainerBeans()) {
            Container container = new Container(assetManager, containerBean.getOwner(),
                    containerBean.getContainerNr(), containerBean.getxLoc(),
                    containerBean.getyLoc(), containerBean.getzLoc(), containerBean.getDepartureDate());
            containers.add(container);
        }
        switch (message.getTransporterType()) {
            case "vrachtauto":
                Lorry lorry = new Lorry(assetManager,
                        message.getTransporterIdentifier(), containers.get(0));
                transporterPool.add(lorry);
                break;
            case "trein":
                Train trein = new Train(assetManager,
                        message.getTransporterIdentifier(), containers);
                transporterPool.add(trein);
                break;
            case "binnenschip":
                Inlandship inland = new Inlandship(assetManager,
                        message.getTransporterIdentifier(), containers);
                transporterPool.add(inland);
                break;
            case "zeeschip":
                Seaship sea = new Seaship(assetManager,
                        message.getTransporterIdentifier(), containers);
                transporterPool.add(sea);
                break;
            default:
                throw new IllegalArgumentException(message.getTransporterType()
                        + " is not a legal transporter type");
        }
        containerList.addAll(containers);
        sendOkMessage(message);
    }

    private void handleArriveMessage(ArriveMessage message) {
        boolean exists = false;
        for (Transporter poolTransporter : transporterPool) {
            if (poolTransporter.getId() == message.getTransporterId()) {
                poolTransporter.setProcessingMessageId(message.getId());
                rootNode.attachChild(poolTransporter);
                poolTransporter.multiplySpeed(speedMultiplier);
                poolTransporter.arrive(message.getDepotIndex());
                arriveMessages.add(message);
                exists = true;
                break;
            }
        }
        if (!exists) {
            throw new IllegalArgumentException("Transporter " + message.getTransporterId()
                    + " does not exist");
        }
// Tell transporter to *arrive*. Rename move() to arrive(). move() is
// too ambiguous. move() should probably be an *abstract* method within
// Transporter, to be actually defined within each of the subclasses.
//
// Set `processingMessageId` within the Transporter/Vehicle/Object to
// this message ID. Once the Transporter has arrived at its destination,
// check for this in a separate function in simpleUpdate(), and then
// send an OK message for the Transporter/Vehicle/Object's
// `processingMessageId`.
//
// The `processingMesageId` also applies to cranes and AGVs. Cranes,
// AGVs and transporters should all therefore be subclassed from the
// same class, OR all implement the same interface (ProcessesMessage).
// An interface is the cleanest solution, and can be found within the
// backend. The default value should be -1 (i.e., not processing any
// messages).
//
// Finally, pop transporter from transporterPool, and add it to
// `transporters`. This list doesn't exist yet, but is trivial to
// create. The reason we don't want to keep the transporter in the pool,
// is because we want a way of discerning whether a transporter can
// actually be interacted with.
    }

    private void handleDepartMessage(DepartMessage message) {
        boolean exists = false;
        for (Transporter transporter : transporters) {
            if (transporter.getId() == message.getTransporterId()) {
                transporter.setProcessingMessageId(message.getId());
                transporter.multiplySpeed(speedMultiplier);
                transporter.depart();
                departMessages.add(message);
                exists = true;
                break;
            }
        }
        if (!exists) {
            throw new IllegalArgumentException("Transporter " + message.getTransporterId()
                    + " does not exist");
        }
    }

    /**
     * Checks the incoming CraneMessage which cranetype we are talking about
     * with which ID and sets the processingMessageId of that crane to the
     * messageID
     *     
* @param message the incoming craneMessage object that is being analyzed
     */
    private void handleCraneMessage(CraneMessage message) {
        craneMessages.add(message);
        Crane crane = findCrane(message.getCraneIdentifier(), message.getCraneType());
//Get the crane with the correct type and ID
        if (crane.getName().equals(message.getCraneType()) && crane.getId() == message.getCraneIdentifier()) {
            if (message.getTransporterType().equals("")) {
//if there isnt a transportertype then we will always be dealing with a storageCrane
                StorageCrane storageCrane = (StorageCrane) crane;
                storageCrane.setProcessingMessageId(message.getId());
//TODO: STORAGE CRANE LOGIC HERE
            } else {
//if there is a transporter type check which crane we are dealing with
                switch (message.getCraneType()) {
                    case "DockingCraneSeaShip":
                        DockingCrane dockingCrane = (DockingCrane) crane;
                        dockingCrane.setProcessingMessageId(message.getId());
                        dockingCrane.boatToAgv(findContainer(message.getContainerNumber()), findAGV(message.getAgvIdentifier()));
                        break;
                    case "DockingCraneInlandShip":
                        dockingCrane = (DockingCrane) crane;
                        dockingCrane.setProcessingMessageId(message.getId());
                        dockingCrane.boatToAgv(findContainer(message.getContainerNumber()), findAGV(message.getAgvIdentifier()));
                        break;
                    case "TrainCrane":
                        TrainCrane trainCrane = (TrainCrane) crane;
                        trainCrane.setProcessingMessageId(message.getId());
                        trainCrane.trainToAgv(findContainer(message.getContainerNumber()), findAGV(message.getAgvIdentifier()), train);
                        break;
                    case "TruckCrane":
                        TruckCrane truckCrane = (TruckCrane) crane;
                        truckCrane.setProcessingMessageId(message.getId());
                        Lorry lorry = (Lorry) findTransporter(message.getTransporterType(), message.getTransporterIdentifier());
                        truckCrane.truckToAgv(lorry, findAGV(message.getAgvIdentifier()));
                        break;
                    default:
                        throw new IllegalArgumentException(message.getCraneType()
                                + " is not a legal crane type");
                }
            }
        }
    }

    /**
     * Checks the incoming MoveMessage which agv we are talking about with which
     * ID and sets the processingMessageId of that agv to the messageID and
     * moves the chosen agv to the provided location
     *     
* @param message the incoming MoveMessage object that is being analyzed
     */
    private void handleMoveMessage(MoveMessage message) {
        moveMessages.add(message);
        Agv agv = findAGV(message.getAgvIdentifier());
        if (agv != null) {
            agv.setProcessingMessageId(message.getId());
            agv.setLocalTranslation(message.getCurrentX(), message.getCurrentY(), 0);
            String[] incomingRoute = message.getDijkstra().split(",\\s*");
            String route = "";
            for (String routeString : incomingRoute) {
                route += routeString;
            }
            agv.move(route);
        } else {
            System.out.println("AGV id : " + message.getAgvIdentifier()
                    + " not found!");
        }
    }

    /**
     * Tries to find a Crane by the given input
     *     
* @param id An ID of the Crane we are going to search for
     * @param craneName An name of the CraneType we are going to search for
     * @return returns null if the agv is not found
     */
    private Crane findCrane(int id, String craneName) {
        for (Crane crane : craneList) {
            if (crane.getName().equals(craneName) && crane.getId() == id) {
                return crane;
            }
        }
        return null;
    }

    /**
     * Tries to find a AGV by the given input
     *     
* @param id An ID of the AGV we are going to search for
     * @return returns null if the agv is not found
     */
    private Agv findAGV(int id) {
        for (Agv AGV : agvList) {
            if (AGV.getId() == id) {
                return AGV;
            }
        }
        return null;
    }

    /**
     * Tries to find a transporter by the given input
     *     
* @param transporterType A transporter type where we are going to search
     * for
     * @param id An ID of the tranposporter we are going to search for
     * @return
     */
    private Transporter findTransporter(String transporterType, int id) {
        for (Transporter transporter : transporters) {
            if (transporter.getId() == id) {
                switch (transporterType) {
                    case "vrachtauto":
                        Lorry lorry = (Lorry) transporter;
                        return lorry;
                    case "trein":
                        train = (Train) transporter;
                        return train;
                    case "binnenschip":
                        Inlandship inlandship = (Inlandship) transporter;
                        return inlandship;
                    case "zeeschip":
                        Seaship seaship = (Seaship) transporter;
                        return seaship;
                    default:
                        throw new IllegalArgumentException(transporterType
                                + " is not a legal transporter type");
                }
            }
        }
        return null;
    }

    /**
     * Tries to find a container by the given input
     *     
* @param containerNumber A number of the container we are going to search
     * for
     * @return returns null if nothing is found.
     */
    private Container findContainer(int containerNumber) {
        for (Container container : containerList) {
            if (container.getContainerID() == containerNumber) {
                return container;
            }
        }
        return null;
    }

    /**
     * Analyzes the incoming speedMessage and sets the speed and the date of the
     * frontend by the given input
     *
     * @param message
     */
    private void handleSpeedMessage(SpeedMessage message) {
        speedMultiplier = message.getSpeed();
        changeCraneSpeed();

        String dateString = message.getDateString();
        DateFormat format = new SimpleDateFormat("EEE MMM FF HH:mm:ss zzz yyyy", Locale.ENGLISH);
        try {
            Date newDate = format.parse(dateString);
            currentDate = newDate;
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        sendOkMessage(message);
    }

    /**
     * Checks wether a transporter or a crane is ready for a new job and sends
     * back an OK-message to the backend system.
     */
    private void handleProcessingMessage() {
//Loop through all transporters and send an OK message when a transporter has arrived to its destination
        if (!arriveMessages.isEmpty()) {
            Iterator<Transporter> itrTransporter = transporterPool.iterator();
            while (itrTransporter.hasNext()) {
                Transporter poolTransporter = itrTransporter.next();
                if (poolTransporter.isArrived()) {
                    Iterator<ArriveMessage> itrMessage = arriveMessages.iterator();
                    while (itrMessage.hasNext()) {
                        Message msg = itrMessage.next();
                        if (msg.getId() == poolTransporter.getProcessingMessageId()) {
                            sendOkMessage(msg);
                            transporters.add(poolTransporter);
                            itrTransporter.remove();
                            itrMessage.remove();
                        }
                    }
                }
            }
        }
//Loop through all transporters and send an OK message when a transporter has departed
        if (!departMessages.isEmpty()) {
            Iterator<Transporter> itrDepartingTransporter = transporters.iterator();
            while (itrDepartingTransporter.hasNext()) {
                Transporter transporter = itrDepartingTransporter.next();
                if (transporter.isArrived()) {
                    Iterator<DepartMessage> itrMessage = departMessages.iterator();
                    while (itrMessage.hasNext()) {
                        Message msg = itrMessage.next();
                        if (msg.getId() == transporter.getProcessingMessageId()) {
                            sendOkMessage(msg);
                            rootNode.detachChild(transporter);
                            itrDepartingTransporter.remove();
                            itrMessage.remove();
                        }
                    }
                }
            }
        }
//Loop through all cranes and send an OK message when a crane is ready
        if (!craneMessages.isEmpty()) {
            Iterator<Crane> itrCrane = craneList.iterator();
            while (itrCrane.hasNext()) {
                Crane crane = itrCrane.next();
                if (crane.isArrived()) {
                    Iterator<CraneMessage> itrMessage = craneMessages.iterator();
                    while (itrMessage.hasNext()) {
                        Message msg = itrMessage.next();
                        if (msg.getId() == crane.getProcessingMessageId()) {
                            sendOkMessage(msg);
                            itrMessage.remove();
                        }
                    }
                }
            }
        }
    }

    /**
     * Sends an OK-type message to the controller of the object that is ready
     * for a new task -SUBJECT TO CHANGE, MAYBE SUPERCLASS OBJECT IN THE
     * FUTURE!-
     *     
* @param veh -SUBJECT TO CHANGE, MAYBE SUPERCLASS OBJECT IN THE FUTURE!-
     */
    private void sendOkMessage(Message message) {
        client.writeMessage("<Ok><id>" + message.getId() + "</id></Ok>");
    }

    /**
     * Reads the message object and creates and returns a Transporter from its
     * information.
     *     
* @param message Create message.
     * @return Transporter as described in the message.
     */
    private Transporter createTransporterFromMessage(Message message) {
        return null;
    }

    /**
     * Initialises the simulation date.
     */
    private void initDate() {
        cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2004);
        cal.set(Calendar.MONTH, 11);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        currentDate = cal.getTime();
        lastTime = System.currentTimeMillis();
    }

    private void changeCraneSpeed() {
        List<Crane> dockingCranes = new ArrayList<>();
        dockingCranes.addAll(boatArea.getDockingCranes());
        dockingCranes.addAll(lorryArea.getTruckCranes());
        dockingCranes.addAll(trainArea.getTrainCranes());
        dockingCranes.addAll(boatStorageArea.getStorageCranes());
        dockingCranes.addAll(lorryStorageArea.getStorageCranes());
        dockingCranes.addAll(trainStorageArea.getStorageCranes());
        for (Crane crane : dockingCranes) {
            crane.multiplySpeed(speedMultiplier);
        }
    }

    /**
     * Updates the simulation date.
     * <p/>
     * Compares the time since the last function call to the current time. This
     * is the delta time. The delta time is added to the simulation date,
     * multiplied by the specified TIME_MULTIPLIER.
     */
    private void updateDate() {
        long curTime = System.currentTimeMillis();
        int deltaTime = (int) (curTime - lastTime);
        cal.add(Calendar.MILLISECOND, deltaTime * (int) speedMultiplier);
        currentDate = cal.getTime();
        lastTime = curTime;
        HUD.updateDateText(currentDate);
    }

    /**
     * Camera settings of the scene.
     */
    private void initCam() {
        viewPort.setBackgroundColor(ColorRGBA.Blue);
        cam.setLocation(new Vector3f(-240, 5, 220));
        flyCam.setMoveSpeed(50);
    }
    /*
     * Method for initializing the scene.
     */

    private void initScene() {
        initLighting();
        initSky();
        initAreas();
        initPlatform();
        initAgvParkingShip();
        initAgvParkingTrain();
        initAgvParkingLorry();
        placeAgv();
        initAgvIdle();
    }

    private void initSky() {
        rootNode.attachChild(SkyFactory.createSky(assetManager, "Textures/Skybox/Skybox.dds", false));
    }

    private void initLighting() {
// Light pointing diagonal from the top right to the bottom left.
        DirectionalLight light = new DirectionalLight();
        light.setDirection((new Vector3f(-0.5f, -0.5f, -0.5f)).normalizeLocal());
        light.setColor(ColorRGBA.White);
        rootNode.addLight(light);
// A second light pointing diagonal from the bottom left to the top right.
        DirectionalLight secondLight = new DirectionalLight();
        secondLight.setDirection((new Vector3f(0.5f, 0.5f, 0.5f)).normalizeLocal());
        secondLight.setColor(ColorRGBA.White);
        rootNode.addLight(secondLight);
    }

    private void initAreas() {
// Add lorry area.
        lorryArea = new LorryArea(assetManager, 20);
        lorryArea.setLocalTranslation(300, 0, 170);
        rootNode.attachChild(lorryArea);
// Add the TrainArea.
        trainArea = new TrainArea(assetManager, 4);
        trainArea.setLocalTranslation(-160, 0, -180);
        rootNode.attachChild(trainArea);
// Add the BoatArea (Sea).
        boatArea = new BoatArea(assetManager, BoatArea.AreaType.SEASHIP, 10, 28);
        boatArea.setLocalTranslation(-325, 0, -100);
        rootNode.attachChild(boatArea);
// Add the inlandBoatArea.
        inlandBoatArea = new BoatArea(assetManager, BoatArea.AreaType.INLANDSHIP, 8, 40);
        inlandBoatArea.setLocalTranslation(-240, 0, 220);
        rootNode.attachChild(inlandBoatArea);
// Add the StorageArea for boat containers.
        boatStorageArea = new StorageArea(assetManager, 4);
        boatStorageArea.setLocalTranslation(-150, 0, -120);
        rootNode.attachChild(boatStorageArea);
// Add the StorageArea for train containers.
        trainStorageArea = new StorageArea(assetManager, 4);
        trainStorageArea.setLocalTranslation(130, 0, -120);
        rootNode.attachChild(trainStorageArea);
// Add the StorageArea for lorry containers.
        lorryStorageArea = new StorageArea(assetManager, 4);
        lorryStorageArea.setLocalTranslation(380, 0, -120);
        rootNode.attachChild(lorryStorageArea);
//Add all the cranes to a list for easier accesability
        craneList.addAll(lorryArea.getTruckCranes());
        craneList.addAll(trainArea.getTrainCranes());
        craneList.addAll(boatArea.getDockingCranes());
        craneList.addAll(inlandBoatArea.getDockingCranes());
        craneList.addAll(boatStorageArea.getStorageCranes());
        craneList.addAll(trainStorageArea.getStorageCranes());
        craneList.addAll(lorryStorageArea.getStorageCranes());
    }

    private void initPlatform() {
// Platform for the scene.
        Spatial platform = assetManager.loadModel("Models/platform/platform.j3o");
//vergroot platform
        platform.scale(30, 45, 20);
//schuif platform op
        platform.setLocalTranslation(150, -9, 0);
        rootNode.attachChild(platform);
//create water
//water
        Box waterplatform = new Box(1000f, 1f, 1000f);
        Geometry waterGeo = new Geometry("", waterplatform);
        Material boxMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture waterTexture = assetManager.loadTexture("/Models/platform/water.jpg");
        boxMat.setTexture("ColorMap", waterTexture);
        waterGeo.setLocalTranslation(150, -17, 0);
        waterGeo.setMaterial(boxMat);
        rootNode.attachChild(waterGeo);
    }

    boolean debug = false;
    private void initUserInput() {
        inputManager.addMapping("debugmode", new KeyTrigger(KeyInput.KEY_P));
        inputManager.addMapping("debugmode2", new KeyTrigger(KeyInput.KEY_O));
        inputManager.addMapping("debugmode3", new KeyTrigger(KeyInput.KEY_I));
        ActionListener acl = new ActionListener() {
            public void onAction(String name, boolean keyPressed, float tpf) {
                if (name.equals("debugmode") && keyPressed) {
                    if (!debug) {
                        debug = false;
                        Calendar cali = Calendar.getInstance();
                        cali.set(Calendar.YEAR, 2004);
                        cali.set(Calendar.MONTH, 1);
                        cali.set(Calendar.DAY_OF_MONTH, 1);
                        cali.set(Calendar.MINUTE, 0);
                        cali.set(Calendar.HOUR_OF_DAY, 22);
                        cali.set(Calendar.SECOND, 0);
                        cali.set(Calendar.MILLISECOND, 0);
                        Date datum = cali.getTime();
                        Container testc = new Container(assetManager, "1 Januari", 0, 0, 0, 0, datum);
                        testc.setLocalTranslation(boatStorageArea.getStorageCranes().get(3).getWorldTranslation().mult(1.1f));
                        rootNode.attachChild(testc);
                        boatStorageArea.getStorageCranes().get(3).agvToStorage(testc);
                    } 
                    
                    
                    
                    else {
                        debug = true;
                    }
                }
                
                if (name.equals("debugmode2") && keyPressed) {
                    if (!debug) {
                        debug = false;
                        Calendar caly = Calendar.getInstance();
                        caly.set(Calendar.YEAR, 2004);
                        caly.set(Calendar.MONTH, 1);
                        caly.set(Calendar.DAY_OF_MONTH, 2);
                        caly.set(Calendar.MINUTE, 0);
                        caly.set(Calendar.HOUR_OF_DAY, 22);
                        caly.set(Calendar.SECOND, 0);
                        caly.set(Calendar.MILLISECOND, 0);
                        Date datums = caly.getTime();
                        Container testc = new Container(assetManager, "2 Januari", 0, 0, 0, 0, datums);
                        testc.setLocalTranslation(boatStorageArea.getStorageCranes().get(3).getWorldTranslation().mult(1.1f));
                        rootNode.attachChild(testc);
                        boatStorageArea.getStorageCranes().get(3).agvToStorage(testc);
                    } 
                    
                    else {
                        debug = true;
                    }
                }
                
                if (name.equals("debugmode3") && keyPressed) {
                    if (!debug) {
                        debug = false;
                        Calendar calo = Calendar.getInstance();
                        calo.set(Calendar.YEAR, 2004);
                        calo.set(Calendar.MONTH, 1);
                        calo.set(Calendar.DAY_OF_MONTH, 3);
                        calo.set(Calendar.MINUTE, 0);
                        calo.set(Calendar.HOUR_OF_DAY, 22);
                        calo.set(Calendar.SECOND, 0);
                        calo.set(Calendar.MILLISECOND, 0);
                        Date datump = calo.getTime();
                        Container testc = new Container(assetManager, "3 Januari", 0, 0, 0, 0, datump);
                        testc.setLocalTranslation(boatStorageArea.getStorageCranes().get(3).getWorldTranslation().mult(1.1f));
                        rootNode.attachChild(testc);
                        boatStorageArea.getStorageCranes().get(3).agvToStorage(testc);
                    } 
                    
                    else {
                        debug = true;
                    }
                }
                
                
            }
        };
        inputManager.addListener(acl, "debugmode");
        inputManager.addListener(acl, "debugmode2");
        inputManager.addListener(acl, "debugmode3");
    }
    /**
     * Initializes the agv parking on the ship storage platform. The X and Y
     * locations are storred in an ArrayList. To trigger the 6th parking spot,
     * pull the 6th element from both array's agvParkingX.pull(5);
     * agvParkingY.pull(5);
     *     
* 144 parking places
     */
    private void initAgvParkingShip() {
// Parking id 0 till 23
        int agvStartPoint = -167;
        for (int p = 1; p < 29; p++) {
            if (p % 7 != 0) {
                agvParkingX.add((agvStartPoint + (4.7f * p)));
                agvParkingY.add(-122f);
            } else {
                agvStartPoint += 17;
            }
        }
// Parking on the opposite side
// Parking id 24 till 47
        int agvOpositeStartPoint = -298;
        for (int j = 29; j < 57; j++) {
            if (j % 7 != 0) {
                agvParkingX.add((agvOpositeStartPoint + (4.7f * j)));
                agvParkingY.add(113f);
            } else {
                agvOpositeStartPoint += 17;
            }
        }
    }

    /**
     * Initializes the agv parking on the train storage platform.
     */
    private void initAgvParkingTrain() {
// Parking id 48 till 71
        int agvStartPoint = -149;
        for (int p = 57; p < 85; p++) {
            if (p % 7 != 0) {
                agvParkingX.add((agvStartPoint + (4.7f * p)));
                agvParkingY.add(-122f);
            } else {
                agvStartPoint += 17;
            }
        }
// Parking on the opposite side
// Parking id 72 till 95
        int agvOpositeStartPoint = -281;
        for (int j = 85; j < 113; j++) {
            if (j % 7 != 0) {
                agvParkingX.add((agvOpositeStartPoint + (4.7f * j)));
                agvParkingY.add(113f);
            } else {
                agvOpositeStartPoint += 17;
            }
        }
    }

    /**
     * Initializes the agv parking on the lorry storage platform.
     */
    private void initAgvParkingLorry() {
// Parking id 96 till 119
        int agvStartPoint = -163;
        for (int p = 113; p < 141; p++) {
            if (p % 7 != 0) {
                agvParkingX.add((agvStartPoint + (4.7f * p)));
                agvParkingY.add(-122f);
            } else {
                agvStartPoint += 17;
            }
        }
// Parking on the opposite side
// Parking id 120 till 143
        int agvOpositeStartPoint = -295;
        for (int j = 141; j < 169; j++) {
            if (j % 7 != 0) {
                agvParkingX.add((agvOpositeStartPoint + (4.7f * j)));
                agvParkingY.add(113f);
            } else {
                agvOpositeStartPoint += 17;
            }
        }
    }

    /**
     * Spawn agv on given parkingspace and add it to agv list
     *     
* @param id used to place agv on the given parkingspace
     */
    private void agvToParking(int id) {
        try {
            Agv agv = new Agv(assetManager, id, agvParkingX.get(id), agvParkingY.get(id));
            float agvX = agvParkingX.get(id);
            float agvY = agvParkingY.get(id);
            agv.setLocalTranslation(agvX, 0, agvY);
            rootNode.attachChild(agv);
            agv.setParkingSpot(id);
            agvList.add(agv);
//System.out.println(agv.getWorldTranslation());
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Error: Max parking id is 143, you used " + id);
        }
    }

    private void placeAgv() {
        for (int p = 0; p < MAXAGV; p++) {
            agvToParking(p);
        }
    }

    private void initAgvIdle() {
        int agvIdleStartPoint = 72;
        for (int p = 1; p < MAX_IDLE_AGV + 1; p++) {
            agvIdleParkingX.add((agvIdleStartPoint + (4.7f * p)));
            agvIdleParkingY.add(-160f);
        }
    }
}