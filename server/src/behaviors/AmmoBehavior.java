package behaviors;

import java.util.ArrayList;
import main.Behavior;
import main.Delay;
import main.Gameobject;
import main.Main;
import main.Physics;
import math.Vector3f;

/**
 *
 * @author Lime
 */
public class AmmoBehavior implements Behavior {

    private float speed;
    private Delay deathDel;
    
    @Override
    public void start(Gameobject go) {
        this.speed = Float.parseFloat((String)go.getState("AmmoBehaviorspeed"));
        deathDel = new Delay(5000);
        deathDel.start();
    }

    @Override
    public void update(Gameobject go) {
        move(go);
        checkHits(go);
        if(deathDel.over()) {
            Main.getGame().removeObject((int)go.getState("id"));
        }
    }
    
    protected void move(Gameobject go) {
        Transform tf = (Transform)go.getBehavior("Transform");
        Vector3f v = tf.forward().mult(-speed);
        if(!tf.move(v)){
            tf.setRotation(tf.getRotation().mult(-1));
            v = tf.forward().mult(-speed);
            if(!tf.move(v)) {
                tf.rotate((float)Math.PI);
                v = tf.forward().mult(-speed);
                tf.move(v);
            }
        }
    }

    protected void checkHits(Gameobject go) {
        Transform tf = (Transform)go.getBehavior("Transform");
        ArrayList<Gameobject> gos = new ArrayList<>();
        for(String s : Main.getGame().getClientNames()) {
            int id = (int)Main.getGame().getFlag(s + "-player");
            Gameobject player = Main.getGame().getObject(id);
            if(player != null) {
                gos.add(player);
            }
        }
        ArrayList<Gameobject> colls = Physics.sphereCollide(tf, tf.getSX()/2, gos);
        for(Gameobject coll : colls) {
            coll.setState("hit", true);
            Main.getGame().updateClients(coll.getState("id") + ":hit:true");
            Transform t = coll.getBehavior("Transform");
            t.setRotation(tf.getRotation());
        }
        if(!colls.isEmpty()) {
            Main.getGame().removeObject((int)go.getState("id"));
        }
    }
    @Override
    public void render(Gameobject go) {
        
    }
    
}