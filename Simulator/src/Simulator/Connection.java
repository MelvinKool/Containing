/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulator;

import java.net.SocketException;
import java.nio.channels.Selector;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author erwin
 */
public class Connection extends Thread implements Runnable
{
    private long lastAppDataSent;
    private SimSocket socket;
    private ObjectLoader objectLoader;
    private CommandHandler commandHandler;
    private String address;
    private int port;
    private boolean connected;
    
    public Connection(String address, int port, ObjectLoader objectLoader, CommandHandler commandHandler) throws Exception {
        this.address = address;
        this.port = port;
        this.objectLoader = objectLoader;
        this.commandHandler = commandHandler;
        this.connected = false;
        this.lastAppDataSent = 0L;
        this.start();
    }
    
    @Override
    public void run() {
        while (true) {
            try
            {
                this.socket = new SimSocket(this.address, this.port);
                this.socket.write("Simulator");
                this.connected = true;
                this.communicate();
            } catch (Exception ex)
            {
                Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
            }
            try
            {
                Thread.sleep(5000);
            } catch (InterruptedException ex)
            {
                Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void communicate() {
        Selector select = null;
        while (this.connected) {
            String data = "";
            JSONObject command = null;
            try
            {
                data = this.socket.read();
                System.out.println("Received: " + data);
            } catch (java.net.SocketTimeoutException ex) { 
            } catch (SocketException ex)
            {
                Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
                this.connected = false;
            } catch (Exception ex) { }
            try 
            {
                command = this.commandHandler.ParseJSON(data);
                this.commandHandler.queueCommand(command);
            } catch (JSONException ex) { }
            
            this.sendAppData();
        }
    }
    
    private void sendAppData() {
        if (System.currentTimeMillis() - this.lastAppDataSent >= 3000) {
            System.out.println("appdata");
            Random random = new Random();
                    
                    int zeeschip    = 10 + random.nextInt(20);
                    int binnenschip = 10 + random.nextInt(20);
                    int agv         = 10 + random.nextInt(20);
                    int trein       = 10 + random.nextInt(20);
                    int vrachtauto  = 10 + random.nextInt(20);
                    int opslag      = 10 + random.nextInt(20);
                    int diversen    = 10 + random.nextInt(20);
                    /*
                    for (Map.Entry pair : objectLoader.containers.entrySet()) {
                        System.out.println(pair.getKey() + " = " + pair.getValue());
                        
                        if(pair.getValue() instanceof Ship){
                            zeeschip++;
                        }
                        //else if(pair.getValue() instanceof Ship){
                        //    
                        //}
                        else if(pair.getValue() instanceof AGV){
                            agv++;
                        }
                        else if(pair.getValue() instanceof Train){
                            trein++;
                        }
                        else if(pair.getValue() instanceof FreightTruck){
                            vrachtauto++;
                        }
                        //else if(pair.getValue() instanceof ){
                        //    opslag
                        //}
                        else{
                            diversen++;
                        }
                    }
                    */
                    String result = "dataforapp/"+
                                    zeeschip+","+
                                    binnenschip+","+
                                    agv+","+
                                    trein+","+
                                    vrachtauto+","+
                                    opslag+","+
                                    diversen;
            try
            {
                this.socket.write(result);
            } catch (Exception ex)
            {
                Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.lastAppDataSent = System.currentTimeMillis();
        }
    }
}
