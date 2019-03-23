package client.graphics;

import common.game.Vector4f;
import java.awt.Font;


public class Textrenderer
{
    private TrueTypeFont font;

    public Textrenderer() {
        this("Times New Roman");
    }

    public Textrenderer(String fontName) {
        font = new TrueTypeFont(new Font(fontName,0,25), false);
//        font = new TrueTypeFont("chars","/textures/charLocations.loc");
    }
    /**
     * Renders a string with prespecified font.
     * @param s String to be rendered
     * @param x x coordinate for where to render the text.
     * @param y y coordinate for where to render the text.
     * @param color
     */
    public void drawString(String s,float x,float y,Vector4f color) {
        font.drawString(x, y, s, color);
    }
}
