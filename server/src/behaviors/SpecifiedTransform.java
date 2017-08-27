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
        Main.getGame().updateClients(Main.getGame().getIdOf(go) + ":pos:" + position.toString());
        //System.out.println(position);
    }
}
