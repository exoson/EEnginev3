package main;

/**
 *
 * @author Lime
 */
public interface GameMode {
    
    public abstract void start();
    
    public abstract boolean update();
    
    public abstract void render();
    
    public abstract void reset();
}
