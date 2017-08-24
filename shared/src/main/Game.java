package main;

import behaviors.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Lime
 */
public class Game implements Runnable {
    
    private final HashMap<String, Object> state;
    protected GameMode gMode;
    protected final ArrayList<Gameobject> gObjects;
    protected final ArrayList<Gameobject> removedObjects;
    protected final ArrayList<Gameobject> addedObjects;
    
    public Game(GameMode gMode) {
        this.gMode = gMode;
        gObjects = new ArrayList<>();
        addedObjects = new ArrayList<>();
        removedObjects = new ArrayList<>();
        state = new HashMap<>();
    }
    
    public void update() {
        for(Gameobject go : gObjects) {
            go.update();
            if((boolean)go.getState("remove")) {
                removedObjects.add(go);
            }
        }
        
        for(Gameobject go : addedObjects) {
            gObjects.add(go);
        }
        addedObjects.removeAll(addedObjects);
        
        for(Gameobject go : removedObjects) {
            gObjects.remove(go);
        }
        removedObjects.removeAll(removedObjects);
        
        if(gMode.update()) {
            gMode.reset();
        }
    }
    
    public void render() {
        
    }
    
    @Override
    public void run() {
        gMode.start();
        setFlag("running", true);
        while((boolean)getFlag("running")) {
            update();
            render();
        }
    }
    
    public void addObject(Gameobject go) {
        addedObjects.add(go);
    }
    
    public void removeObject(Gameobject go) {
        removedObjects.add(go);
    }
    
    public Object getFlag(Object flagName) {
        return state.get(flagName);
    }
    
    public void setFlag(String flagName, Object value) {
        state.put(flagName, value);
    }
}
