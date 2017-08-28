package behaviors;

import main.Behavior;
import main.Delay;
import main.Gameobject;
import main.Input;
import main.Main;
import math.Vector3f;

/**
 *
 * @author Lime
 */
public class CannonBehavior implements Behavior {

    private String cName;
    private int shootingKey;
    private Delay reloadDel;
    
    @Override
    public void start(Gameobject go) {
        this.cName = (String)go.getState("client");
        this.shootingKey = Input.KEY_SPACE;
        reloadDel = new Delay(1000);
        reloadDel.end();
    }

    @Override
    public void update(Gameobject go) {
        //if(go.getIsDead()) return;
        
        if(Input.getKey(cName, shootingKey)) {
            // FIXME implement this in some nicer way
            if(reloadDel.over()) {
                Main.getGame().addObject(createAmmo(go));
                reloadDel.start();
            }
        }
    }

    @Override
    public void render(Gameobject go) {
        
    }
    
    protected String createAmmo(Gameobject go) {
        Transform tf = (Transform)go.getBehavior("Transform");
        Vector3f v = tf.forward().mult(-tf.getSY());
        String pos = tf.getPosition().add(v).toString();
        String rot = tf.getRotation().toString();
        return "in;file:ammo;Transform:pos:" + pos + ":rot:" + rot;
    }
}
