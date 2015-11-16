/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingcontroller;

import containing.xml.Message;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Hendrik
 */
public class ServerClient {

    private BufferedReader input;//input stream from client
    private Socket client;//client's socket connection
    private PrintWriter output;//output stream from client
    private Server server;//server to relay message's
    private boolean sendOnly = false;
    private boolean android = true;

    /**
     *
     * @param client
     * @param s
     */
    public ServerClient(Socket client, Server s) {
        this.client = client;
        server = s;
        try {
            client.setTcpNoDelay(true);
        } catch (SocketException ex) {
            Logger.getLogger(ServerClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    ServerClient(Socket client, Server s, boolean b) {
        this(client, s);
        sendOnly = b;
    }

    /**
     * Send message to client, must be in xml format
     *
     * @param message
     */
    public void sendMessage(String message) {
        if (!android) {
            //   System.out.println("message to ip:" + client.getRemoteSocketAddress());
            //System.out.println(message);

            output.println(message);
            output.flush();
            /*  try {
             //   Thread.sleep(10/Controller.Speed);
             } catch (InterruptedException ex) {
             Logger.getLogger(ServerClient.class.getName()).log(Level.SEVERE, null, ex);
             }*/
        }
    }

    /**
     * send message to android phone
     * @param message
     */
    public void sendAndroidMessage(String message) {
     //  System.out .println("message to ip:" + client.getRemoteSocketAddress());
        //System.out.println(message);
        output.println(message);

    }
  //  String last = "";

    /**
     * main function that listens to client
     */
    public void Run() {
        boolean firstMessage = true;
        try {
            input = new BufferedReader(new InputStreamReader(client.getInputStream()));
            output = new PrintWriter(client.getOutputStream(), true);
        } catch (IOException e) {
            System.out.println("Read failed");
        }
        while (true) {
            try {
             //   System.out.println("lisining" +last);
                if (input.ready()) {

                    String s = input.readLine();
                        //    System.out.println("heard" +s);
                   // last = s;
                    if (firstMessage) {
                        firstMessage = false;
                        Message m = Message.decodeMessage(s);
                        if (((String) m.getParameters()[0]).equalsIgnoreCase("simulator")) {
                            server.controller.PrintMessage("Connected simulator - " + client.getRemoteSocketAddress());
                            android = false;
                        } else {
                            android = true;
                            //server.controller.PrintMessage("Connected Android - " + client.getRemoteSocketAddress());

                            sendAndroidMessage(server.controller.getAndroidData());

                            break;
                        }

                    } else {
                        if (!s.isEmpty()) {
                            server.MessageRecieved(s);
                          //  System.out.println("message from ip:" + client.getRemoteSocketAddress());
                            // System.out.println(s);
                        }
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(ServerClient.class.getName()).log(Level.SEVERE, null, ex);
                return;
            }

        }
    }
}
