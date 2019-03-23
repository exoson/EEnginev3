package client.game;

import common.game.Vector2f;
import common.game.Vector3f;

public class Map
{
    public static final int SQRSIZE = 32;

    private int width, height;

    private int[][] squares;

    public Map(String sqrs) {
        if(sqrs.isEmpty()) {
            return;
        }
        String[] splitted = sqrs.split(",");
        width = splitted[0].length();
        height = splitted.length;
        squares = new int[width][height];
        for(int j = 0; j < height; j++) {
            for(int i = 0; i < width; i++) {
                squares[i][j] = Integer.parseInt("" + splitted[j].charAt(i));
            }
        }
    }
    public void update() {
    }

    public void render() {
        if(squares == null) return;
        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
                Square.values()[squares[i][j]].render(new Vector3f(i*SQRSIZE, j*SQRSIZE, 0.1f));
            }
        }
    }

    public int[][] getSquares() {
        return squares;
    }

}
