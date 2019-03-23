package server.game;

import java.util.ArrayList;
import common.game.Delay;
import common.game.GameMode;
import common.game.Gameobject;
import common.game.Vector3f;

/**
 *
 * @author Lime
 */
public class DeathMatch implements GameMode {

    private Map map;

    private Delay resetDelay;
    private boolean uMap;

    @Override
    public void start() {
        uMap = false;
        map = new Map();
        resetDelay = new Delay(3000);
        resetDelay.start();
    }

    @Override
    public boolean update() {
        if(uMap) {
            updateMap();
            uMap = false;
        }
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

        if(Math.random() > 0.999) {
            Vector3f pos = Vector3f.random()
                    .mult(new Vector3f((Map.WIDTH-5)*Map.SQRSIZE, (Map.HEIGHT-5)*Map.SQRSIZE, 0))
                    .add(new Vector3f(1.5f*Map.SQRSIZE, 1.5f*Map.SQRSIZE,0));
            Main.getGame().addObject("in;file:powerup;Transform:pos:" + pos.toString());
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
            Vector3f pos = Vector3f.random()
                    .mult(new Vector3f((Map.WIDTH-5)*Map.SQRSIZE, (Map.HEIGHT-5)*Map.SQRSIZE, 0))
                    .add(new Vector3f(1.5f*Map.SQRSIZE, 1.5f*Map.SQRSIZE,0));
            int playerId = Main.getGame().addObject("in;client:" + cName + ";file:tank;Transform:pos:" + pos.toString());
            Main.getGame().setFlag(cName + "-player", playerId);
        }
        uMap = true;
    }

    private void updateMap() {
        map.initRandomMap();
        Main.getGame().updateClients("map:" + map.toString() + ";res:1");
    }

    public Map getMap() {
        return map;
    }
}