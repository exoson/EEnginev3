package server.game;

import java.util.ArrayList;

import common.game.Vector2f;
import common.game.Vector2i;
import common.game.Vector3f;
import common.game.Gameobject;

public class Util
{
    public static boolean los(Gameobject go1, Gameobject go2) {
//        return los(go1.getX(),go1.getY(),go2.getX(),go2.getY());
        return true;
    }
    public static boolean los(Gameobject go1, float x,float y) {
//        return los(go1.getX(),go1.getY(),x,y);
        return true;
    }
    public static boolean los(float x1,float y1,float x2,float y2) {
//        //check that start point is closer to 0,y than end point
//        int startx = (int)((x1 < x2 ? x1 : x2) / Game.SQUARESIZE),starty = (int)((x1 < x2 ? y1 : y2) / Game.SQUARESIZE),endx = (int)((x1 > x2 ? x1 : x2) / Game.SQUARESIZE),endy = (int)((x1 > x2 ? y1 : y2) / Game.SQUARESIZE);
//
//        float dx = (int)((endx-startx)),dy = (int)((endy - starty));
//        if((int)((x2 - x1)/ Game.SQUARESIZE) == 0 && (int)((y2 - y1) / Game.SQUARESIZE) == 0) return true;
//
//        int x,y;
//        if(dx == 0){
//            for(int i = 0; Math.abs(i) < Math.abs(dy);i += Math.abs(dy) / dy){
//                x = startx;
//                y = starty + i;
//                Square sqr = Game.getLevel().getMap().getsquare(x, y);
//                if(sqr != null) if(!sqr.getTransparent()) return false;
//            }
//
//        }else if(dy == 0){
//            for(int i = 0; Math.abs(i) < Math.abs(dx);i += Math.abs(dx) / dx){
//                x = startx + i;
//                y = starty;
//                Square sqr = Game.getLevel().getMap().getsquare(x, y);
//                if(sqr != null) if(!sqr.getTransparent()) return false;
//            }
//        }else{
//            float k = dy / dx;
//            if(Math.abs(k) < 1)
//            {
//                for(float i = 0; i < dx; i += Math.abs(k))
//                {
//                    x = startx + (int) i;
//                    y = starty + (int)(i * k);
//                    Square sqr = Game.getLevel().getMap().getsquare(x, y);
//                    if(sqr != null) if(!sqr.getTransparent()) return false;
//                }
//            }else{
//                for(float i = 0; Math.abs(i) < Math.abs(dy); i +=  1 / k)
//                {
//                    x = startx + (int)(i / k);
//                    y = starty + (int)(i);
//                    Square sqr = Game.getLevel().getMap().getsquare(x, y);
//                    if(sqr != null) if(!sqr.getTransparent()) return false;
//                }
//            }
//        }
        return true;
    }
    public static float dist(Vector3f a, Vector3f b) {
        return a.minus(b).length();
    }

    public static float dist(Vector2i a, Vector2i b) {
        return a.minus(b).length();
    }
    public static float dist(float x1, float y1, float x2, float y2) {
        return new Vector2f(x1-x2,y1-y2).length();
    }
    public static ArrayList<Vector2i> findPath(Vector3f start, Vector3f goal, int[][] map) {
        return findPath(start.as2i(), goal.as2i(), map);
    }
    public static ArrayList<Vector2i> findPath(Vector2i start, Vector2i goal, int[][] map) {
        start = start.div(Map.SQRSIZE);
        goal = goal.div(Map.SQRSIZE);
        if(start.getX() < 0 || start.getY() < 0 || start.getX() >= map.length || start.getY() >= map[0].length ||
                goal.getX() < 0 || goal.getY() < 0 || goal.getX() >= map.length || goal.getY() >= map[0].length) {
            return null;
        }
        while(map[goal.getX()][goal.getY()] > 0) goal = goal.add(new Vector2i(1,0));

        ArrayList<Vector2i> ClosedSet = new ArrayList<>();    	  // The set of nodes already evaluated.
        ArrayList<Vector2i> OpenSet = new ArrayList<>();    // The set of tentative nodes to be evaluated, initially containing the start node

        OpenSet.add(start);

        Vector2i[][] Came_From = new Vector2i[map.length][map[0].length];    // The map of navigated nodes.

        float[][] g_score = new float[map.length][map[0].length];
        for(int i = 0; i < g_score.length; i++) {
            for(int j = 0; j < g_score[i].length; j++) {
                g_score[i][j] = Float.POSITIVE_INFINITY;
            }
        }
        g_score[start.getX()][start.getY()] = 0;    // Cost from start along best known path.

        // Estimated total cost from start to goal through y.
        float[][] f_score = new float[map.length][map[0].length];
        for(int i = 0; i < f_score.length; i++) {
            for(int j = 0; j < f_score[i].length; j++) {
                f_score[i][j] = Float.POSITIVE_INFINITY;
            }
        }
        f_score[start.getX()][start.getY()] = Util.dist(start, goal);    // Cost from start along best known path.

        int i = 0;
        while(!OpenSet.isEmpty()) {
            i++;
            Vector2i current = new Vector2i();
            float minVal = Float.POSITIVE_INFINITY;
            for(Vector2i v : OpenSet) {
                if(f_score[(int)v.getX()][(int)v.getY()] < minVal) {
                    current = v;
                    minVal = f_score[(int)v.getX()][(int)v.getY()];
                }
            }
            if (current.equals(goal)) {
                return reconstruct_path(Came_From, goal);
            }
            if(i > 1000) break;
            //System.out.println(current.toString());
            OpenSet.remove(current);
            ClosedSet.add(current);
            Vector2i[] neighbors = new Vector2i[]{
                current.add(new Vector2i(1,0)),
                current.add(new Vector2i(0,1)),
                current.add(new Vector2i(-1,0)),
                current.add(new Vector2i(0,-1))
            };
            for(Vector2i neighbor : neighbors) {
                if(neighbor.getX() < 0 ||
                        neighbor.getY() < 0 ||
                        (int)(neighbor.getX()) >= g_score.length ||
                        (int)(neighbor.getY()) >= g_score[0].length){
                    continue;
                }
                if(neighbor.equals(current)) {
                    continue;
                }
                if(ClosedSet.contains(neighbor)) {
                    continue;
                }
                /*if(!sqrs[(int)neighbor.getX()][(int)neighbor.getY()].getTransparent()) {
                    continue;
                }*/
                float tentative_g_score = g_score[current.getX()][current.getY()] +
                        distanceBetween(current, neighbor); // length of this path.
                if (!OpenSet.contains(neighbor)) {	// Discover a new node
                    OpenSet.add(neighbor);
                } else if(tentative_g_score >= g_score[(int)neighbor.getX()][(int)neighbor.getY()]) {
                    continue;       // This is not a better path.
                }

                // This path is the best until now. Record it!
                Came_From[(int)neighbor.getX()][(int)neighbor.getY()] = current;
                g_score[(int)neighbor.getX()][(int)neighbor.getY()] = tentative_g_score;
                f_score[(int)neighbor.getX()][(int)neighbor.getY()] =
                        g_score[(int)neighbor.getX()][(int)neighbor.getY()] +
                        goal.minus(neighbor).length();
            }
        }
        return null;
    }
    private static float distanceBetween(Vector2i start, Vector2i end) {
        //if(!Game.getLevel().getMap().getsquare((int)end.getX(), (int)end.getY()).getTransparent())
        //    return Float.POSITIVE_INFINITY;
        /*if(!Game.getLevel().getMap().getsquare((int)start.getX(), (int)start.getY()).getTransparent())
            return Float.POSITIVE_INFINITY;*/
        return Util.dist(start, end);
    }
    private static ArrayList<Vector2i> reconstruct_path(Vector2i[][] Came_From, Vector2i current) {
        ArrayList<Vector2i> total_path = new ArrayList<>();
        current = Came_From[current.getX()][current.getY()];
        while(current != null) {
            total_path.add(current);
            current = Came_From[current.getX()][current.getY()];
        }
        return total_path;
    }
}
