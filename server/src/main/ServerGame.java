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
        new Thread(server).start();
    }
    
    private void updateClients() {
        for(ClientServer cs : server.getAdded()) {
            cHandler.init(cs);
        }
        server.clearAdded();
        for(ClientServer cs : server.getClientServers()) {
            String input = cs.getInput();
            if(input == null) {
                continue;
            }
            if(input.equals("quit")) {
                cHandler.quit(cs);
                continue;
            }
            setFlag(cs.toString() + "-input", input);
            if (cHandler.update(cs)) {
                cHandler.quit(cs);
            }
        }
    }
    
    @Override
    public void update() {
        updateClients();
        super.update();
    }
    
    public ArrayList<String> getClientNames() {
        ArrayList<String> names = new ArrayList<>();
        for(ClientServer cs : server.getClientServers()) {
            names.add(cs.toString());
        }
        return names;
    }
}
