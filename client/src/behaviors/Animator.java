package behaviors;

import graphics.Animation;
import java.util.ArrayList;
import main.Behavior;
import main.Gameobject;

/**
 *
 * @author Lime
 */
public class Animator implements Behavior {

    private ArrayList<Animation> anims;
    private int curAnim;
    
    @Override
    public void start(Gameobject go) {
        anims = new ArrayList<>();
        curAnim = 0;
    }

    @Override
    public void update(Gameobject go) {
        
    }

    @Override
    public void render(Gameobject go) {
        Transform tf = (Transform)go.getBehavior("Transform");
        anims.get(curAnim).render(tf.getPosition());
    }
    public void addAnimation(Animation anim) {
        anims.add(anim);
    }
    public void setAnimation(int anim) {
        curAnim = anim;
    }
}
