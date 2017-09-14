package behaviors;

import main.Behavior;
import main.Delay;
import main.Gameobject;

/**
 *
 * @author Lime
 */
public class CannonBehaviorRoot implements Behavior{

    protected Delay reloadDel;
    protected int shootingKey;
    
    @Override
    public void start(Gameobject go) {
        reloadDel = new Delay(1000);
        reloadDel.end();
    }

    @Override
    public void update(Gameobject go) {
        
    }

    @Override
    public void render(Gameobject go) {
        
    }
    
}
