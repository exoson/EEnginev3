package server.game;

/**
 *
 * @author Lime
 */
public class Main {

    private static ServerGame game;

    public static void main(String args[]) {
        game = new ServerGame(new DeathMatch(), new DeathMatchClientHandler(), args);
        game.run();
    }

    /**
     * @return the game
     */
    public static ServerGame getGame() {
        return game;
    }
}
