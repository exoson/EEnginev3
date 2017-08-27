package main;

import main.Client;
import graphics.Camera;
import graphics.Window;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import math.Matrix4f;
import math.Vector2f;
import math.Vector3f;

/**
 *
 * @author Lime
 */
public class ClientGame extends Game {
    
    private final Camera camera;
    
    private Client client;

    public ClientGame(GameMode gMode) {
        super(gMode);
        camera = new Camera(Matrix4f.identity());
        try {
            client = new Client();
            Thread cThread = new Thread(client);
            cThread.setDaemon(true);
            cThread.start();
        } catch (IOException ex) {
            Logger.getLogger(ClientGame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void update() {
        Input.update();
        client.sendMsg(Input.getKeys());
        super.update();
    }
    
    @Override
    public void render() {
        Window.clear();
        camera.enable();
        
        gMode.render();
        for(Gameobject go : gObjects) {
            go.render();
        }
        
        camera.disable();
        Window.update();
    }
    
    @Override
    protected void stop() {
        client.stop();
    }

    public void updateStates(String updates) {
        //System.out.println(updates);
        for(String up : updates.split(";")) {
            String[] split = up.split(":");
            if(split.length == 1) {
                continue;
            }
            try {
                int objId = Integer.parseInt(split[0]);
                getObject(objId).setState(split[1], split[2]);
            } catch (Exception exc) {
                setFlag(split[0], split[1]);
            }
        }
    }
    
    /**
     * @return the camera
     */
    public Camera getCamera() {
        return camera;
    }
    
    public void moveView(Vector2f vec) {
        getCamera().move(vec);
    }
    
    public void moveView(Vector3f vec) {
        getCamera().move(vec);
    }
}
