package main;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        state.put("remove", false);
        
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
    
    public void setState(String flagName, Object value) {
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
        
        parseStateBehv(behaviors, state, str);
        
        state.put("init", str);
        return new Gameobject(behaviors, state);
    }
    
    private static void parseStateBehv(ArrayList<Behavior> behaviors, HashMap<String, Object> state, String spec) {
        String[] splitted = spec.split(";");
        for(String s : splitted) {
            if(s.isEmpty()) {
                continue;
            }
            if(s.equals("in")) {
                continue;
            }
            if(s.startsWith("id:")) {
                state.put("id", Integer.parseInt(s.substring(3)));
                continue;
            }
            if(s.startsWith("client:")) {
                state.put("client", s.substring(7));
                continue;
            }
            if(s.startsWith("file:")) {
                parseStateBehv(behaviors, state, fromFile(s.substring(5)));
                continue;
            }
            String[] subsplit = s.split(":");
            try {
                String className = subsplit[0];
                String fullName = "behaviors." + className;
                boolean duplicate = false;
                for(Behavior b : behaviors) {
                    if(b.getClass().equals(Class.forName(fullName))) {
                        duplicate = true;
                        break;
                    }
                }
                if(!duplicate) {
                    Behavior b = (Behavior)Class.forName(fullName).newInstance();
                    behaviors.add(b);
                }
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
    }
    
    private static String fromFile(String fileName) {
        String fullDef = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader("../res/objects/" + fileName + ".obj"));
            String line;
            while((line = br.readLine()) != null) {
                fullDef += line;
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Gameobject.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Gameobject.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fullDef;
    }
}