/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package containingsimulator;

import containing.xml.Message;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
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
    private Main main;//maingame to send message recieved
    private List<String> recievedMessages;
    public boolean running = false;
    /**
     * Listener to server and sender to server
     *
     * @param main
     */
    public ServerListener(Main main) {

        //127.0.0.1 on port 6066 by default
        this.main = main;
        recievedMessages = new ArrayList<String>();
       
    }

    private void startThread()
    {
         Thread listenerThread = new Thread(new Runnable() {
            public void run() {
                ServerListener.this.run();
            }
        });
        listenerThread.start();
    }
    
    
    public void changeConnection(String ip, int port)
    {
        this.serverName = ip;
        this.port = port;
        startThread();
    }
    
    public List<String> getMessages()
    {
        ArrayList temp = new ArrayList<String>(recievedMessages);
        recievedMessages.clear();
        return temp;
    }
    /**
     * main methoded that is listining to server
     */
    public void run() {
        try {
            System.out.println("Connecting to " + serverName
                    + " on port " + port);
            client = new Socket(serverName, port);
            client.setTcpNoDelay(true);
            System.out.println("Just connected to "
                    + client.getRemoteSocketAddress());
            OutputStream outToServer = client.getOutputStream();
            output =
                    new PrintWriter(outToServer, true);
            running = true;

            input = new BufferedReader(new InputStreamReader(client.getInputStream()));
        } catch (UnknownHostException ex) {
            Logger.getLogger(ServerListener.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServerListener.class.getName()).log(Level.SEVERE, null, ex);
        }

        output.println(Message.encodeMessage(new Message(Commands.READY, new String[]{"simulator"})));
        while (true) {
            String s;
            try {
                if (input.ready()) {
                    s = input.readLine();
                    if (!s.isEmpty()) {
                       recievedMessages.add(s);
                          //     System.out.println(s);
               //         main.messageReceivedEvent(Message.decodeMessage(s));
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(ServerListener.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    /**
     * Send message to server
     *
     * @param message the message you want to send
     */
    void sendMessage(Message message) {
       /* System.out.println("message to ip:" + message
                );*/
        output.println(Message.encodeMessage(message));
        output.flush();
    }
}
