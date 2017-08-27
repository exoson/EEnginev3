package behaviors;

import main.Behavior;
import main.Gameobject;
import main.Input;
import math.Vector2f;

/**
 *
 * @author Lime
 */
public class TankMovement implements Behavior {
    
    private static final int FORWARD = 0, 
            RIGHT = 1, 
            BACKWARD = 2, 
            LEFT = 3;
    private final int[] keys = new int[4];
    
    private String cName;
    
    private float speed, rotSpeed;
    
    @Override
    public void start(Gameobject go) {
        this.speed = Float.parseFloat((String)go.getState("TankMovementspeed"));
        this.rotSpeed = Float.parseFloat((String)go.getState("TankMovementrotSpeed"));
        this.cName = (String)go.getState("client");
        setBackward(Input.KEY_S);
        setForward(Input.KEY_W);
        setRight(Input.KEY_D);
        setLeft(Input.KEY_A);
    }
    @Override
    public void render(Gameobject go) {
        
    }
    @Override
    public void update(Gameobject go) {
        Transform tf = (Transform)go.getBehavior("Transform");
        Vector2f v = new Vector2f((float)Math.cos(tf.getRotation().getZ()+Math.PI/2),(float)Math.sin(tf.getRotation().getZ()+Math.PI/2)).mult(speed);
        if(Input.getKey(cName, keys[FORWARD])) {
            tf.move(v.mult(-1));
        }else if(Input.getKey(cName, keys[BACKWARD])) {
            tf.move(v);
        }
        float rot = (Input.getKey(cName, keys[LEFT]) ? -rotSpeed : 0) + (Input.getKey(cName, keys[RIGHT]) ? rotSpeed : 0);
        tf.rotate(rot);
    }
    public void setForward(int key){
        keys[FORWARD] = key;
    }
    public void setBackward(int key){
        keys[BACKWARD] = key;
    }
    public void setLeft(int key){
        keys[LEFT] = key;
    }
    public void setRight(int key){
        keys[RIGHT] = key;
    }

    /**
     * @return the speed
     */
    public float getSpeed() {
        return speed;
    }

    /**
     * @param speed the speed to set
     */
    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
