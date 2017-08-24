
package graphics;

import math.Vector4f;
import java.awt.Font;


public class Textrenderer
{
    private static TrueTypeFont font;
    
    public static void init()
    {
        font = new TrueTypeFont(new Font("Times New Roman",0,25), false);
//        font = new TrueTypeFont("chars","res/textures/charLocations.loc");
    }
    /**
     * Renders a string with prespecified font.
     * @param s String to be rendered
     * @param x x coordinate for where to render the text.
     * @param y y coordinate for where to render the text.
     * @param color
     */
    public static void drawString(String s,float x,float y,Vector4f color)
    {
        font.drawString(x, y, s, color);
    }
}