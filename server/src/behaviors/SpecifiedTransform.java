package behaviors;

import main.Gameobject;
import main.Main;

/**
 *
 * @author Lime
 */
public class SpecifiedTransform extends Transform {
    
    @Override
    public void update(Gameobject go) {
        Main.getGame().updateClients(go.getState("id") + ":pos:" + position.toString());
        //System.out.println(position);
    }
}
