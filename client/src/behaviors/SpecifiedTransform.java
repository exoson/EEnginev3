package behaviors;

import main.Gameobject;
import math.Vector3f;

/**
 *
 * @author Lime
 */
public class SpecifiedTransform extends Transform {
    
    @Override
    public void update(Gameobject go) {
        position = new Vector3f((String)go.getState("pos"));
    }
}
