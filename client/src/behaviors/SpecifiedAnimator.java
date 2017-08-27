package behaviors;

import graphics.Animation;
import graphics.Frame;
import graphics.Sprite;
import java.util.ArrayList;
import java.util.HashMap;
import main.Behavior;
import main.Gameobject;

/**
 *
 * @author Lime
 */
public class SpecifiedAnimator implements Behavior {

    private HashMap<String, Animation> anims;
    private String curAnim;
    
    @Override
    public void start(Gameobject go) {
        anims = new HashMap<>();
        ArrayList<Frame> frames = new ArrayList<>();
        frames.add(new Frame(new Sprite(64, 64), 100));
        Animation anim = new Animation(frames, "default");
        addAnimation(anim);
        curAnim = "default";
    }

    @Override
    public void update(Gameobject go) {
        
    }

    @Override
    public void render(Gameobject go) {
        Transform tf = (Transform)go.getBehavior("SpecifiedTransform");
        Animation animation = anims.get(curAnim);
        if(animation != null) {
            animation.render(tf.getPosition());
        }
    }
    public void addAnimation(Animation anim) {
        anims.put(anim.getName(), anim);
    }
    public void setAnimation(String anim) {
        curAnim = anim;
    }
}
