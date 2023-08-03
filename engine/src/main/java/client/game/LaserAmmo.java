package client.game;

import java.util.ArrayList;
import common.game.Vector2f;
import common.game.Vector4f;
import client.graphics.Renderer;
import common.game.Behavior;
import common.game.Gameobject;

/**
 *
 * @author Lime
 */
public class LaserAmmo implements Behavior{
    private final ArrayList<Vector2f> points = new ArrayList<>();

    @Override
    public void start(Gameobject go) {

    }

    @Override
    public void update(Gameobject go) {
        points.clear();
        String pointsString = (String)go.getState("LaserAmmopoints");
		if (pointsString != null) {
			String[] pointsSplit = pointsString.split("/");
			for (int i = 0; i < pointsSplit.length; i++) {
				points.add(new Vector2f(pointsSplit[i]));
			}
		}
    }

    @Override
    public void render(Gameobject go) {
        Renderer.renderLines(points, 5, new Vector4f(1f,1,1f,1));
    }

}


