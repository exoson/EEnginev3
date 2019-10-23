package client.game;

import common.game.Vector3f;
import common.game.Vector4f;
import client.graphics.Animation;


public class Panel extends UiObject {

    private Vector3f pos;
    protected Animation animation;

    public Panel(int x, int y) {
        super();
        pos = new Vector3f(x, y, 0);
    }

    @Override
    public void render() {
        super.render();
        if (animation != null) {
            animation.render(pos, new Vector3f());
        }
    }

    @Override
    public void update() {
        super.update();
        updateAnimation();
    }

    public void updateAnimation() {}
}


