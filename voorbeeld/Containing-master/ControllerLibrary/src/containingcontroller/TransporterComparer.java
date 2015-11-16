/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package containingcontroller;

import java.util.Comparator;

/**
 *
 * @author Ruben
 */
public class TransporterComparer implements Comparator<Transporter> {

    @Override
    public int compare(Transporter o1, Transporter o2) {
        return o1.getDateArrival().compareTo(o2.getDateArrival());
    }
}