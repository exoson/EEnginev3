package client.game;

import common.game.GameMode;
import common.game.Vector4f;
import client.graphics.Textrenderer;

/**
 *
 * @author Lime
 */
public class DeathMatch implements GameMode {

    private class PlayerInfo {
        public int points, elo;
        public String name = "";
    }

    private Map map;
    private PlayerInfo player0, player1;
    private Textrenderer textRenderer;

    @Override
    public void start() {
        map = new Map("");
        textRenderer = new Textrenderer();
        player0 = new PlayerInfo();
        player1 = new PlayerInfo();
    }

    @Override
    public boolean update() {
        if("1".equals((String)Main.getGame().getFlag("res"))) {
            Main.getGame().setFlag("res", "0");
            return true;
        }
        player0.name = (String)Main.getGame().getFlag("player0");
        player1.name = (String)Main.getGame().getFlag("player1");
        String points0 = (String)Main.getGame().getFlag("player0points");
        if (points0 != null) {
            player0.points = Integer.parseInt(points0);
        }
        String points1 = (String)Main.getGame().getFlag("player1points");
        if (points1 != null) {
            player1.points = Integer.parseInt(points1);
        }
        return false;
    }

    @Override
    public void render() {
        map.render();
        textRenderer.drawString(player0.name + ": " + player0.points, 10, 10, new Vector4f(1, 0, 0, 1));
        textRenderer.drawString(player1.name + ": " + player1.points, 10, 100, new Vector4f(1, 0, 0, 1));
    }

    @Override
    public void reset() {
        String sqrs = (String)Main.getGame().getFlag("map");
        map = new Map(sqrs);
    }

}
