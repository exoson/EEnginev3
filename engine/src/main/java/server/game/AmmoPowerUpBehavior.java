package server.game;

import java.util.ArrayList;
import common.game.Behavior;
import common.game.Gameobject;

/**
 *
 * @author Lime
 */
public class AmmoPowerUpBehavior implements Behavior{

    private String pUpName;

    @Override
    public void start(Gameobject go) {
        pUpName = (String)go.getState("AmmoPowerUpBehaviorpUpName");
    }

    @Override
    public void update(Gameobject go) {
        checkHits(go);
    }

    private void checkHits(Gameobject go) {
        Transform tf = (Transform)go.getBehavior("Transform");
        ArrayList<Gameobject> gos = new ArrayList<>();
        for(String s : Main.getGame().getClientNames()) {
            int id = (int)Main.getGame().getFlag(s + "-player");
            Gameobject player = Main.getGame().getObject(id);
            if(player != null) {
                gos.add(player);
            }
        }
        ArrayList<Gameobject> colls = Physics.sphereCollide(tf, tf.getSX()/2, gos);
        for(Gameobject coll : colls) {
            coll.setState("AmmoTemplate", pUpName);
            String clientName = (String)coll.getState("client");
            Main.getGame().updateClient(clientName, "powerUpIcon:" +  go.getState("init"));
        }
        if(!colls.isEmpty()) {
            Main.getGame().removeObject((int)go.getState("id"));
        }
    }
    @Override
    public void render(Gameobject go) {

    }

}
