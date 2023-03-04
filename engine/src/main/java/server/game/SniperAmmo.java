package server.game;

import common.game.Gameobject;
import common.game.Vector2f;
import common.game.Vector3f;

/**
 *
 * @author emil
 */
public class SniperAmmo extends AmmoBehavior
{
    private static final float SPREAD = (float)Math.PI/3;
    private static final int AMMOAMT = 5;

    @Override
    public void start(Gameobject go) {
        super.start(go);
        Transform tf = (Transform)go.getBehavior("Transform");
        tf.setIsSolid(false);
    }
    @Override
    public void move(Gameobject go) {
        Transform tf = (Transform)go.getBehavior("Transform");
        Vector2f v = new Vector2f((float)Math.cos(tf.getRotation().getZ() + Math.PI/2), (float)Math.sin(tf.getRotation().getZ() + (float)Math.PI/2)).mult(-speed);
        /*wasColliding = isColliding;
        isColliding = go.checkCollisions();
        if(wasColliding && !isColliding) {
            go.setIsSolid(true);
        }*/
        tf.move(v);
    }
}

