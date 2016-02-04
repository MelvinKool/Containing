/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulator;

import Simulator.cranes.Crane;
import Simulator.vehicles.AGV;
import Simulator.vehicles.FreightTruck;
import Simulator.vehicles.Ship;
import Simulator.vehicles.TrainCart;
import java.net.SocketException;
import java.util.List;
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
        this.setName("connection thread");
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
                System.err.println("Unknown error when trying to connect to server: " + ex.getMessage());
                this.connected = false;
            }
            try
            {
                Thread.sleep(5000);
            } catch (InterruptedException ex)
            {
                System.err.println("Stopping connection: " + ex.getMessage());
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
//                System.out.println("Received: " + data);
            } 
            catch (java.net.SocketTimeoutException ex) { 
                continue;
            }
            catch (SocketException ex)
            {
                System.err.println("SocketError while trying to read:" + ex.getMessage());
                this.connected = false;
            } 
            catch (Exception ex) 
            {
                System.err.println("Unknown error while trying to read: " + ex.getMessage());
                this.connected = false;
            }
            
            if (data.equals("freeAgv")) {
                int agvId = -1;
                while (agvId == -1) {
                    agvId = this.commandHandler.getFreeAgv();
                    if (agvId == -1)
                    {
                        try
                        {
                            Thread.sleep(1);
                        } catch (InterruptedException ex)
                        {
                            System.err.println("Error while waiting for free agv:" + ex.getMessage());
                            this.connected = false;
                            return;
                        }
                    }
                }
                try
                {
                    this.objectLoader.agvs.get(agvId).setBusy(true); // agv will certainly be used, set busy
                    this.socket.write("freeAgv " + agvId);
                }
                catch (SocketException ex) 
                {
                    System.err.println("Error while sending free agv: " + ex.getMessage()); 
                    this.connected = false;
                    break;
                }
                catch (Exception ex) 
                { 
                    System.err.println("At agv req; " + ex.getMessage());
                    if (!this.connected) 
                    {
                        break;
                    }
                }
                continue;
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
                    
                    int zeeschip = 0    ;//= 10 + random.nextInt(20);
                    int binnenschip = 0 ;//= 10 + random.nextInt(20);
                    int agv = 0         ;//= 10 + random.nextInt(20);
                    int trein = 0       ;//= 10 + random.nextInt(20);
                    int vrachtauto = 0  ;//= 10 + random.nextInt(20);
                    int opslag = 0      ;//= 10 + random.nextInt(20);
                    int kranen = 0    ;//= 10 + random.nextInt(20);
                    
                    
                    for (Map.Entry pair : objectLoader.containers.entrySet()) {
                        //System.out.println(pair.getKey() + " = " + pair.getValue());
                        Container cont = (Container) pair.getValue();
                        if(cont.getVehicle() instanceof Ship){
                            zeeschip++;
                        }
                        //else if(pair.getValue() instanceof Ship){
                        //    
                        //}
                        else if(cont.getVehicle() instanceof AGV){
                            agv++;
                        }
                        else if(cont.getVehicle() instanceof TrainCart){
                            trein++;
                        }
                        else if(cont.getVehicle() instanceof FreightTruck){
                            vrachtauto++;
                        }
                        else if(cont.getVehicle() == null){
                            opslag++;
                        }
                        else if (cont.getVehicle() instanceof Crane) {
                            kranen++;
                        }
                    }
                    String result = "dataforapp/"+
                                    zeeschip+","+
                                    binnenschip+","+
                                    agv+","+
                                    trein+","+
                                    vrachtauto+","+
                                    opslag+","+
                                    kranen;
            try
            {
                this.socket.write(result);
            } catch (Exception ex)
            {
                System.out.println("Error while sending state to server" + ex.getMessage());
                this.connected = false;
            }
            this.lastAppDataSent = System.currentTimeMillis();
        }
    }
}
