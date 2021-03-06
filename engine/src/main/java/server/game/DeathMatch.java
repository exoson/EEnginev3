package server.game;

import java.util.ArrayList;
import java.util.HashMap;
import common.game.Delay;
import common.game.GameMode;
import common.game.Gameobject;
import common.game.Vector3f;

/**
 *
 * @author Lime
 */
public class DeathMatch implements GameMode {

    private static final int MAX_POINTS = 10;

    private Map map;

    private Delay resetDelay;
    private Delay quitDelay;
    private boolean uMap;

    private HashMap<String, Integer> points;

    @Override
    public void start() {
        uMap = false;
        map = new Map();
        resetDelay = new Delay(3000);
        resetDelay.start();
        quitDelay = new Delay(30000);
        quitDelay.start();
    }

    @Override
    public boolean update() {
        if(uMap) {
            updateMap();
            uMap = false;
        }
        if(Main.getGame().getClientNames().size() < 2) {
            resetDelay.start();
            if(!quitDelay.active()) {
                quitDelay.start();
            }
        }
        if(resetDelay.over()) {
            resetDelay.terminate();
            quitDelay.terminate();
            return true;
        }
        if(quitDelay.over()) {
            Main.getGame().setFlag("running", false);
            ArrayList<String> clients = Main.getGame().getClientNames();
            for(String cName : clients) {
                System.out.println("connected:" + cName);
            }
            return false;
        }

        if(Math.random() > 0.999) {
            Vector3f pos = Vector3f.random()
                    .mult(new Vector3f((Map.WIDTH-5)*Map.SQRSIZE, (Map.HEIGHT-5)*Map.SQRSIZE, 0))
                    .add(new Vector3f(1.5f*Map.SQRSIZE, 1.5f*Map.SQRSIZE,0));
            Main.getGame().addObject("in;file:powerup;Transform:pos:" + pos.toString());
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
        if (points == null) {
            points = new HashMap<>();
            for (String cName : clients) {
                points.put(cName, 0);
            }
        } else {
            for (String cName : clients) {
                int playerId = (int)Main.getGame().getFlag(cName + "-player");
                Gameobject player = Main.getGame().getObject(playerId);
                if (player != null) {
                    points.put(cName, points.get(cName) + 1);
                    if ((points.get(cName) >= MAX_POINTS)) {
                        Main.getGame().setFlag("running", false);
                        System.out.println("winner:" + cName);
                        return;
                    }
                }
            }
        }
        Main.getGame().removeAll();
        int curTank = 0;
        for(String cName : clients) {
            Main.getGame().updateClients("player" + curTank + ":" + cName);
            Main.getGame().updateClients("player" + curTank + "points:" + points.get(cName));
            Vector3f pos = Vector3f.random()
                    .mult(new Vector3f((Map.WIDTH-5)*Map.SQRSIZE, (Map.HEIGHT-5)*Map.SQRSIZE, 0))
                    .add(new Vector3f(1.5f*Map.SQRSIZE, 1.5f*Map.SQRSIZE,0));
            int playerId = Main.getGame().addObject("in;client:" + cName + ";file:tank;Transform:pos:" + pos.toString() + (curTank==0 ? "" : ";Animator:anims:tankblue,default,tankHit,tankHit;"));
            curTank++;
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
