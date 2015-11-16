/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingcontroller;

import java.util.Comparator;

/**
 *
 * @author Wessel
 */
public class ContainerDepartureComparer implements Comparator<Container> {

    @Override
    public int compare(Container o1, Container o2) {
        return o1.getDateDeparture().compareTo(o2.getDateDeparture());
    }
}
