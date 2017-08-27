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
    protected final ArrayList<String> addedObjects;
    protected int objId;
    
    public Game(GameMode gMode) {
        this.gMode = gMode;
        this.objId = 0;
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
        
        for(String goSpec : addedObjects) {
            initObject(goSpec);
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
    
    public int addObject(String goSpec) {
        addedObjects.add(goSpec);
        return objId++;
    }
    
    public Gameobject getObject(int id) {
        if(id < gObjects.size() && id >= 0) 
            return gObjects.get(id);
        return null;
    }
    
    public int getIdOf(Gameobject go) {
        return gObjects.indexOf(go);
    }
    
    protected void initObject(String goSpec) {
        gObjects.add(Gameobject.fromString(goSpec));
    }
    
    public void removeObject(Gameobject go) {
        removedObjects.add(go);
    }
    
    public Object getFlag(String flagName) {
        return state.get(flagName);
    }
    
    public void setFlag(String flagName, Object value) {
        state.put(flagName, value);
    }
    
    public void appendFlag(String flagName, String value) {
        setFlag(flagName, getFlag(flagName) + ";" + value);
    }
}