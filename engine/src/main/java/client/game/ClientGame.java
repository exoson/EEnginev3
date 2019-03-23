package client.game;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import client.graphics.Camera;
import client.graphics.Window;
import common.game.Matrix4f;
import common.game.Vector2f;
import common.game.Vector3f;
import common.game.Game;
import common.game.Gameobject;
import common.game.GameMode;

/**
 *
 * @author Lime
 */
public class ClientGame extends Game {

    private final Camera camera;

    private Client client;

    public ClientGame(GameMode gMode) {
        super(gMode, "client");
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

        getgMode().render();
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