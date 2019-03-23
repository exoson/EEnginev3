package client.game;

import common.game.GameMode;

/**
 *
 * @author Lime
 */
public class DeathMatch implements GameMode {

    private Map map;

    @Override
    public void start() {
        map = new Map("");
    }

    @Override
    public boolean update() {
        if("1".equals((String)Main.getGame().getFlag("res"))) {
            Main.getGame().setFlag("res", "0");
            return true;
        }
        return false;
    }

    @Override
    public void render() {
        map.render();
    }

    @Override
    public void reset() {
        String sqrs = (String)Main.getGame().getFlag("map");
        map = new Map(sqrs);
    }

}
