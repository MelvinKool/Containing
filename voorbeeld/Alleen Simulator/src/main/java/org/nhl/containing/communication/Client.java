package org.nhl.containing.communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

/**
 * Client.
 */
public class Client implements Runnable {

    private final int portNumber = 6666;
    private final String serverName = "localhost";
    private Socket socket;
    private ListenRunnable listenRunnable;
    private SendRunnable sendRunnable;
    private boolean running;

    public Client() {
    }

    @Override
    public void run() {
        // Initialise the client.
        try {
            // Try to connect to the server.
            while (true) {
                try {
                    // Open up the socket.
                    socket = new Socket(serverName, portNumber);
                    break;
                } catch (SocketException e) {
                    System.out.println("Could not establish connection with " + serverName + " " + portNumber + ". Reconnecting.");
                    try {
                        Thread.sleep(1000);
                    } catch (Throwable e_) {
                        e_.printStackTrace();
                    }
                }
            }

            listenRunnable = new ListenRunnable(new BufferedReader(new InputStreamReader(socket.getInputStream())));
            sendRunnable = new SendRunnable(new PrintWriter(socket.getOutputStream(), true));

            Thread listenThread = new Thread(listenRunnable);
            listenThread.setName("ListenThread");
            Thread sendThread = new Thread(sendRunnable);
            sendThread.setName("SendThread");

            listenThread.start();
            sendThread.start();
            running = true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Enter the main loop.
        while (running) {
            try {
                // Do nothing.
                Thread.sleep(1000);
            } catch (Throwable e) {
                e.printStackTrace();
            }
            // In case the client shut down the listener, shut down everything.
            if (!listenRunnable.isRunning()) {
                this.stop();
            }
        }

    }

    /**
     * Stop the client and its runnables.
     */
    public void stop() {
        try {
            socket.close();
        } catch (Throwable e) {
        }
        try {
            listenRunnable.stop();
        } catch (Throwable e) {
        }
        try {
            sendRunnable.stop();
        } catch (Throwable e) {
        }
        running = false;
    }

    /**
     * Pop a message from the listener's message pool, and return it.
     * </p>
     * If no listener exists yet, return null instead. This is also the default
     * return value from the listener if there is no message in the message
     * pool.
     *
     * @return An XML instruction from the backend.
     */
    public String getMessage() {
        try {
            return listenRunnable.getMessage();
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * Send a message to the backend server.
     * </p>
     * This function encapsulates the message in <Simulation> tags.
     *
     * @param message XML message.
     */
    public void writeMessage(String message) {
        sendRunnable.writeMessage("<Simulation>" + message + "</Simulation>");
    }
}
