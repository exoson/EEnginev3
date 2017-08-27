package behaviors;

import main.Behavior;
import main.Gameobject;
import math.Vector3f;

/**
 *
 * @author Lime
 */
public abstract class Transform implements Behavior {

    protected Vector3f position, rotation;
    
    @Override
    public void start(Gameobject go) {
        position = new Vector3f((String)go.getState("pos"));
        rotation = new Vector3f();
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
