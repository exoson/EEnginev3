package main;

import behaviors.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import math.Vector3f;

/**
 *
 * @author Lime
 */
public class Gameobject {

    private final HashMap<String, Object> state;
    
    protected final ArrayList<Behavior> behaviors;
    private final ArrayList<Behavior> removedBehaviors;

    public Gameobject() {
        this(new ArrayList<>(), new HashMap<>());
    }
    
    public Gameobject(ArrayList<Behavior> behaviors, HashMap<String, Object> state) {
        this.state = state;
        removedBehaviors = new ArrayList<>();
        
        this.behaviors = behaviors;
        for(Behavior b : this.behaviors) {
            b.start(this);
        }
    }
    
    public void update() {
        for(Behavior b : behaviors) {
            b.update(this);
        }
        behaviors.removeAll(removedBehaviors);
        removedBehaviors.removeAll(removedBehaviors);
    }

    public void render() {
        for(Behavior b : behaviors) {
            b.render(this);
        }
    }
    
    public Object getState(String flagName) {
        return state.get(flagName);
    }
    
    public void setFlag(String flagName, Object value) {
        state.put(flagName, value);
    }
    
    /**
     * @param name the name of the Behavior to be found.
     * @return the Behavior if found.
     */
    public Behavior getBehavior(String name) {
        for(Behavior b : behaviors) {
            String[] split = b.getClass().getName().split("\\.");
            if(split[split.length == 0 ? 0 : split.length-1].equals(name)) {
                return b;
            }
        }
        return null;
    }
    public void removeBehavior(Behavior b) {
        removedBehaviors.add(b);
    }
    
    public static Gameobject fromString(String str) {
        ArrayList<Behavior> behaviors = new ArrayList<>();
        HashMap<String, Object> state = new HashMap<>();
        behaviors.add(new Transform());
        
        String[] splitted = str.split(":");
        for(String s : splitted) {
            if(s.equals("in")) {
                continue;
            }
            if(s.startsWith("pos,")) {
                state.put("pos", new Vector3f(s.substring(4)));
                continue;
            }
            String[] subsplit = s.split(",");
            try {
                String className = subsplit[0];
                Behavior b = (Behavior)Class.forName("behaviors." + className).newInstance();
                behaviors.add(b);
                for(int i = 1; i < subsplit.length; i+=2) {
                    state.put(className + subsplit[i], subsplit[i+1]);
                }
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InstantiationException ex) {
                Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return new Gameobject(behaviors, state);
    }
}