/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containing.xml;

import containingcontroller.Container;
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
     *
     * @param c
     */
    public SimContainer(Container c) {
        this.id = c.getId();
        this.indexPosition = c.getPosition();
        this.bedrijf = c.getOwner();
    }

    public SimContainer(String id, CustomVector3f pos, String bedrijf) {
        this.id = id;
        this.indexPosition = pos;
        this.bedrijf = bedrijf;
    }

    public SimContainer() {
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
    public CustomVector3f getIndexPosition() {
        return indexPosition;
    }

    /**
     *
     * @param indexPosition
     */
    public void setIndexPosition(CustomVector3f indexPosition) {
        this.indexPosition = indexPosition;
    }

    public String getBedrijf() {
        return bedrijf;
    }

    public void setBedrijf(String bedrijf) {
        this.bedrijf = bedrijf;
    }
}
