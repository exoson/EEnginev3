package behaviors;

import main.Behavior;
import main.Delay;
import main.Gameobject;
import main.Main;
import math.Vector2f;

/**
 *
 * @author Lime
 */
public class AmmoBehavior implements Behavior {

    private float speed;
    private Delay deathDel;
    
    @Override
    public void start(Gameobject go) {
        this.speed = Float.parseFloat((String)go.getState("AmmoBehaviorspeed"));
        deathDel = new Delay(5000);
        deathDel.start();
    }

    @Override
    public void update(Gameobject go) {
        Transform tf = (Transform)go.getBehavior("Transform");
        Vector2f v = new Vector2f((float)Math.cos(tf.getRotation().getZ()+Math.PI/2),(float)Math.sin(tf.getRotation().getZ()+Math.PI/2)).mult(speed);
        tf.move(v.mult(-1));
        if(deathDel.over()) {
            Main.getGame().removeObject((int)go.getState("id"));
        }
    }

    @Override
    public void render(Gameobject go) {
        
    }
    
}