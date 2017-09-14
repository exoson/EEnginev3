package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 *
 * @author Lime
 */
public class Game implements Runnable {
    
    private final HashMap<String, Object> state;
    private final GameMode gMode;
    protected final ArrayList<Gameobject> gObjects;
    protected final ArrayList<Gameobject> removedObjects;
    protected final ArrayList<String> addedObjects;
    protected int objId;
    
    private final ReentrantReadWriteLock addLock, rmLock;
    
    public Game(GameMode gMode) {
        this.gMode = gMode;
        this.objId = 0;
        gObjects = new ArrayList<>();
        addedObjects = new ArrayList<>();
        removedObjects = new ArrayList<>();
        state = new HashMap<>();
        addLock = new ReentrantReadWriteLock();
        rmLock = new ReentrantReadWriteLock();
    }
    
    public void update() {
        for(Gameobject go : gObjects) {
            go.update();
            if((boolean)go.getState("remove")) {
                removedObjects.add(go);
            }
        }
        
        addLock.readLock().lock();
        try {
            for(String goSpec : addedObjects) {
                initObject(goSpec);
            }
        } finally {
            addLock.readLock().unlock();
        }
        addLock.writeLock().lock();
        try {
            addedObjects.removeAll(addedObjects);
        } finally {
            addLock.writeLock().unlock();
        }
        
        rmLock.readLock().lock();
        try {
            for(Gameobject go : removedObjects) {
                gObjects.remove(go);
            }
        } finally {
            rmLock.readLock().unlock();
        }
        rmLock.writeLock().lock();
        try {
            removedObjects.removeAll(removedObjects);
        } finally {
            rmLock.writeLock().unlock();
        }
        
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
        addLock.writeLock().lock();
        try {
            addedObjects.add(goSpec);
        } finally {
            addLock.writeLock().unlock();
        }
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
        addLock.writeLock().lock();
        try {
            gObjects.add(Gameobject.fromString(goSpec));
        } finally {
            addLock.writeLock().unlock();
        }
    }
    
    public void removeObject(Gameobject go) {
        rmLock.writeLock().lock();
        try {
            removedObjects.add(go);
        } finally {
            rmLock.writeLock().unlock();
        }
    }
    
    public void removeObject(int id) {
        rmLock.writeLock().lock();
        try {
            for(Gameobject go : gObjects) {
                if((int)go.getState("id") == id) {
                    System.out.println("Destroy:" + id);
                    removedObjects.add(go);
                    break;
                }
            }
        } finally {
            rmLock.writeLock().unlock();
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