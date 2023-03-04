package server.game;

import common.game.TransformRoot;
import common.game.Gameobject;
import common.game.Vector2f;
import common.game.Vector2i;
import common.game.Vector3f;

/**
 *
 * @author Lime
 */
public class Transform extends TransformRoot {


    @Override
    public void update(Gameobject go) {
        Main.getGame().updateClients(go.getState("id") + ":Transformpos:" + position.toString());
        Main.getGame().updateClients(go.getState("id") + ":Transformrot:" + rotation.toString());
        Main.getGame().updateClients(go.getState("id") + ":Transformsize:" + size.toString());
    }

    @Override
    public void render(Gameobject go) {

    }

    public void setPosition(Vector3f vec) {
        position = vec;
    }

    public boolean move(Vector2i vec) {
        return move(new Vector3f(vec));
    }

    public boolean move(Vector2f vec) {
        return move(new Vector3f(vec));
    }

    public boolean move(Vector3f vec) {
        position = position.add(vec);
        if(isSolid && ((DeathMatch)Main.getGame().getgMode()).getMap().checkCollisionsAll(this)) {
            position = position.minus(vec);
            return false;
        }
        return true;
    }

    public void rotate(Vector3f vec) {
        rotation = rotation.add(vec);
        rotation = rotation.reminder(2 * (float)Math.PI);
    }

    public void rotate(float amt) {
        rotation = rotation.add(new Vector3f(0, 0, amt));
        rotation = rotation.reminder(2 * (float)Math.PI);
    }

    public void setRotation(Vector3f rotation) {
        this.rotation = rotation;
    }

    public Vector3f forward() {
        return new Vector3f((float)Math.cos(rotation.getZ()+Math.PI/2),(float)Math.sin(rotation.getZ()+Math.PI/2), 0);
    }
}
