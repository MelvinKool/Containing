package Simulator;
import java.net.*;
import java.io.*;
import java.util.Random;

public class oldConnection
{
    private String ip = "localhost";
    
    private boolean shouldStop = false;
    private SimSocket simSocket;
    private ObjectLoader objectLoader;
    private CommandHandler commandHandler;
    
    private Thread tConnection;
    private Thread tRead;
    private Thread tCheck;
    private Thread tDataForApp;
    
    public oldConnection(ObjectLoader objectLoader, CommandHandler commandHandler) throws Exception
    {
        this.objectLoader = objectLoader;
        this.commandHandler = commandHandler;
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
                while(!shouldStop)
                {
                    String input = read();
                    if(input.contentEquals("disconnect"))
                    {
                        System.out.println("Disconnected from server.");
                        break;
                    }
                    System.out.println(input);
                    commandHandler.queueCommand(
                                commandHandler.ParseJSON(input));
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
                    write(result);
                    Thread.sleep(3000);
                }
            } 
            catch (Exception e){}
        }});
    }
    
    private void initSocket()
    {
        try                { simSocket = new oldConnection.SimSocket(InetAddress.getByName(ip), 1337); }
        catch(Exception e) { simSocket = null; }
    }
}