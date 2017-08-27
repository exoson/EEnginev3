package behaviors;

import main.Behavior;
import main.Gameobject;
import main.Main;
import math.Vector3f;

/**
 *
 * @author Lime
 */
public class Transform extends TransformRoot {
    
    
    @Override
    public void update(Gameobject go) {
        Main.getGame().updateClients(go.getState("id") + ":pos:" + position.toString());
    }
    
    @Override
    public void render(Gameobject go) {
        
    }
    
    public void move(Vector3f vec) {
        position = position.add(vec);
    }
    
    public void rotate(Vector3f vec) {
        rotation = rotation.add(vec);
    }
}
