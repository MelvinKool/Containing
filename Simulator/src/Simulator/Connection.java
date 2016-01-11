package Simulator;
import java.net.*;
import java.io.*;

public class Connection
{
    private boolean stop = false;
    private String ip = "141.252.236.91";
    // "141.252.236.91" "192.168.56.56"
    private class SimSocket extends Socket
    {
        private DataInputStream in;
        private OutputStream out;
        private final int bufsize = 4096;
        SocketAddress socket = new InetSocketAddress(ip, 1337);
        
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
    
    private Connection.SimSocket simSocket;
    
    public Connection() throws Exception
    {
        //try
        //{
            simSocket = new Connection.SimSocket(InetAddress.getByName(ip), 1337);
            System.out.println(simSocket.socket.toString());
            if(simSocket != null)
                write("Simulator");
        //}
        //catch(Exception e)
        //{
        //    System.out.println("Connection.Connection() - Cannot initialize or write to socket. - " + e);
        //    stop();
        //}
    }
    
    public void stop()
    {
        try
        {
            stop = true;
            if(simSocket != null)
            {
                simSocket.write("disconnect");
                simSocket.close();
                simSocket = null;
            }
        }
        catch(Exception e)
        {
            System.out.println("Connection.stop() - Cannot close connection.");
        }
    }
    
    public String read() throws Exception
    {
        if(simSocket != null)
        {
            String input = simSocket.read();
            
            if(input.contentEquals("disconnect"))
            {
                stop();
                return "Disconnected from server.";
            }
            return input;
        }
        else
        {
            throw new Exception("Connection.read() - simSocket = null");
        }
    }
    
    public void write(String message) throws Exception
    {
        if(simSocket != null)
            simSocket.write(message);
        else
            throw new Exception("Connection.write() - simSocket = null");
    }
    
    public Thread connectionThread()
    {
        return new Thread(new Runnable() 
        {
            public void run() 
            {
                while (true && !stop) 
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
            }
        });
    }
    
    public void reconnect()
    {
        try 
        {
            if (simSocket==null && !stop)
            {
                try
                {
                    simSocket = new Connection.SimSocket(InetAddress.getByName(ip), 1337);
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
            System.out.println("Could not reconnect, trying again in 5.");
        }
    }
}