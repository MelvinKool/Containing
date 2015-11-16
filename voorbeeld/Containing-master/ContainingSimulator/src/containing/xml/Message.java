/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containing.xml;

import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.simpleframework.xml.*;
import org.simpleframework.xml.core.Persister;

/**
 *
 * @author Hendrik
 */
@Root(name = "message")
public class Message {

    @Attribute
    private int command;//Command number
    @ElementArray(name = "parameters", entry = "parameter")
    private Object[] parameters;//Parameters for command

    /**
     * Empty message
     */
    public Message() {
    }

    /**
     * Filled message
     *
     * @param command
     * @param parameters
     */
    public Message(int command, Object[] parameters) {
        this.command = command;
        this.parameters = parameters;
    }

    /**
     *
     * @return command number
     */
    public int getCommand() {
        return command;
    }

    /**
     *
     * @param command
     */
    public void setCommand(int command) {
        this.command = command;
    }

    /**
     * parameters toevoegen
     *
     * @return parameters
     */
    public Object[] getParameters() {
        return parameters;
    }

    /**
     * parameters toevoegen aan de command
     *
     * @param parameters
     */
    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }

    /**
     * Get message object from xml string
     *
     * @param XML
     * @return
     */
    static public Message decodeMessage(String XML) {
        try {
            Serializer serializer = new Persister();

            return serializer.read(Message.class, XML);
            
        } catch (Exception ex) {
            Logger.getLogger(Message.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * encode message from object to xml
     *
     * @param message
     * @return
     */
    static public String encodeMessage(Message message) {
        try {
            Serializer serializer = new Persister();
            StringWriter xmlString = new StringWriter();

            serializer.write(message, xmlString);
            return xmlString.toString().replace("\n", "");
        } catch (Exception ex) {
            Logger.getLogger(Message.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public String toString() {
        return "Message{" + "command=" + command + ", parameters=" + parameters + '}';
    }
}
