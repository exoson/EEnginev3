package client.game;

import client.graphics.Sprite;
import common.game.Vector3f;

public enum Square
{
    grass(new Sprite(Map.SQRSIZE, Map.SQRSIZE,"grass")),
    stone(new Sprite(Map.SQRSIZE, Map.SQRSIZE,"stone")),
    road(new Sprite(Map.SQRSIZE, Map.SQRSIZE,"road"));

    private final Sprite spr;

    private Square(Sprite spr) {
        this.spr = spr;
    }
    public void render(Vector3f pos) {
        spr.render(pos);
    }
//    public void render(Vector2f pos, float r, float g, float b) {
//        spr.render(new Vector3f(pos),0,r,g,b,1);
//    }
}
