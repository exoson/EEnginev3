package client.game;

import common.game.TransformRoot;
import common.game.Behavior;
import common.game.Gameobject;
import common.game.Vector3f;

/**
 *
 * @author Lime
 */
public class Transform extends TransformRoot {


    @Override
    public void update(Gameobject go) {
        position = new Vector3f((String)go.getState("Transformpos"));
        rotation = new Vector3f((String)go.getState("Transformrot"));
        size = new Vector3f((String)go.getState("Transformsize"));
    }

    @Override
    public void render(Gameobject go) {

    }
}
