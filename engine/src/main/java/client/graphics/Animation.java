package client.graphics;

import common.game.Vector2f;
import common.game.Vector3f;
import java.util.ArrayList;

public class Animation
{
    private final ArrayList<Frame> frames;
    private int curframe;
    private final String name;

    public Animation(String fileName, String name) {
        this.frames = Frame.fromFile(fileName);
        this.name = name;
    }

    public Animation(ArrayList<Frame> frames, String name) {
        this.name = name;
        this.frames = frames;
        curframe = 0;
    }
    /**
     * Renders current frame and updates animation
     * @param pos position to render the texture
     * @return True if animation has ended.
     */
    public boolean render(Vector3f pos) {
        return render(pos, new Vector3f(), new Vector2f(getFrames().get(curframe).getSX(), getFrames().get(curframe).getSY()),1,1,1,1);
    }
    /**
     * Renders current frame and updates animation
     * @param pos position to render the texture
     * @return True if animation has ended.
     */
    public boolean render(Vector3f pos, Vector3f rot) {
        return render(pos, rot, new Vector2f(getFrames().get(curframe).getSX(), getFrames().get(curframe).getSY()),1,1,1,1);
    }
    /**
     * Renders current frame and updates animation with the specified color
     * @param pos position to render the texture
     * @param rot amount to rotate the texture
     * @param size size to be rendered in
     * @param r red value of the color
     * @param g green value of the color
     * @param b blue value of the color
     * @param a alpha value of the color
     * @return True if animation has ended.
     */
    public boolean render(Vector3f pos, Vector3f rot, Vector2f size, float r, float g, float b, float a)
    {
        Frame temp = getFrames().get(curframe);

        if(temp.render(pos, rot, size, r, g, b, a))
        {
            curframe++;
            curframe %= getFrames().size();
            if(curframe == 0) return true; else return false;
        }
        return false;
    }

    /**
     * @return the frames
     */
    public ArrayList<Frame> getFrames() {
        return frames;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
}
