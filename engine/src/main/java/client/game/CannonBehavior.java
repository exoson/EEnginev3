package client.game;

import common.game.Behavior;
import common.game.Gameobject;

/**
 *
 * @author Lime
 */
public class CannonBehavior implements Behavior{

    private Sound cannonSound;
    @Override
    public void start(Gameobject go) {
        go.setState("CannonPlay", "false");
        cannonSound = new Sound("shoot");
    }

    @Override
    public void update(Gameobject go) {
        if(Boolean.parseBoolean((String)go.getState("CannonPlay"))) {
            cannonSound.playClip();
            go.setState("CannonPlay", "false");
        }
    }

    @Override
    public void render(Gameobject go) {

    }
}
