package game;

import behaviors.Transform;
import java.util.ArrayList;
import main.Delay;
import main.GameMode;
import main.Gameobject;
import main.Main;
import math.Vector3f;

/**
 *
 * @author Lime
 */
public class DeathMatch implements GameMode {

    private Map map;
    
    private Delay resetDelay;
    
    @Override
    public void start() {
        map = new Map();
        resetDelay = new Delay(3000);
        resetDelay.start();
    }

    @Override
    public boolean update() {
        if(Main.getGame().getClientNames().size() < 2) {
            resetDelay.start();
        }
        if(resetDelay.over()) {
            resetDelay.terminate();
            return true;
        }
        ArrayList<String> clients = Main.getGame().getClientNames();
        int alivePlayers = 0;
        for(String cName : clients) {
            int playerId = (int)Main.getGame().getFlag(cName + "-player");
            Gameobject player = Main.getGame().getObject(playerId);
            if(player != null) {
                alivePlayers++;
            }
        }
        if(!resetDelay.active() && alivePlayers < 2) {
            resetDelay.start();
        }
        return false;
    }

    @Override
    public void render() {
        
    }

    @Override
    public void reset() {
        ArrayList<String> clients = Main.getGame().getClientNames();
        Main.getGame().removeAll();
        for(String cName : clients) {
            Vector3f pos = Vector3f.random().mult(new Vector3f((Map.WIDTH-3)*Map.SQRSIZE, (Map.HEIGHT-3)*Map.SQRSIZE, 0)).add(new Vector3f(1.5f*Map.SQRSIZE,1.5f*Map.SQRSIZE,0));
            int playerId = Main.getGame().addObject("in;client:" + cName + ";file:tank;Transform:pos:" + pos.toString());
            Main.getGame().setFlag(cName + "-player", playerId);
        }
        map.initRandomMap();
        Main.getGame().updateClients("map:" + map.toString() + ";res:1");
    }
    
    public Map getMap() {
        return map;
    }
}
