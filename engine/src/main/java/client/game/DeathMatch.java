package client.game;

import common.game.GameMode;
import common.game.Vector4f;
import client.graphics.Animation;

/**
 *
 * @author Lime
 */
public class DeathMatch implements GameMode {

    private class PlayerInfo {
        public int points, elo;
        public String name = "";
    }

    private UiObject uiRoot;
    private Map map;
    private PlayerInfo player0, player1;

    @Override
    public void start() {
        uiRoot = new UiObject();
        Main.getGame().setUi(uiRoot);
        map = new Map("");
        player0 = new PlayerInfo();
        player1 = new PlayerInfo();
        uiRoot.addChild(new Text("", 20, 10, new Vector4f(1, 1, 1, 1)) {
            @Override
            public void updateText() {
                player0.name = (String)Main.getGame().getFlag("player0");
                String points0 = (String)Main.getGame().getFlag("player0points");
                if (points0 != null) {
                    player0.points = Integer.parseInt(points0);
                }
                str = player0.name + ": " + player0.points;
            }
        });
        uiRoot.addChild(new Text("", 700, 10, new Vector4f(1, 1, 1, 1)) {
            @Override
            public void updateText() {
                player1.name = (String)Main.getGame().getFlag("player1");
                String points1 = (String)Main.getGame().getFlag("player1points");
                if (points1 != null) {
                    player1.points = Integer.parseInt(points1);
                }
                str = player1.name + ": " + player1.points;
            }
        });
        Main.getGame().setFlag("powerUpIcon", "");
        uiRoot.addChild(new Panel(400, 25) {
            String lastAnimation = "";
            @Override
            public void updateAnimation() {
                String curAnimation = (String)Main.getGame().getFlag("powerUpIcon");
                if(!lastAnimation.equals(curAnimation)) {
                    lastAnimation = curAnimation;
                    String[] split = curAnimation.split(",");
                    System.out.println(lastAnimation);
                    animation = new Animation(split[0], split[1]);
                }
            }
        });
    }

    @Override
    public boolean update() {
        if("1".equals((String)Main.getGame().getFlag("res"))) {
            Main.getGame().setFlag("res", "0");
            Main.getGame().setFlag("powerUpIcon", "powerupempty,default");
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
