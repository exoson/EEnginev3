package main;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Lime
 */
public class Game implements Runnable {
    
    private final HashMap<String, Object> state;
    private GameMode gMode;
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
        
        if(getgMode().update()) {
            getgMode().reset();
        }
    }
    
    public void render() {
        
    }
    
    @Override
    public void run() {
        Time.init();
        getgMode().start();
        setFlag("running", true);
        while((boolean)getFlag("running")) {
            Time.update();
            update();
            render();
        }
        stop();
    }
    
    public int addObject(String goSpec) {
        //System.out.println(goSpec);
        addedObjects.add(goSpec);
        return objId++;
    }
    
    public Gameobject getObject(int id) {
        for(Gameobject go : gObjects) {
            if((int)go.getState("id") == id) {
                return go;
            }
        }
        return null;
    }
    
    public ArrayList<Gameobject> getObjects() {
        return gObjects;
    }
    
    protected void initObject(String goSpec) {
        gObjects.add(Gameobject.fromString(goSpec));
    }
    
    public void removeObject(Gameobject go) {
        removedObjects.add(go);
    }
    
    public void removeObject(int id) {
        for(Gameobject go : gObjects) {
            if((int)go.getState("id") == id) {
                removedObjects.add(go);
                break;
            }
        }
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

    protected void stop() {
        
    }

    /**
     * @return the gMode
     */
    public GameMode getgMode() {
        return gMode;
    }
    
    public void removeAll() {
        for(Gameobject go : gObjects) {
            removeObject(go);
        }
    }
}