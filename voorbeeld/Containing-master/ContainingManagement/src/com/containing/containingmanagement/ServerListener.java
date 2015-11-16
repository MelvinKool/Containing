package com.containing.containingmanagement;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Hendrik
 */
public class ServerListener {

    private String serverName;//server host
    private int port;//port number of server
    private BufferedReader input;//input stream from client
    private Socket client;//client's socket connection
    private PrintWriter output;//output stream from client
   private MainActivity activity;

   

    public ServerListener(MainActivity mainActivity, String ipAdress) {
        serverName = ipAdress;
        port = 6066;
        this.activity = mainActivity;
        Thread listenerThread = new Thread(new Runnable() {
            public void run() {
                ServerListener.this.run();
            }
        });
        listenerThread.start();
        
	}

	/**
     * main methoded that is listining to server
     */
    public void run() {
        try {
            System.out.println("Connecting to " + serverName
                    + " on port " + port);
            client = new Socket(serverName, port);
            System.out.println("Just connected to "
                    + client.getRemoteSocketAddress());
            OutputStream outToServer = client.getOutputStream();
            output =
                    new PrintWriter(outToServer, true);

            input = new BufferedReader(new InputStreamReader(client.getInputStream()));
            sendMessage(new Message(0, new Object[]{"android"}));
        } catch (UnknownHostException ex) {
            Logger.getLogger(ServerListener.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServerListener.class.getName()).log(Level.SEVERE, null, ex);
        }
         
         
        while (true) {
            String s;
            try {
                if (input.ready()) {
                    s = input.readLine();
                        System.out.println("message from ip:" + client.getRemoteSocketAddress());
                        activity.messageRecieved(Message.decodeMessage(s));
                        break;
                }
            } catch (IOException ex) {
                Logger.getLogger(ServerListener.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    /**
     * Send message to server
     * @param message the message you want to send
     */
    void sendMessage(Message message) {
        System.out.println("message to ip:" + client.getRemoteSocketAddress());
        output.println(Message.encodeMessage(message));
    }
}

