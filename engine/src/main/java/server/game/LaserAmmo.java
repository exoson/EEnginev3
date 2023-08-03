package server.game;

import java.util.ArrayList;
import common.game.Gameobject;
import common.game.Vector2f;
import common.game.Vector3f;

/**
 *
 * @author emil
 */
public class LaserAmmo extends AmmoBehavior
{
    private static final int MOVESPEED = 10,LENGTH = 2500;
    private final ArrayList<Vector2f> points = new ArrayList<>();

    @Override
    public void start(Gameobject go) {
        go.setState("AmmoBehaviorlifetime", LENGTH/MOVESPEED);
        super.start(go);
    }
    @Override
    public void update(Gameobject go) {
        super.update(go);
        String pointString = "";
        for(int i = 0; i < points.size(); i++) {
            pointString += "" + points.get(i).getX() + "," + points.get(i).getY();
            if (i < points.size()-1) {
                pointString += "/";
            }
        }
        Main.getGame().updateClients(go.getState("id") + ":LaserAmmopoints:" + pointString);
    }
    @Override
    public void move(Gameobject go) {
        Transform tf = (Transform)go.getBehavior("Transform");
        Vector3f pos = tf.getPosition();
        for(int i = 0; i < MOVESPEED; i++) {
            super.move(go);
            super.checkHits(go);
            points.add(new Vector2f(pos.getX(),pos.getY()));
        }
        while(points.size() > MOVESPEED*3) {
            points.remove(0);
        }
    }
}
