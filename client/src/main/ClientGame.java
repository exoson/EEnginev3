package main;

import Main.Client;
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
            new Thread(client).start();
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
