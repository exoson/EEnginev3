package server.game;

import java.util.ArrayList;
import common.game.Delay;
import common.game.Gameobject;
import common.game.Vector2i;
import common.game.Vector3f;

/**
 *
 * @author emil
 */
public class RocketAmmo extends AmmoBehavior
{
    ArrayList<Vector2i> path;
    private Delay trackDel;
    private float rotSpeed = 0.01f;

    @Override
    public void start(Gameobject go) {
        super.start(go);
        trackDel = new Delay(1000);
        trackDel.start();
    }
    @Override
    protected void move(Gameobject go) {
        if(!trackDel.over()) {
            super.move(go);
            return;
        }
        System.out.println("asdf");
        Transform tf = go.getBehavior("Transform");
        Transform targetTf;
        //go.setIsSolid(false);
        float minDist = Float.POSITIVE_INFINITY;
        Gameobject closest = null;
        for(String s : Main.getGame().getClientNames()) {
            int id = (int)Main.getGame().getFlag(s + "-player");
            Gameobject player = Main.getGame().getObject(id);
            if(player == null) continue;
            targetTf = player.getBehavior("Transform");
            float dist = Util.dist(targetTf.getPosition(), tf.getPosition());
            if(dist < minDist) {
                minDist = dist;
                closest = player;
            }
        }

        if(closest != null) {
            targetTf = closest.getBehavior("Transform");

            path = Util.findPath(tf.getPosition(),
                                 targetTf.getPosition(),
                                 ((DeathMatch)Main.getGame().getgMode()).getMap().getSquares());

            if(path != null) {
                Vector3f dest;
                if(path.size() > 1) {
                    dest = new Vector3f(path.get(path.size() > 2 ? path.size()-3 : 0).mult(Map.SQRSIZE));
                } else {
                    dest = targetTf.getPosition();
                }
                Vector3f targetHeading = dest.minus(tf.getPosition()).normalize();
                float targetRot = (float)Math.acos(-targetHeading.getY());
                targetRot = (targetHeading.getX() > 0 ? targetRot : 2*(float)Math.PI-targetRot);
                float dRot = tf.getRotation().getZ() - targetRot;
                float prior = dRot > 0 ? -1 : 1;
                tf.rotate((Math.abs(dRot) > Math.PI ? -1 * prior : 1 * prior) * rotSpeed);
            }
        }
        super.move(go);
    }
}
