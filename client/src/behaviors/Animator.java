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
public class Animator implements Behavior {

    private HashMap<String, Animation> anims;
    private String curAnim;
    
    @Override
    public void start(Gameobject go) {
        anims = new HashMap<>();
        String animList = (String)go.getState("Animatoranims");
        String[] splitted = animList.split(",");
        for(int i = 0; i < splitted.length; i+=2) {
            addAnimation(new Animation(splitted[i], splitted[i+1]));
        }
        curAnim = splitted[1];
    }

    @Override
    public void update(Gameobject go) {
        
    }

    @Override
    public void render(Gameobject go) {
        TransformRoot tf = (TransformRoot)go.getBehavior("Transform");
        Animation animation = anims.get(curAnim);
        if(animation != null) {
            animation.render(tf.getPosition(), tf.getRotation());
        }
    }
    public void addAnimation(Animation anim) {
        anims.put(anim.getName(), anim);
    }
    public void setAnimation(String anim) {
        curAnim = anim;
    }
}
