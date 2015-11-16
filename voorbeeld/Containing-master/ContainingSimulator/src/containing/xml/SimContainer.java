/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containing.xml;


import org.simpleframework.xml.*;

/**
 *
 * @author Hendrik
 */
@Root
public class SimContainer {

    @Attribute
    private String id;
    @Attribute
    private String bedrijf;
    @Element
    private CustomVector3f indexPosition;

    /**
     * Empty simcontainer constructor
     */
    public SimContainer() {
    }
    
    /**
     * Simcontainer constructor
     * @param id container id
     * @param pos indexposition
     */
    public SimContainer(String id, CustomVector3f pos)
    {
        this.id = id;
        this.indexPosition = pos;
    }


    /**
     * Get ID
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     * Set ID
     * @param id container ID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Get index position
     * @return
     */
    public CustomVector3f getIndexPosition() {
        return indexPosition;
    }

    /**
     * set index position
     * @param indexPosition index position
     */
    public void setIndexPosition(CustomVector3f indexPosition) {
        this.indexPosition = indexPosition;
    }

    /**
     * Get company
     * @return
     */
    public String getBedrijf() {
        return bedrijf;
    }

    /**
     * Set company
     * @param bedrijf company
     */
    public void setBedrijf(String bedrijf) {
        this.bedrijf = bedrijf;
    }
}