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
    private Delay reloadDel;
    private int shootingKey;
    
    @Override
    public void start(Gameobject go) {
        this.shootingKey = Input.KEY_SPACE;
        this.cName = (String)go.getState("client");
        reloadDel = new Delay(1000);
        reloadDel.end();
    }

    @Override
    public void update(Gameobject go) {
        //if(go.getIsDead()) return;
        
        if(Input.getKey(cName, shootingKey)) {
            Main.getGame().updateClients(go.getState("id") + ":CannonPlay:true");
            // @TODO implement this in some nicer way
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
