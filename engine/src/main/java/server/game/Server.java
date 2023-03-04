package server.game;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.AuthenticationException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;

public class Server implements Runnable {

    private HashMap<String, ClientServer> clients = new HashMap<>();
    private ArrayList<ClientServer> newClients = new ArrayList<>();
    private ArrayList<ClientServer> removedClients = new ArrayList<>();
    boolean running;
    private String expectedSecret;

    public Server(String[] args) {
        expectedSecret = args[0];
    }

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

    public void stop() {
        running = false;
        for(ClientServer c : getClientServers().values()) {
            c.stop();
        }
    }

    @Override
    public void run() {
        try {
            //System.setProperty("javax.net.ssl.keyStoreType", "jks");
            System.setProperty("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol");
            Path keyStorePath = Paths.get("/home/exoman/EEnginev3/proKeystore");
            System.setProperty("javax.net.ssl.keyStore", keyStorePath.toString());
            SSLServerSocketFactory ssocketFactory = (SSLServerSocketFactory)SSLServerSocketFactory.getDefault();
            SSLServerSocket ss = (SSLServerSocket) ssocketFactory.createServerSocket(12322);
            ss.setSoTimeout(1000);
            running = true;
            while(running){
                SSLSocket client = null;
                try {
                    client = (SSLSocket) ss.accept();
                    client.startHandshake();
                }
                catch(IOException e){ }
                if(client != null && !client.isClosed()) {
                    try {
                        ClientServer clientServer = new ClientServer(this, client, expectedSecret);
                        newClients.add(clientServer);
                        new Thread(clientServer).start();
                    } catch(AuthenticationException ae) {
                        Logger.getLogger(Server.class.getName()).log(Level.INFO, null, ae);
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
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
        removedClients.clear();
    }

    public ClientServer getClientServer(String cName) {
        return clients.get(cName);
    }
}
