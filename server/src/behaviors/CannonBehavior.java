package behaviors;

import main.Behavior;
import main.Delay;
import main.Gameobject;
import main.Input;
import main.Main;
import math.Vector2f;

/**
 *
 * @author Lime
 */
public class CannonBehavior implements Behavior{

    private String cName;
    private int shootingKey;
    private Delay reloadDel;
    
    @Override
    public void start(Gameobject go) {
        this.cName = (String)go.getState("TankMovementclient");
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
        Vector2f v = new Vector2f((float)Math.cos(tf.getRotation().getZ()+Math.PI/2),(float)Math.sin(tf.getRotation().getZ()+Math.PI/2)).mult(-tf.getSY());
        //int playerId = game.addObject("in;Transform:pos:10,10,0:rot:0,0,0;Animator;TankMovement:speed:10.0:rotSpeed:0.05:client:" + cs.toString());
        String pos = tf.getPosition().add(v).toString();
        String rot = tf.getRotation().toString();
        return "in;Transform:pos:" + pos + ":rot:" + rot + ":size:10,10,0;Animator;AmmoBehavior:speed:20;";
    }
}
