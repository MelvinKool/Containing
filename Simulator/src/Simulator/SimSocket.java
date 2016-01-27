/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulator;

import java.io.DataInputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 *
 * @author erwin
 */
public class SimSocket extends Socket
    {
        private DataInputStream in;
        private OutputStream out;
        private final int bufsize = 4096;
        
        public SimSocket(String ip, int port) throws Exception
        {
            super(ip, port);
            in = new DataInputStream(getInputStream());
            out = getOutputStream();
            this.setSoTimeout(1);
        }
        
        public void write(String message) throws Exception
        {
            if(message.length() > bufsize - 1)
                throw new Exception("SimSocket.write() - message too long.");
            out.write((message+"\n").getBytes());
        }
        
        public String read() throws Exception
        {
            return in.readLine();
        }
    }
