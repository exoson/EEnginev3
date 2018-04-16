package behaviors;

import main.Behavior;
import main.Gameobject;
import math.Vector3f;

/**
 *
 * @author Lime
 */
public abstract class TransformRoot implements Behavior {

    protected Vector3f position, rotation, size;
    
    @Override
    public void start(Gameobject go) {
        position = new Vector3f((String)go.getState("Transformpos"));
        rotation = new Vector3f((String)go.getState("Transformrot"));
        size = new Vector3f((String)go.getState("Transformsize"));
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
    
    public float getSX() {
        return size.getX();
    }
    public float getSY() {
        return size.getY();
    }
    public float getSZ() {
        return size.getZ();
    }
    
    @Override
    public String toString() {
        return position.toString() + " " + rotation.toString() + " " + size.toString();
    }
}
