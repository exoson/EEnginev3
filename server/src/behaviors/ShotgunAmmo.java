
package behaviors;

import main.Gameobject;
import main.Main;
import math.Vector3f;

/**
 *
 * @author emil
 */
public class ShotgunAmmo extends AmmoBehavior
{
    private static final float SPREAD = (float)Math.PI/3;
    private static final int AMMOAMT = 5;
    
    @Override
    public void start(Gameobject go) {
        
    }
    @Override
    public void update(Gameobject go) {
        Main.getGame().removeObject((int)go.getState("id"));
        Transform tf = go.getBehavior("Transform");
        Vector3f v = tf.forward().mult(-tf.getSY());
        String pos = tf.getPosition().add(v).toString();
        for(int i = 0; i < AMMOAMT; i++) {
            String rot = tf.getRotation().add(new Vector3f(0, 0, SPREAD / 5 * i - SPREAD / 2)).toString();
            Main.getGame().addObject("in;file:ammo;Transform:pos:" + pos + ":rot:" + rot);
        }
    }
}
