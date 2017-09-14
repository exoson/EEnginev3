package behaviors;

import main.Delay;
import main.Gameobject;
import main.Input;
import main.Sound;

/**
 *
 * @author Lime
 */
public class CannonBehavior extends CannonBehaviorRoot {
    
    @Override
    public void start(Gameobject go) {
        super.start(go);
        this.shootingKey = Input.KEY_SPACE;
    }

    @Override
    public void update(Gameobject go) {
        if(reloadDel.over() && Input.getKey(shootingKey)) {
            new Sound("shoot").playClip();
            reloadDel.start();
        }
    }
}
