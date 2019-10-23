package client.game;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.AuthenticationException;

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
    private UiObject uiRoot;

    private Client client;

    public ClientGame(GameMode gMode, String[] args) {
        super(gMode, "client");
        camera = new Camera(Matrix4f.identity());
        camera.setPosition(new Vector3f(-Map.SQRSIZE/2,-70,0));
        try {
            client = new Client(args);
            Thread cThread = new Thread(client);
            cThread.setDaemon(true);
            cThread.start();
        } catch (IOException ex) {
            Logger.getLogger(ClientGame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AuthenticationException ex) {
            Logger.getLogger(ClientGame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void update() {
        Input.update();
        client.sendMsg(Input.getKeys());
        super.update();
        if (uiRoot != null) {
            uiRoot.update();
        }
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
        if (uiRoot != null) {
            uiRoot.render();
        }
        Window.update();
    }

    @Override
    protected void stop() {
        client.stop();
    }

    public void updateStates(String updates) {
        //System.out.println(updates);
        for(String up : updates.split(";")) {
            String[] split = up.split(":", 3);
            if(split.length == 1) {
                continue;
            }
            try {
                int objId = Integer.parseInt(split[0]);
                getObject(objId).setState(split[1], split[2]);
            } catch (Exception exc) {
                split = up.split(":", 2);
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

    public void setUi(UiObject ui) {
        uiRoot = ui;
    }
}
