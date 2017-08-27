package behaviors;

import main.Gameobject;
import main.Main;
import math.Vector2f;
import math.Vector3f;

/**
 *
 * @author Lime
 */
public class Transform extends TransformRoot {
    
    
    @Override
    public void update(Gameobject go) {
        Main.getGame().updateClients(go.getState("id") + ":Transformpos:" + position.toString());
        Main.getGame().updateClients(go.getState("id") + ":Transformrot:" + rotation.toString());
        Main.getGame().updateClients(go.getState("id") + ":Transformsize:" + size.toString());
    }
    
    @Override
    public void render(Gameobject go) {
        
    }
    
    public void move(Vector2f vec) {
        position = position.add(vec);
    }
    
    public void move(Vector3f vec) {
        position = position.add(vec);
    }
    
    public void rotate(Vector3f vec) {
        rotation = rotation.add(vec);
    }
    
    public void rotate(float amt) {
        rotation = rotation.add(new Vector3f(0, 0, amt));
    }
}
