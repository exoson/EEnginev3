package client.graphics;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import common.game.Vector2f;
import common.game.Vector3f;

public class Frame
{
    private final int length;
    private final Sprite spr;
    private int numDisplayed;

    public Frame(Sprite spr, int length) {
        this.spr = spr;
        this.length = length;
        numDisplayed = 0;
    }
    /**
     * Renders sprite and updates age of frame.
     * @param pos position to render the texture
     * @return True if frame has ended.
     */
    public boolean render(Vector3f pos) {
        return render(pos, new Vector3f(),new Vector2f(getSX(),getSY()),1,1,1,1);
    }
    /**
     * Renders the sprite with specified color and updates age of frame.
     * @param pos position to render the texture
     * @param rot amount to rotate the texture
     * @param size size to be rendered in
     * @param r the red value of the color
     * @param g the green value of the color
     * @param b the blue value of the color
     * @param a the alpha value of the color
     * @return True if frame has ended.
     */
    public boolean render(Vector3f pos, Vector3f rot,Vector2f size,float r, float g, float b, float a) {
        getSpr().render(pos, rot, size, r, g, b, a);
        numDisplayed++;

        if(numDisplayed >= length) {
            numDisplayed = 0;
            return true;
        }
        return false;
    }
    public float getSX() {
        return getSpr().getsx();
    }
    public float getSY() {
        return getSpr().getsy();
    }

    /**
     * @return the spr
     */
    public Sprite getSpr() {
        return spr;
    }

    public static ArrayList<Frame> fromFile(String fileName) {
        ArrayList<Frame> frames = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(Frame.class.getResourceAsStream("/animations/" + fileName + ".anim")));
            String line = br.readLine();
            String[] split = line.split(",");
            int sx = Integer.parseInt(split[0]), sy = Integer.parseInt(split[1]);
            while((line = br.readLine()) != null) {
                split = line.split(",");
                frames.add(new Frame(new Sprite(sx, sy, split[0]), Integer.parseInt(split[1])));
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Frame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Frame.class.getName()).log(Level.SEVERE, null, ex);
        }
        return frames;
    }
}
