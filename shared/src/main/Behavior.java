package main;

/**
 *
 * @author Lime
 */
public interface Behavior {
    
    public abstract void start(Gameobject go);
    
    public abstract void update(Gameobject go);
    
    public abstract void render(Gameobject go);
}
