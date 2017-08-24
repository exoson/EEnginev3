package main;

/**
 *
 * @author Lime
 */
public class Main {
    
    private static ServerGame game;
    
    public static void main(String args[]) {
        game = new ServerGame(new GameMode() {

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
        }, new ClientHandler() {

            @Override
            public boolean init(ClientServer cs) {
                return false;
            }

            @Override
            public boolean update(ClientServer cs) {
                String input = (String)game.getFlag(cs.toString() + "-input");
                int KEY_0 = 0x30;
                
                if(input != null) {
                    System.out.println(input.charAt(KEY_0));
                }
                return false;
            }

            @Override
            public void quit(ClientServer cs) {
                
            }
        });
        game.run();
    }

    /**
     * @return the game
     */
    public static ServerGame getGame() {
        return game;
    }
}
