package Simulator;

import java.net.*;
import java.io.*;

public class Connection
{
    private class SimSocket extends Socket
    {
        private InputStream in;
        private OutputStream out;
        private final int bufsize = 4096;
        
        public SimSocket(InetAddress ip, int port) throws Exception
        {
            super(ip, port);
            in = getInputStream();
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
            byte[] message = new byte[bufsize];
            int count = in.read(message, 0, bufsize-1);
            if(count > 0 && message[bufsize -1] == '\n')
                count--;
            message[count] = '\0';
            return new String(message);
        }
    }
    
    private SimSocket simSocket;
    
    public Connection()
    {
        try
        {
            simSocket = new SimSocket(InetAddress.getByName("localhost"), 1337);
            if(simSocket != null)
                write("Simulator");
        }
        catch(Exception e)
        {
            System.out.println("Connection.Connection() - Cannot initialize or write to socket. - " + e);
            stop();
        }
    }
    
    public void stop()
    {
        try
        {
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
            if(input.contains("disconnect"))// == "disconnect")
            {
                stop();
                return "Disconnected from server.";
            }
            return input;
        }
        else
            throw new Exception("Connection.read() - simSocket = null");
    }
    
    public void write(String message) throws Exception
    {
        if(simSocket != null)
            simSocket.write(message);
        else
            throw new Exception("Connection.write() - simSocket = null");
    }
}