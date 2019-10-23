package client.game;

import common.game.Vector3f;
import common.game.Vector4f;
import client.graphics.Animation;


public class Panel extends UiObject {

    private int x, y;
    private Vector3f pos;
    private Animation animation;

    public Panel(int x, int y, Animation animation) {
        super();
        pos = new Vector3f(x, y, 0);
        this.animation = animation;
    }

    @Override
    public void render() {
        super.render();
        animation.render(pos, new Vector3f());
    }
}


