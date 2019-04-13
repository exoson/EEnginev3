package server.game;

import common.game.Behavior;
import common.game.Delay;
import common.game.Gameobject;
import common.game.Vector3f;

/**
 *
 * @author Lime
 */
public class CannonBehavior implements Behavior {

    private Delay reloadDel;
    private int shootingKey;
    private String ammoTemplate;

    @Override
    public void start(Gameobject go) {
        this.shootingKey = Input.KEY_SPACE;
        reloadDel = new Delay(1000);
        reloadDel.end();
        go.setState("AmmoTemplate", "rocketAmmo");
    }

    @Override
    public void update(Gameobject go) {
        //if(go.getIsDead()) return;

        ammoTemplate = (String)go.getState("AmmoTemplate");
        if(Input.getKey(go, shootingKey)) {
            // @TODO implement this in some nicer way
            if(reloadDel.over()) {
                Main.getGame().updateClients(go.getState("id") + ":CannonPlay:true");
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
        return "in;file:" + ammoTemplate + ";Transform:pos:" + pos + ":rot:" + rot;
    }
}
