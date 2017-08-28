
package game;

import behaviors.Transform;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.Gameobject;
import main.Main;
import main.Physics;
import math.Vector3f;

public class Map
{
    public static final int WIDTH = 26, HEIGHT = 20, SQRSIZE = 32;
    
    private int[][] squares;
    
    public Map() {
        initRandomMap();
    }
    public void update() {
        
    }
    
    public boolean checkCollisionsAll(Transform tf) {
        for(int y = 0; y < HEIGHT; y++) { 
            for(int x = 0; x < WIDTH; x++) {
                if(squares[x][y] == 0) continue;
                if(checkCollisions(tf, x, y)) {
                    return true;
                }
            }
        }
        return false;
    }
    public boolean checkCollisionsAll(Gameobject go) {
        Transform tf = (Transform)go.getBehavior("Transform");
        return checkCollisionsAll(tf);
    }
    
    public boolean checkCollisions(Gameobject go, int x, int y) {
        Transform tf = (Transform)go.getBehavior("Transform");
        return checkCollisions(tf, x, y);
    }
    
    public boolean checkCollisions(Transform tf, int x, int y) {
        return Physics.rectRectCollision(tf, new Vector3f[]{
                        new Vector3f((x-1f/2) * SQRSIZE,(y-1f/2) * SQRSIZE, 0),
                        new Vector3f((x+1f/2) * SQRSIZE,(y-1f/2) * SQRSIZE, 0),
                        new Vector3f((x-1f/2) * SQRSIZE,(y+1f/2) * SQRSIZE, 0),
                        new Vector3f((x+1f/2) * SQRSIZE,(y+1f/2) * SQRSIZE, 0)
                    });
    }
    
//    private void checkVisibilitya()
//    {
//        for(int x = 0; x < WIDTH;x++) {
//            for(int y = 0; y < HEIGHT;y++) {
//                visiblesqrs[0][x][y] = false;
//                visiblesqrs[1][x][y] = false;
//            }
//        }
//        for(int x = 0; x < WIDTH;x++) {
//            for(int y = 0; y < HEIGHT;y++) {
//                visiblesqrs[0][x][y] = checkVisibility(0,x,y);
//                visiblesqrs[1][x][y] = checkVisibility(1,x,y);
//            }
//        }
//    }
//    private boolean checkVisibility(int team,int x,int y) {
//        if(getVisiblesqrs()[team][x][y]) return true;
//        for(Gameobject go : Game.getObjects()) {
//            if(go.getTeam() != team) continue;
//            if(Util.dist(x * Game.SQUARESIZE, y * Game.SQUARESIZE, go.getX(), go.getY()) > 600) continue;
//            if(Util.los(go,x * Game.SQUARESIZE,y * Game.SQUARESIZE)) return true;
//        }
//        return false;
//    }
//    public boolean getVisibility(int x, int y, int team) {
//        if(x >= WIDTH || x < 0 || y >= HEIGHT || y < 0 || team >= visiblesqrs.length) {
//            return false;
//        }
//        return visiblesqrs[team][x][y];
//    }
//    public void render() {
//        for(int x = 0; x < WIDTH; x++) {
//            for(int y = 0;y < HEIGHT; y++) {
//                //if(Math.abs(x * Game.SQUARESIZE + Game.game.getShiftX()) < Display.getWidth() && Math.abs(y * Game.SQUARESIZE + Game.game.getShiftY()) < Display.getHeight()){
//                float modf = Game.getFow() ? (getVisiblesqrs()[Game.getTeam()][x][y] ? 1 : 10) : 1;
//                getSquares()[x][y].render(new Vector2f(x*Game.SQUARESIZE,y*Game.SQUARESIZE),1 / modf,1 / modf,1 / modf);
//                //}
//            }
//        }
//    }
//    public Square getsquare(int x,int y)
//    {
//        if(x >= WIDTH || x < 0 || y >= HEIGHT || y < 0) 
//        {
//            return null;
//        }
//        return getSquares()[x][y];
//    }
    public void initRandomMap() {
//        WIDTH = 26;
//        HEIGHT = 20;
        squares = new int[WIDTH][HEIGHT];
        Random rng = new Random();
        for(int y = 0; y < HEIGHT; y++) { 
            for(int x = 0; x < WIDTH; x++) {
                int temp = rng.nextFloat() > 0.9 ? 1 : 0;
                for(Gameobject go : Main.getGame().getObjects()) {
                    if(checkCollisions(go, x, y)) {
                        temp = 0;
                    }
                    if(x == 0 || x == WIDTH-1 || y == 0 || y == HEIGHT-1) {
                        temp = 1;
                    }
                }
                squares[x][y] = temp;
            }
        }
    }
    public void initMap(String mapname)
    {
        try {
            
            try (BufferedReader r = new BufferedReader(new FileReader("res/maps/" + mapname + ".txt")))
            {
//                WIDTH = Integer.parseInt(r.readLine());
//                HEIGHT = Integer.parseInt(r.readLine());
                squares = new int[WIDTH][HEIGHT];
                for(int y = 0; y < HEIGHT; y++) { 
                    for(int x = 0; x < WIDTH; x++) {
                        char tempchar = (char)r.read();
                        squares[x][y] = Integer.parseInt("" + tempchar);
                    }
                    r.readLine();
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Map.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public int[][] getSquares() {
        return squares;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int j = 0; j < HEIGHT; j++) {
            for(int i = 0; i < WIDTH; i++) {
                sb.append(squares[i][j]);
            }
            sb.append(',');
        }
        return sb.toString();
    }
}
