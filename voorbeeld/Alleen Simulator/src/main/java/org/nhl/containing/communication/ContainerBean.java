package org.nhl.containing.communication;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Small data Java bean class that holds information about containers.
 * </p>
 * Not technically a true bean, but it has an empty constructor and getters and
 * setters a plenty.
 * </p>
 * The scope of this class should be miniscule. It's just a temporary data class
 * until the data can be transferred to an actual Container.
 *
 * @author Rubykuby
 */
public class ContainerBean {

    private int containerNr;
    private String owner;
    private int xLoc;
    private int yLoc;
    private int zLoc;
    private Date departureDate;

    public ContainerBean() {
    }

    public int getContainerNr() {
        return containerNr;
    }

    public void setContainerNr(int containerNr) {
        this.containerNr = containerNr;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getxLoc() {
        return xLoc;
    }

    public void setxLoc(int xLoc) {
        this.xLoc = xLoc;
    }

    public int getyLoc() {
        return yLoc;
    }

    public void setyLoc(int yLoc) {
        this.yLoc = yLoc;
    }

    public int getzLoc() {
        return zLoc;
    }

    public void setzLoc(int zLoc) {
        this.zLoc = zLoc;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        DateFormat format = new SimpleDateFormat("EEE MMM FF HH:mm:ss zzz yyyy", Locale.ENGLISH);
        try {
            Date newDate = format.parse(departureDate);
            this.departureDate = newDate;
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
    }
}
