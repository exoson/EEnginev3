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
            resetDelay.start();
            return true;
        }
        return false;
    }

    @Override
    public void render() {
        
    }

    @Override
    public void reset() {
        ArrayList<String> clients = Main.getGame().getClientNames();
        for(String cName : clients) {
            int playerId = (int)Main.getGame().getFlag(cName + "-player");
            Gameobject player = Main.getGame().getObject(playerId);
            Transform tf = (Transform)player.getBehavior("Transform");
            tf.setPosition(Vector3f.random().mult(new Vector3f(Map.WIDTH*Map.SQRSIZE, Map.HEIGHT*Map.SQRSIZE, 0)));
        }
        map.initRandomMap();
        Main.getGame().updateClients("map:" + map.toString() + ";res:1");
    }
    
    public Map getMap() {
        return map;
    }
}
