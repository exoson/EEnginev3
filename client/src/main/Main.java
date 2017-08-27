package main;

import graphics.Window;

/**
 *
 * @author Lime
 */
public class Main {

    private static ClientGame game;
    
    public static void main(String[] args) {
        init();
        game = new ClientGame(new GameMode() {

            @Override
            public void start() {
                
            }

            @Override
            public boolean update() {
                return false;
            }

            @Override
            public void render() {
            }

            @Override
            public void reset() {
                
            }
        });
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
