package Simulator;
import Simulator.vehicles.AGV;
import Simulator.vehicles.FreightTruck;
import Simulator.vehicles.Ship;
import Simulator.vehicles.Train;
import java.net.*;
import java.io.*;
import java.util.Map;

public class Connection
{
    private String ip = "localhost";

    private class SimSocket extends Socket
    {
        private DataInputStream in;
        private OutputStream out;
        private final int bufsize = 4096;
        private SocketAddress socket = new InetSocketAddress(ip, 1337);
        
        public SimSocket(InetAddress ip, int port) throws Exception
        {
            super(ip, port);
            in = new DataInputStream(getInputStream());
            out = getOutputStream();
        }
        
        public void write(String message) throws Exception
        {
            if(message.length() > bufsize - 1)
                throw new Exception("SimSocket.write() - message to long.");
            out.write((message+"\n").getBytes());
        }
        
        public String read() throws Exception
        {
            return in.readLine();
        }
    }
    
    private boolean shouldStop = false;
    private SimSocket simSocket;
    private ObjectLoader objectLoader;
    
    private Thread tConnection;
    private Thread tRead;
    private Thread tCheck;
    private Thread tDataForApp;
    
    public Connection(ObjectLoader objectLoader) throws Exception
    {
        this.objectLoader = objectLoader;
        tConnection = initTConnection();
        tConnection.start();
    }
    
    public void stop()
    {
        try
        {
            shouldStop = true;
            if(simSocket != null)
            {
                simSocket.write("disconnect");
                tConnection.join();
                simSocket.close();
            }
        }
        catch(Exception e)
        {
            System.out.println("Connection.stop() - Cannot close connection.");
        }
        finally
        {
            simSocket = null;
        }
    }
    
    private String read() throws Exception
    {
        return simSocket.read();
    }
    
    private void write(String message) throws Exception
    {
        if(simSocket != null)
            simSocket.write(message);
        else
            throw new Exception("Connection.write() - simSocket = null");
    }
    
    private Thread initTConnection()
    {
        return new Thread(new Runnable() { @Override public void run()
        {
            while(!shouldStop)
            {
                initSocket();
                if(simSocket != null)
                {
                    try { write("Simulator"); }
                    catch(Exception e)
                    {
                        try                 { simSocket.close(); }
                        catch(Exception ex) {}
                        finally             { simSocket = null; continue; }
                    }
                    
                    tRead = initTRead();
                    tCheck = initTCheck();
                    tDataForApp = initTDataFotApp();
                    
                    tRead.start();
                    tCheck.start();
                    tDataForApp.start();
                    
                    try
                    {
                        tCheck.join();
                        tRead.stop();
                        tDataForApp.stop();
                    }
                    catch(Exception e){}
                }
                else
                {
                    System.out.println("Cannot get connection, trying again in 5s");
                    try { Thread.sleep(5000); }
                    catch(Exception e) {}
                }
            }
        }});
    }
    
    private Thread initTRead()
    {
        return new Thread(new Runnable() { @Override public void run()
        {
            try
            {
                CommandHandler commandHandler = new CommandHandler(objectLoader);
                while(!shouldStop)
                {
                    String input = read();
                    if(input.contentEquals("disconnect"))
                    {
                        System.out.println("Disconnected from server.");
                        break;
                    }
                    System.out.println(input);
                    commandHandler.ParseJSON(input);
                }
            }
            catch(Exception e){}
        }});
    }
    
    private Thread initTCheck()
    {
        return new Thread(new Runnable() { @Override public void run() 
        {
            try 
            {
                while (!shouldStop) 
                {   
                    write("connection_check");
                    Thread.sleep(1000);
                }
            } 
            catch (Exception e) 
            {
                System.out.println("Connection lost");
            }
        }});
    }
    
    private Thread initTDataFotApp()
    {
        return new Thread(new Runnable() { @Override public void run() 
        {
            try 
            {
                while (!shouldStop) 
                {
                    int zeeschip    = 0;
                    int binnenschip = 0;
                    int agv         = 0;
                    int trein       = 0;
                    int vrachtauto  = 0;
                    int opslag      = 0;
                    int diversen    = 0;
                    
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
                    
                    String result = "dataforapp/"+
                                    zeeschip+","+
                                    binnenschip+","+
                                    agv+","+
                                    trein+","+
                                    vrachtauto+","+
                                    opslag+","+
                                    diversen;
                    write(result);
                    Thread.sleep(1000);
                }
            } 
            catch (Exception e){}
        }});
    }
    
    private void initSocket()
    {
        try                { simSocket = new Connection.SimSocket(InetAddress.getByName(ip), 1337); }
        catch(Exception e) { simSocket = null; }
    }
}