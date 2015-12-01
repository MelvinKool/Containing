/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingcontroller;

import java.util.Comparator;

/**
 *
 * @author Hendrik
 */
public class ContainerComparer implements Comparator<Container> {

    @Override
    public int compare(Container o1, Container o2) {
        return o1.getDateArrival().compareTo(o2.getDateArrival());
    }
}
