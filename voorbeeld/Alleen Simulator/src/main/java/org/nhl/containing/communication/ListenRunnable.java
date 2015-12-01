package org.nhl.containing.communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 */
public class ListenRunnable implements Runnable {
    BufferedReader in;
    ConcurrentLinkedQueue<String> queue;

    private boolean running;

    public ListenRunnable(BufferedReader in) {
        this.in = in;
        this.queue = new ConcurrentLinkedQueue<String>();
    }

    @Override
    public void run() {
        String inputLine;
        this.running = true;

        try {
            while ((inputLine = in.readLine()) != null) {
                if (!running || inputLine.equals("quit")) {
                    break;
                }
                // Write to queue
                queue.add(inputLine);
                System.out.println("Received " + inputLine);
            }
        } catch (IOException e) {
        }

        running = false;
    }

    public void stop() {
        running = false;
    }

    public String getMessage() {
        return queue.poll();
    }

    public boolean isRunning() {
        return running;
    }
}
