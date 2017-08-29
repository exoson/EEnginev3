package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientServer implements Runnable {
    
    private final Server server;
    private final Socket clientSocket;
    private final BufferedReader in;
    private final PrintWriter out;
    private boolean running;
    
    private String clientInput;
    private String[] splitClientInput;
    private final ReadWriteLock inLock;
    
    public ClientServer(Server s, Socket client) throws IOException {
        client.setSoTimeout(0);
        client.setKeepAlive(true);
        clientSocket = client;
        server = s;
        inLock = new ReentrantReadWriteLock();
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new PrintWriter(clientSocket.getOutputStream());
    }

    @Override
    public void run() {
        running = true;
        clientInput = "";
        splitClientInput = new String[0];
        while(running && !clientSocket.isClosed()) {
            try {
                clientInput = in.readLine();
                if(clientInput.equals("quit")) {
                    stop();
                } else {
                    inLock.writeLock().lock();
                    try {
                        splitClientInput = clientInput.split(",");
                        //System.out.println(Arrays.toString(splitClientInput));
                    } finally {
                        inLock.writeLock().unlock();
                    }
                }
            } catch (IOException ex) {
                stop();
            }
        }
    }
    public void sendMsg(String msg) {
        out.println(msg);
        out.flush();
    }
    public void stop() {
        running = false;
        try {
            clientSocket.close();
            server.remove(this);
            System.out.println("Client disconnected: " + clientSocket.getInetAddress().toString());
        } catch (IOException ex) {
            Logger.getLogger(ClientServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getInput() {
        return clientInput;
    }
    
    public boolean getKey(int keyCode) {
        inLock.readLock().lock();
        try {
            if(splitClientInput.length == 0) return false;
            if(splitClientInput[0].isEmpty()) return false;
            for(String key : splitClientInput) {
                System.out.println(key + ":" + keyCode);
                if(Integer.parseInt(key) == keyCode) {
                    return true;
                }
            }
        } finally {
            inLock.readLock().unlock();
        }
        return false;
    }
    
    @Override
    public String toString() {
        return clientSocket.toString();
    }
}
