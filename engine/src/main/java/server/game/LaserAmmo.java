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
    private static final int MOVESPEED = 10,LENGTH = 2500;
    private final ArrayList<Vector2f> points = new ArrayList<>();

    @Override
    public void start(Gameobject go) {
        go.setState("AmmoBehaviorlifetime", LENGTH/MOVESPEED)
        super.start(go);
    }
    @Override
    public void update(Gameobject go) {
        super.update(go);
        String pointString = "";
        for(Vector2f point : points) {
            pointString += "" + point.getX() + "," + point.getY() + ";";
        }
        Main.getGame().updateClients(go.getState("id") + ":SniperAmmopoints:" + pointString);
    }
    @Override
    public void move(Gameobject go) {
        Transform tf = (Transform)go.getBehavior("Transform");
        Vector2f v = new Vector2f((float)Math.cos(tf.getRotation().getZ() + Math.PI/2), (float)Math.sin(tf.getRotation().getZ() + (float)Math.PI/2)).mult(-speed);
        tf.move(v);
        if(isSolid && ((DeathMatch)Main.getGame().getgMode()).getMap().checkCollisionsAll(this)) {
        for(int i = 0; i < MOVESPEED; i++) {
            tf.move(go);
            super.checkHits(go);
            points.add(new Vector2f(go.getX(),go.getY()));
        }
        while(points.size() > MOVESPEED*3) {
            points.remove(0);
        }
    }
}



package Game;

import Graphics.Renderer;
import Main.Gameobject;
import Main.Vector2f;
import Main.Vector4f;
import java.util.ArrayList;

/**
 *
 * @author emil
 */
public class LaserAmmo extends AmmoBase
{
    private static final int MOVESPEED = 10,LENGTH = 2500;
    private final ArrayList<Vector2f> points = new ArrayList<>();

    @Override
    public void start(Gameobject go) {
        super.start(go);
        setDelay(LENGTH/MOVESPEED);
    }
    @Override
    protected void move(Gameobject go) {
        for(int i = 0; i < MOVESPEED; i++) {
            super.move(go);
            super.checkHits(go);
            points.add(new Vector2f(go.getX(),go.getY()));
        }
        while(points.size() > MOVESPEED*3) {
            points.remove(0);
        }
    }
    @Override
    public void render(Gameobject go) {
        Renderer.renderLines(points, 5, new Vector4f(1f,1,1f,1));
    }
}
