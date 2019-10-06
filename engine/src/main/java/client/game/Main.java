package client.game;

import client.graphics.Window;
import client.game.Input;

/**
 *
 * @author Lime
 */
public class Main {

    private static ClientGame game;

    public static void main(String[] args) {
        init();
        game = new ClientGame(new DeathMatch(), args[0]);
        game.run();
        cleanUp();
    }

    private static void init() {
        Window.init();
        Input.init();
    }

    public static void cleanUp() {
        Input.cleanUp();
        Window.cleanUp();
    }

    /**
     * @return the game
     */
    public static ClientGame getGame() {
        return game;
    }
}
