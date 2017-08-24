package main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server implements Runnable{
    
    private ArrayList<ClientServer> clients = new ArrayList<>();
    private ArrayList<ClientServer> newClients = new ArrayList<>();
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
        for(ClientServer c : getClientServers()) {
            c.sendMsg(msg);
        }
    }
    
    public void sendMsg(String msg, int client) {
        getClientServers().get(client).sendMsg(msg);
    }
    
    private void stop() {
        running = false;
        for(ClientServer c : getClientServers()) {
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
    public ArrayList<ClientServer> getClientServers() {
        return clients;
    }

    public ArrayList<ClientServer> getAdded() {
        return newClients;
    }
    
   public void remove(ClientServer cs) {
       if(clients.contains(cs)) {
           clients.remove(cs);
       }
       if(newClients.contains(cs)) {
           newClients.remove(cs);
       }
   }

    public void clearAdded() {
        clients.addAll(newClients);
        newClients.removeAll(clients);
    }
}
