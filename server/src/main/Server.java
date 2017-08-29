package main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server implements Runnable{
    
    private HashMap<String, ClientServer> clients = new HashMap<>();
    private ArrayList<ClientServer> newClients = new ArrayList<>();
    private ArrayList<ClientServer> removedClients = new ArrayList<>();
    boolean running;
    
//    public static void main(String[] args) {
//        Server s;
//        s = new Server();
//        Thread t = new Thread(s);
//        t.start();
//        Scanner scan = new Scanner(System.in);
//        boolean running = true;
//        System.out.println("Server running");
//        while(running){
//            String input = scan.nextLine();
//            s.broadcast(input);
//            if(input.equals("quit")){
//                s.stop();
//                running = false;
//            }
//        }
//    }
    
    public void broadcast(String msg) {
        for(ClientServer c : getClientServers().values()) {
            c.sendMsg(msg);
        }
    }
    
    public void sendMsg(String client, String msg) {
        getClientServers().get(client).sendMsg(msg);
    }
    
    private void stop() {
        running = false;
        for(ClientServer c : getClientServers().values()) {
            c.stop();
        }
    }
    
    @Override
    public void run() {
        try (ServerSocket ss = new ServerSocket(8000)) {
            ss.setSoTimeout(1000);
            running = true;
            while(running){
                Socket client = null;
                try {
                    client = ss.accept();
                }
                catch(IOException e){ }
                if(client != null) {
                    ClientServer clientServer = new ClientServer(this, client);
                    newClients.add(clientServer);
                    new Thread(clientServer).start();
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @return the list of clients
     */
    public HashMap<String, ClientServer> getClientServers() {
        return clients;
    }

    public ArrayList<ClientServer> getAdded() {
        return newClients;
    }
    
    public void remove(ClientServer cs) {
        removedClients.add(cs);
    }

    public void clearAdded() {
        for(ClientServer cs : newClients) {
            clients.put(cs.toString(), cs);
        }
        for(ClientServer cs : clients.values()) {
            newClients.remove(cs);
        }
    }
    
    public void clearRemoved(ClientHandler ch) {
        for(ClientServer cs : removedClients) {
            if(clients.containsKey(cs.toString())) {
                clients.remove(cs.toString());
            }
            if(newClients.contains(cs)) {
                newClients.remove(cs);
            }
            ch.quit(cs);
        }
        removedClients.removeAll(removedClients);
    }
    
    public ClientServer getClientServer(String cName) {
        return clients.get(cName);
    }
}
