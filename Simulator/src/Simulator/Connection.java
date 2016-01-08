package Simulator;

import java.net.*;
import java.io.*;

public class Connection
{
    private class SimSocket extends Socket
    {
        private DataInputStream in;
        private OutputStream out;
        private final int bufsize = 4096;
        //SocketAddress socket = new InetSocketAddress("localhost", 1337);
        
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
    
    private void reconnect()
    {
        try 
        {
            if (simSocket==null && !shouldStop)
            {
                try
                {
                    simSocket = new Connection.SimSocket(InetAddress.getByName("localhost"), 1337);
                } 
                catch (Exception e)
                {
                }
                
            }
            else
            {
                simSocket.close();
                simSocket = null;
            }
        } 
        catch (IOException ioe) 
        {
            System.out.println("Could not reconnect, trying again in 5s.");
        }
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
                    
                    //Connection is good
                    tRead = initTRead();
                    tCheck = initTCheck();
                }
                else
                {
                    System.out.println("Cannot get connection, trying again in 5s");
                    try { Thread.sleep(5000); }
                    catch(Exception e) {}
                }
                simSocket = null;
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
                stop();
            }
            catch(Exception e){}
        }});
    }
    
    private Thread initTCheck()
    {
        return new Thread(new Runnable() { @Override public void run() 
        {
            while (!shouldStop) 
            {   
                try 
                {
                    Thread.sleep(5000);
                    write("connection_check");
                } 
                catch (Exception e) 
                {
                    reconnect();
                }
            }
        }});
    }
    
    private void initSocket()
    {
        try                { simSocket = new Connection.SimSocket(InetAddress.getByName("localhost"), 1337); }
        catch(Exception e) { simSocket = null; }
    }
}