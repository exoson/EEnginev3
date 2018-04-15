package behaviors;

import java.util.ArrayList;
import main.Behavior;
import main.Gameobject;
import main.Main;
import main.Physics;

/**
 *
 * @author Lime
 */
public class AmmoPowerUpBehavior implements Behavior{

    private String pUpName;
    
    @Override
    public void start(Gameobject go) {
        pUpName = "shotgunAmmo";
    }

    @Override
    public void update(Gameobject go) {
        checkHits(go);
    }
    
    private void checkHits(Gameobject go) {
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
           coll.setState("AmmoTemplate", pUpName);
        }
        if(!colls.isEmpty()) {
            Main.getGame().removeObject((int)go.getState("id"));
        }
    }
    @Override
    public void render(Gameobject go) {

    }
    
}
