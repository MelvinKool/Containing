/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulator;

import Simulator.vehicles.AGV;
import Simulator.vehicles.FreightTruck;
import Simulator.vehicles.Ship;
import Simulator.vehicles.Train;
import Simulator.vehicles.TrainCart;
import java.net.SocketException;
import java.nio.channels.Selector;
import java.util.Map;
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
        this.lastAppDataSent = System.currentTimeMillis();
        this.start();
    }
    
    @Override
    public void run() {
        while (!this.isInterrupted()) {
            try
            {
                this.socket = new SimSocket(this.address, this.port);
                this.socket.write("Simulator");
                this.connected = true;
                this.communicate();
            } catch (Exception ex)
            {
                System.err.println(ex.getMessage());
                this.connected = false;
            }
            try
            {
                Thread.sleep(5000);
            } catch (InterruptedException ex)
            {
                System.err.println(ex.getMessage());
                this.connected = false;
                break;
            }
        }
    }
    
    public void communicate() {
        while (this.connected && !this.isInterrupted()) {
            String data = "";
            JSONObject command = null;
            
            this.sendAppData();
            
            try
            {
                data = this.socket.read();
                //System.out.println("Received: " + data);
            } 
            catch (java.net.SocketTimeoutException ex) { 
                continue;
            }
            catch (SocketException ex)
            {
                System.err.println(ex.getMessage());
                this.connected = false;
            } 
            catch (Exception ex) 
            {
                System.err.println(ex.getMessage());
                this.connected = false;
            }
            
            try 
            {
                command = this.commandHandler.ParseJSON(data);
                this.commandHandler.queueCommand(command);
            } 
            catch (JSONException ex) { 
                System.err.println(ex.getMessage() + ": " + data);
            }
        }
    }
    
    private void sendAppData() {
        if (System.currentTimeMillis() - this.lastAppDataSent >= 3000) {
            System.out.println("sending state to server");
            Random random = new Random();
                    
                    int zeeschip    = 0;//10 + random.nextInt(20);
                    int binnenschip = 0;//10 + random.nextInt(20);
                    int agv         = 0;//10 + random.nextInt(20);
                    int trein       = 0;//10 + random.nextInt(20);
                    int vrachtauto  = 0;//10 + random.nextInt(20);
                    int opslag      = 0;//10 + random.nextInt(20);
                    int diversen    = 0;//10 + random.nextInt(20);
                    
                    for (Map.Entry pair : objectLoader.containers.entrySet()) {
                        System.out.println(pair.getKey() + " = " + pair.getValue());
                        
                        if(((Container)pair.getValue()).getVehicle() instanceof Ship){
                            System.out.println("ship");
                            zeeschip++;
                        }
                        //else if(pair.getValue() instanceof Ship){
                        //    
                        //}
                        else if(((Container)pair.getValue()).getVehicle() instanceof AGV){
                            System.out.println("agv");
                            agv++;
                        }
                        else if(((Container)pair.getValue()).getVehicle() instanceof TrainCart){
                            
                            System.out.println("traincart");
                            trein++;
                        }
                        else if(((Container)pair.getValue()).getVehicle() instanceof FreightTruck){
                            
                            System.out.println("truck");
                            vrachtauto++;
                        }
                        else if(pair.getValue() instanceof SortField){
                            opslag++;
                        }
                        else{
                            System.out.println("overig");
                            diversen++;
                        }
                    }
                    
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
