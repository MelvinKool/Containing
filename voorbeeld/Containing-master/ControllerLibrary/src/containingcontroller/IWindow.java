/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package containingcontroller;

import java.util.Date;

/**
 *
 * @author Hendrik
 */
public interface IWindow {

    /**
     *Write line to output
     * @param message
     */
    public void WriteLogLine(String message);

    /**
     * set time in simulator
     * @param simTime
     */
    public void setTime(Date simTime);
}
