/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingcontroller;

import containing.xml.CustomVector3f;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author Wessel
 */
public class Container {
    //Properties

    private String id;                      //Container id
    private Date dateArrival;               //Date of arrival
    private Date dateDeparture;             //Date of scheduled departure
    private int transportTypeArrival;       //Type of container arrival
    private int transportTypeDeparture;     //Type of container departure
    private String cargoCompanyArrival;     //Company handling container arrival
    private String cargoCompanyDeparture;   //Company handling container departure
    private CustomVector3f position;              //Position on arrival transport
    private CustomVector3f bufferPosition;        //Position of container inside buffer
    private String owner;                   //Owner of this container
    private int containerNumber;            //Container number
    private String height;                 //Only length and width?
    private String width;
    private String lenght;
    private int weightEmpty;                //Container weight when empty
    private int weightLoaded;               //Container weight when loaded
    private String contents;                //Contents of this container
    private String contentType;             //Type of contents
    private String contentDanger;           //Danger of contents
    private String iso;                     //Container standard

    //Constructor
    /**
     *
     * @param id
     * @param dateArrival
     * @param dateDeparture
     * @param transportTypeArrival
     * @param transportTypeDeparture
     * @param cargoCompanyArrival
     * @param cargoCompanyDeparture
     * @param position
     * @param owner
     * @param containerNumber
     * @param height
     * @param width
     * @param weightEmpty
     * @param lenght
     * @param weightLoaded
     * @param contents
     * @param contentType
     * @param contentDanger
     * @param iso
     */
    public Container(String id, Date dateArrival, Date dateDeparture,
            int transportTypeArrival, int transportTypeDeparture,
            String cargoCompanyArrival, String cargoCompanyDeparture,
            CustomVector3f position, String owner, int containerNumber,
            String height,
            String width,
            String lenght, int weightEmpty, int weightLoaded,
            String contents, String contentType, String contentDanger,
            String iso) {
        this.id = id;
        this.dateArrival = dateArrival;
        this.dateDeparture = dateDeparture;
        this.transportTypeArrival = transportTypeArrival;
        this.transportTypeDeparture = transportTypeDeparture;
        this.cargoCompanyArrival = cargoCompanyArrival;
        this.cargoCompanyDeparture = cargoCompanyDeparture;
        this.position = position;
        //NOTE: bufferPosition remains null until container is assigned to a buffer!
        this.owner = owner;
        this.containerNumber = containerNumber;

        this.weightEmpty = weightEmpty;
        this.weightLoaded = weightLoaded;
        this.contents = contents;
        this.contentType = contentType;
        this.contentDanger = contentDanger;
        this.iso = iso;
        this.height = height;                 //Only length and width?
        this.width = width;
        this.lenght = lenght;
    }

    /**
     *
     */
    public Container() {
    }

    /**
     *
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public Date getDateArrival() {
        return dateArrival;
    }

    /**
     *
     * @param dateArrival
     */
    public void setDateArrival(Date dateArrival) {
        this.dateArrival = dateArrival;
    }

    /**
     *
     * @return
     */
    public Date getDateDeparture() {
        return dateDeparture;
    }

    /**
     *
     * @param dateDeparture
     */
    public void setDateDeparture(Date dateDeparture) {
        this.dateDeparture = dateDeparture;
    }

    /**
     *
     * @return
     */
    public int getTransportTypeArrival() {
        return transportTypeArrival;
    }

    /**
     *
     * @param transportTypeArrival
     */
    public void setTransportTypeArrival(int transportTypeArrival) {
        this.transportTypeArrival = transportTypeArrival;
    }

    /**
     *
     * @return
     */
    public int getTransportTypeDeparture() {
        return transportTypeDeparture;
    }

    /**
     *
     * @param transportTypeDeparture
     */
    public void setTransportTypeDeparture(int transportTypeDeparture) {
        this.transportTypeDeparture = transportTypeDeparture;
    }

    /**
     *
     * @return
     */
    public String getCargoCompanyArrival() {
        return cargoCompanyArrival;
    }

    /**
     *
     * @param cargoCompanyArrival
     */
    public void setCargoCompanyArrival(String cargoCompanyArrival) {
        this.cargoCompanyArrival = cargoCompanyArrival;
    }

    /**
     *
     * @return
     */
    public String getCargoCompanyDeparture() {
        return cargoCompanyDeparture;
    }

    /**
     *
     * @param cargoCompanyDeparture
     */
    public void setCargoCompanyDeparture(String cargoCompanyDeparture) {
        this.cargoCompanyDeparture = cargoCompanyDeparture;
    }

    /**
     *
     * @return
     */
    public CustomVector3f getPosition() {
        return position;
    }

    /**
     *
     * @param position
     */
    public void setPosition(CustomVector3f position) {
        this.position = position;
    }

    /**
     *
     * @return
     */
    public CustomVector3f getBufferPosition() {
        return bufferPosition;
    }

    /**
     *
     * @param bufferPosition
     */
    public void setBufferPosition(CustomVector3f bufferPosition) {
        this.bufferPosition = bufferPosition;
    }

    /**
     *
     * @return
     */
    public String getOwner() {
        return owner;
    }

    /**
     *
     * @param owner
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     *
     * @return
     */
    public int getContainerNumber() {
        return containerNumber;
    }

    /**
     *
     * @param containerNumber
     */
    public void setContainerNumber(int containerNumber) {
        this.containerNumber = containerNumber;
    }

    /**
     *
     * @return
     */
    public String getHeight() {
        return height;
    }

    /**
     *
     * @param height
     */
    public void setHeight(String height) {
        this.height = height;
    }

    /**
     *
     * @return
     */
    public String getWidth() {
        return width;
    }

    /**
     *
     * @param width
     */
    public void setWidth(String width) {
        this.width = width;
    }

    /**
     *
     * @return
     */
    public String getLenght() {
        return lenght;
    }

    /**
     *
     * @param lenght
     */
    public void setLenght(String lenght) {
        this.lenght = lenght;
    }

    /**
     *
     * @return
     */
    public int getWeightEmpty() {
        return weightEmpty;
    }

    /**
     *
     * @param weightEmpty
     */
    public void setWeightEmpty(int weightEmpty) {
        this.weightEmpty = weightEmpty;
    }

    /**
     *
     * @return
     */
    public int getWeightLoaded() {
        return weightLoaded;
    }

    /**
     *
     * @param weightLoaded
     */
    public void setWeightLoaded(int weightLoaded) {
        this.weightLoaded = weightLoaded;
    }

    /**
     *
     * @return
     */
    public String getContents() {
        return contents;
    }

    /**
     *
     * @param contents
     */
    public void setContents(String contents) {
        this.contents = contents;
    }

    /**
     *
     * @return
     */
    public String getContentType() {
        return contentType;
    }

    /**
     *
     * @param contentType
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
     *
     * @return
     */
    public String getContentDanger() {
        return contentDanger;
    }

    /**
     *
     * @param contentDanger
     */
    public void setContentDanger(String contentDanger) {
        this.contentDanger = contentDanger;
    }

    /**
     *
     * @return
     */
    public String getIso() {
        return iso;
    }

    /**
     *
     * @param iso
     */
    public void setIso(String iso) {
        this.iso = iso;
    }

    @Override
    public String toString() {
        SimpleDateFormat timeFormat = new SimpleDateFormat(" dd-MM-yyyy HH:mm:ss");
        return "Container{" + "id=" + id + ", dateArrival=" + timeFormat.format(dateArrival) + ", dateDeparture=" + dateDeparture + ", transportTypeArrival=" + transportTypeArrival + ", transportTypeDeparture=" + transportTypeDeparture + ", cargoCompanyArrival=" + cargoCompanyArrival + ", cargoCompanyDeparture=" + cargoCompanyDeparture + ", position=" + position + ", bufferPosition=" + bufferPosition + ", owner=" + owner + ", containerNumber=" + containerNumber + ", height=" + height + ", width=" + width + ", lenght=" + lenght + ", weightEmpty=" + weightEmpty + ", weightLoaded=" + weightLoaded + ", contents=" + contents + ", contentType=" + contentType + ", contentDanger=" + contentDanger + ", iso=" + iso + '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Container other = (Container) obj;
       /* if (!Objects.equals(this.id, other.id)) {
            return false;
        }*/
        if (this.containerNumber != other.containerNumber) {
            return false;
        }
        return true;
    }
    
}
