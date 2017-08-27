package behaviors;

import main.Behavior;
import main.Gameobject;
import math.Vector3f;

/**
 *
 * @author Lime
 */
public abstract class TransformRoot implements Behavior {

    protected Vector3f position, rotation;
    
    @Override
    public void start(Gameobject go) {
        position = new Vector3f((String)go.getState("Transformpos"));
        rotation = new Vector3f((String)go.getState("Transformrot"));
    }
    
    @Override
    public void render(Gameobject go) {
        
    }
    
    /**
     * @return the position
     */
    public Vector3f getPosition() {
        return position;
    }

    /**
     * @return the rotation
     */
    public Vector3f getRotation() {
        return rotation;
    }
}
