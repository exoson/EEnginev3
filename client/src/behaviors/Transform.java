package behaviors;

import main.Behavior;
import main.Gameobject;
import math.Vector3f;

/**
 *
 * @author Lime
 */
public class Transform extends TransformRoot {
    
    
    @Override
    public void update(Gameobject go) {
        position = new Vector3f((String)go.getState("Transformpos"));
        rotation = new Vector3f((String)go.getState("Transformrot"));
    }
    
    @Override
    public void render(Gameobject go) {
        
    }
}
