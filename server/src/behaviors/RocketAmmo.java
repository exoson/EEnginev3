
package behaviors;

import game.DeathMatch;
import game.Map;
import java.util.ArrayList;
import main.Delay;
import main.Gameobject;
import main.Main;
import main.Util;
import math.Vector2i;
import math.Vector3f;

/**
 *
 * @author emil
 */
public class RocketAmmo extends AmmoBehavior
{
    ArrayList<Vector2i> path;
    private Delay trackDel;
    @Override
    public void start(Gameobject go) {
        super.start(go);
        trackDel = new Delay(1000);
        trackDel.start();
        speed = 5;
    }
    @Override
    protected void move(Gameobject go) {
        if(!trackDel.over()) {
            super.move(go);
            return;
        }
        Transform tf = go.getBehavior("Transform");
        Transform targetTf;
        //go.setIsSolid(false);
        float minDist = Float.POSITIVE_INFINITY;
        Gameobject closest = null;
        for(String s : Main.getGame().getClientNames()) {
            int id = (int)Main.getGame().getFlag(s + "-player");
            Gameobject player = Main.getGame().getObject(id);
            targetTf = player.getBehavior("Transform");
            float dist = Util.dist(targetTf.getPosition(), tf.getPosition());
            if(dist < minDist) {
                minDist = dist;
                closest = player;
            }
        }
        
        if(closest == null) {
            super.move(go);
        } else {
            targetTf = closest.getBehavior("Transform");
            System.out.println(targetTf);
            path = Util.findPath(tf.getPosition(), 
                                 targetTf.getPosition(),
                                 ((DeathMatch)Main.getGame().getgMode()).getMap().getSquares());
            System.out.println(path.size());
            if(path != null) {
                Vector2i dest;
                if(path.size() > 1) {
                    dest = path.get(path.size() > 2 ? path.size()-3 : 0).mult(Map.SQRSIZE);
                } else {
                    dest = targetTf.getPosition().as2i();
                }
                Vector3f heading = new Vector3f(dest).minus(tf.getPosition());
                if(heading.length() < speed) {
                    tf.move(heading);
                } else {
                    tf.move(heading.normalize().mult(speed));
                }
            } else {
                super.move(go);
            }
        }
    }
    @Override
    public void render(Gameobject go) {
    }
}
