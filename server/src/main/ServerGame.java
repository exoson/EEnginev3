package main;

import java.util.ArrayList;

/**
 *
 * @author Lime
 */
public class ServerGame extends Game {

    private final Server server;
    
    private final ClientHandler cHandler;
    
    public ServerGame(GameMode gMode, ClientHandler cHandler) {
        super(gMode);
        this.cHandler = cHandler;
        server = new Server();
        Thread sThread = new Thread(server);
        sThread.setDaemon(true);
        sThread.start();
    }
    
    private void updateClients() {
        for(ClientServer cs : server.getAdded()) {
            setFlag(cs.toString() + "-update", "");
            for(Gameobject go : gObjects) {
                cs.sendMsg((String)go.getState("init"));
            }
            cHandler.init(cs);
        }
        server.clearAdded();
        server.clearRemoved(cHandler);
        for(ClientServer cs : server.getClientServers().values()) {
            String input = cs.getInput();
            if(input != null) {
                setFlag(cs.toString() + "-input", input);
            }
            if (cHandler.update(cs)) {
                cHandler.quit(cs);
            }
            cs.sendMsg("up" + (String)getFlag(cs.toString()+ "-update"));
            setFlag(cs.toString()+ "-update", "");
        }
    }
    
    @Override
    public void update() {
        updateClients();
        super.update();
    }
    
    public ArrayList<String> getClientNames() {
        ArrayList<String> names = new ArrayList<>();
        for(ClientServer cs : server.getClientServers().values()) {
            names.add(cs.toString());
        }
        return names;
    }
    
    @Override
    protected void initObject(String spec) {
        super.initObject(spec);
        server.broadcast(spec);
    }
    
    @Override
    public void removeObject(Gameobject go) {
        super.removeObject(go);
        server.broadcast("rm;" + (String)go.getState("id"));
    }
    
    @Override
    public void removeObject(int id) {
        super.removeObject(id);
        System.out.println("rm;" + id);
        server.broadcast("rm;" + id);
    }
    
    public void updateClients(String update) {
        for(ClientServer cs : server.getClientServers().values()) {
            updateClient(cs.toString(), update);
        }
    }
    
    public void updateClient(String serverStr, String update) {
        appendFlag(serverStr + "-update", update);
    }
    
    public boolean getClientKey(String clientName, int keyCode) {
        String keys = (String)getFlag(clientName + "-input");
        if(keys.length() < keyCode) {
            return false;
        }
        return keys.charAt(keyCode) == '1';
    }
}
