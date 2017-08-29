package main;

import java.util.ArrayList;

/**
 *
 * @author Lime
 */
public class ServerGame extends Game {

    private final Server server;
    
    private final ClientHandler cHandler;
    
    private final Delay tickDelay;
    
    public ServerGame(GameMode gMode, ClientHandler cHandler) {
        super(gMode);
        this.tickDelay = new Delay(1000 / 60);
        this.cHandler = cHandler;
        server = new Server();
        Thread sThread = new Thread(server);
        sThread.setDaemon(true);
        sThread.start();
        tickDelay.end();
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
        while(!tickDelay.over()) {Time.sleep(1);}
        tickDelay.start();
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
    public int addObject(String goSpec) {
        goSpec += ";id:" + objId;
        return super.addObject(goSpec);
    }
    
    @Override
    public void removeObject(Gameobject go) {
        super.removeObject(go);
        server.broadcast("rm;" + (int)go.getState("id"));
    }
    
    @Override
    public void removeObject(int id) {
        super.removeObject(id);
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
        return server.getClientServer(clientName).getKey(keyCode);
    }
}
