package client.game;

import common.game.Vector4f;
import client.graphics.Textrenderer;


public class Text extends UiObject {

    protected String str;
    private int x, y;
    private Vector4f color;
    private Textrenderer renderer;

    public Text(String str, int x, int y, Vector4f color) {
        this.str = str;
        this.x = x;
        this.y = y;
        this.color = color;
        renderer = new Textrenderer();
    }

    @Override
    public void render() {
        super.render();
        renderer.drawString(str, x, y, color);
    }

    @Override
    public void update() {
        super.update();
        updateText();
    }

    public void updateText() {}
}

