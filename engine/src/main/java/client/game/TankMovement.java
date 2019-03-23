package client.game;

import common.game.Behavior;
import common.game.Gameobject;

/**
 *
 * @author Lime
 */
public class TankMovement implements Behavior{

    private Sound hitSound;

    @Override
    public void start(Gameobject go) {
        hitSound = new Sound("explosion");
    }

    @Override
    public void update(Gameobject go) {
        if(Boolean.parseBoolean((String)go.getState("hit"))) {
            Animator anim = go.getBehavior("Animator");
            anim.setAnimation("tankHit");
            hitSound.playClip();
        }
    }

    @Override
    public void render(Gameobject go) {

    }

}
