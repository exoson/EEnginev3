package server.game;

import java.util.ArrayList;
import common.game.Gameobject;
import common.game.Vector2f;
import common.game.Vector3f;

public class Physics {
    /**
     * Calculates which Gameobjects collide with the specified sphere.
     * @param tf Transform which collisions we wanna check
     * @param radius radius of the sphere.
     * @param gObjects ArrayList of objects we wanna check collisions with
     * @return ArrayList of Gameobjects collided with.
     */
    public static ArrayList<Gameobject> sphereCollide(Transform tf,float radius, ArrayList<Gameobject> gObjects) {
        ArrayList<Gameobject> res = new ArrayList<>();
        for(Gameobject go : gObjects) {
            if(rectCircleCollision(go, tf.getPosition(), radius)) {
                res.add(go);
            }
        }
        return res;
    }
    public static boolean rectRectCollision(Gameobject go1, Gameobject go2) {
        Transform t1 = (Transform)go1.getBehavior("Transform");
        Transform t2 = (Transform)go2.getBehavior("Transform");
        return rectRectCollision(t1, t2);
    }

    public static boolean rectRectCollision(Transform t1, Transform t2) {
        Vector2f[] rotationMatrix1 = new Vector2f[] {
            new Vector2f((float)Math.cos(-t1.getRotation().getZ()),
                    (float)Math.sin(-t1.getRotation().getZ())),
            new Vector2f(-(float)Math.sin(-t1.getRotation().getZ()),
                    (float)Math.cos(-t1.getRotation().getZ()))
        };
        Vector2f[] rotationMatrix2 = new Vector2f[]{
            new Vector2f((float)Math.cos(-t2.getRotation().getZ()),
                    (float)Math.sin(-t2.getRotation().getZ())),
            new Vector2f(-(float)Math.sin(-t2.getRotation().getZ()),
                    (float)Math.cos(-t2.getRotation().getZ()))
        };
        Vector3f[] corners1 = new Vector3f[4];
        Vector3f[] corners2 = new Vector3f[4];
        corners1[0] = t1.getPosition().add(new Vector2f(-t1.getSX()/2,-t1.getSY()/2).dot(rotationMatrix1[0],rotationMatrix1[1]));
        corners1[1] = t1.getPosition().add(new Vector2f(t1.getSX()/2,-t1.getSY()/2).dot(rotationMatrix1[0],rotationMatrix1[1]));
        corners1[2] = t1.getPosition().add(new Vector2f(-t1.getSX()/2,t1.getSY()/2).dot(rotationMatrix1[0],rotationMatrix1[1]));
        corners1[3] = t1.getPosition().add(new Vector2f(t1.getSX()/2,t1.getSY()/2).dot(rotationMatrix1[0],rotationMatrix1[1]));
        corners2[0] = t2.getPosition().add(new Vector2f(-t2.getSX()/2,-t2.getSY()/2).dot(rotationMatrix2[0],rotationMatrix2[1]));
        corners2[1] = t2.getPosition().add(new Vector2f(t2.getSX()/2,-t2.getSY()/2).dot(rotationMatrix2[0],rotationMatrix2[1]));
        corners2[2] = t2.getPosition().add(new Vector2f(-t2.getSX()/2,t2.getSY()/2).dot(rotationMatrix2[0],rotationMatrix2[1]));
        corners2[3] = t2.getPosition().add(new Vector2f(t2.getSX()/2,t2.getSY()/2).dot(rotationMatrix2[0],rotationMatrix2[1]));

        return rectRectCollision(corners1, corners2);
    }
    public static boolean rectRectCollision(Gameobject go1, Vector3f[] corners2) {
        Transform t1 = (Transform)go1.getBehavior("Transform");
        return rectRectCollision(t1, corners2);
    }
    public static boolean rectRectCollision(Transform t1, Vector3f[] corners2) {
        boolean tooFar = true;
        for (Vector3f corner : corners2) {
            if(Util.dist(t1.getPosition(), corner) < (t1.getSX() + t1.getSY())) {
                tooFar = false;
                break;
            }
        }
        if(tooFar) return false;

        Vector2f[] rotationMatrix1 = new Vector2f[]{
            new Vector2f((float)Math.cos(-t1.getRotation().getZ()),
                    (float)Math.sin(-t1.getRotation().getZ())),
            new Vector2f(-(float)Math.sin(-t1.getRotation().getZ()),
                    (float)Math.cos(-t1.getRotation().getZ()))
        };
        Vector3f[] corners1 = new Vector3f[4];
        corners1[0] = t1.getPosition().add(new Vector2f(-t1.getSX()/2,-t1.getSY()/2).dot(rotationMatrix1[0],rotationMatrix1[1]));
        corners1[1] = t1.getPosition().add(new Vector2f(t1.getSX()/2,-t1.getSY()/2).dot(rotationMatrix1[0],rotationMatrix1[1]));
        corners1[2] = t1.getPosition().add(new Vector2f(-t1.getSX()/2,t1.getSY()/2).dot(rotationMatrix1[0],rotationMatrix1[1]));
        corners1[3] = t1.getPosition().add(new Vector2f(t1.getSX()/2,t1.getSY()/2).dot(rotationMatrix1[0],rotationMatrix1[1]));

        return rectRectCollision(corners1, corners2);
    }
    public static boolean rectRectCollision(Vector3f[] corners1,Vector3f[] corners2)
    {

        boolean collided = true;

        Vector3f[] axises = new Vector3f[8];
        axises[0] = corners1[1].minus(corners1[0]);
        axises[1] = corners1[1].minus(corners1[3]);
        axises[2] = corners2[1].minus(corners2[0]);
        axises[3] = corners2[1].minus(corners2[3]);
        axises[4] = axises[0].mult(-1);
        axises[5] = axises[1].mult(-1);
        axises[6] = axises[2].mult(-1);
        axises[7] = axises[3].mult(-1);
        for(Vector3f axis : axises) {
            float lSqr = axis.dot(axis);
            float min1 = Float.MAX_VALUE;
            float max1 = Float.MIN_VALUE;
            float min2 = Float.MAX_VALUE;
            float max2 = Float.MIN_VALUE;

            for(Vector3f corner : corners1) {
                Vector3f temp = axis.mult(axis.dot(corner)/lSqr);
                float location = temp.dot(axis);
                if(location < min1){
                    min1 = location;
                }
                if(location > max1){
                    max1 = location;
                }
            }
            for(Vector3f corner : corners2) {
                Vector3f temp = axis.mult(axis.dot(corner)/lSqr);
                float location = temp.dot(axis);
                if(location < min2) {
                    min2 = location;
                }
                if(location > max2) {
                    max2 = location;
                }
            }
            if(min2 > max1) {
                collided = false;
                break;
            }
            if(min1 > max2) {
                collided = false;
                break;
            }
        }
        return collided;
    }
    public static boolean rectCircleCollision(Gameobject go1, Vector3f v, float radius) {
        Transform t1 = go1.getBehavior("Transform");
        if(t1 == null) return false;

        Vector2f[] rotationMatrix1 = new Vector2f[]{
            new Vector2f((float)Math.cos(-t1.getRotation().getZ()),
                    (float)Math.sin(-t1.getRotation().getZ())),
            new Vector2f(-(float)Math.sin(-t1.getRotation().getZ()),
                    (float)Math.cos(-t1.getRotation().getZ()))
        };
        Vector3f[] corners1 = new Vector3f[4];
        corners1[0] = t1.getPosition().add(new Vector2f(-t1.getSX()/2,-t1.getSY()/2).dot(rotationMatrix1[0],rotationMatrix1[1]));
        corners1[1] = t1.getPosition().add(new Vector2f(t1.getSX()/2,-t1.getSY()/2).dot(rotationMatrix1[0],rotationMatrix1[1]));
        corners1[2] = t1.getPosition().add(new Vector2f(-t1.getSX()/2,t1.getSY()/2).dot(rotationMatrix1[0],rotationMatrix1[1]));
        corners1[3] = t1.getPosition().add(new Vector2f(t1.getSX()/2,t1.getSY()/2).dot(rotationMatrix1[0],rotationMatrix1[1]));

        return circleIntersectLine(v, radius, corners1[0], corners1[1]) ||
               circleIntersectLine(v, radius, corners1[1], corners1[3]) ||
               circleIntersectLine(v, radius, corners1[2], corners1[3]) ||
               circleIntersectLine(v, radius, corners1[2], corners1[0]) ||
                pointInRectangle(go1, v);
    }
    public static boolean circleIntersectLine(Vector3f mid, float rad, Vector3f start, Vector3f end) {
        Vector3f startCenter = mid.minus(start);
        float scLength = startCenter.length();
        float ecLength = mid.minus(end).length();
        Vector3f startEnd = end.minus(start);
        float projection = startEnd.dot(startCenter) / startEnd.length();
        float dist = (float)Math.sqrt(projection*projection+scLength*scLength);
        return dist < rad && (scLength < rad || ecLength < rad);
    }
//    public static boolean pointInRectangle(UIobject uo, Vector2f v)
//    {
//        Vector2f[] rotationMatrix1 = new Vector2f[]{
//            new Vector2f((float)Math.cos(-uo.getRotation()),
//                    (float)Math.sin(-uo.getRotation())),
//            new Vector2f(-(float)Math.sin(-uo.getRotation()),
//                    (float)Math.cos(-uo.getRotation()))
//        };
//        Vector2f a = uo.getPos().add(new Vector2f(-uo.getSX()/2,-uo.getSY()/2).dot(rotationMatrix1[0],rotationMatrix1[1]));
//        Vector2f b = uo.getPos().add(new Vector2f(uo.getSX()/2,-uo.getSY()/2).dot(rotationMatrix1[0],rotationMatrix1[1]));
//        Vector2f c = uo.getPos().add(new Vector2f(-uo.getSX()/2,uo.getSY()/2).dot(rotationMatrix1[0],rotationMatrix1[1]));
//        Vector2f d = uo.getPos().add(new Vector2f(uo.getSX()/2,uo.getSY()/2).dot(rotationMatrix1[0],rotationMatrix1[1]));
//
//        return 0 < b.minus(a).dot(v.minus(a)) &&
//                b.minus(a).dot(v.minus(a)) < b.minus(a).dot(b.minus(a)) &&
//                0 < d.minus(a).dot(v.minus(a)) &&
//                d.minus(a).dot(v.minus(a)) < d.minus(a).dot(d.minus(a));
//    }
    public static boolean pointInRectangle(Gameobject go1, Vector3f v) {
        Transform t1 = (Transform)go1.getBehavior("Transform");
        Vector2f[] rotationMatrix1 = new Vector2f[]{
            new Vector2f((float)Math.cos(-t1.getRotation().getZ()),
                    (float)Math.sin(-t1.getRotation().getZ())),
            new Vector2f(-(float)Math.sin(-t1.getRotation().getZ()),
                    (float)Math.cos(-t1.getRotation().getZ()))
        };
        Vector3f a = t1.getPosition().add(new Vector2f(-t1.getSX()/2,-t1.getSY()/2).dot(rotationMatrix1[0],rotationMatrix1[1]));
        Vector3f b = t1.getPosition().add(new Vector2f(t1.getSX()/2,-t1.getSY()/2).dot(rotationMatrix1[0],rotationMatrix1[1]));
        Vector3f c = t1.getPosition().add(new Vector2f(-t1.getSX()/2,t1.getSY()/2).dot(rotationMatrix1[0],rotationMatrix1[1]));
        Vector3f d = t1.getPosition().add(new Vector2f(t1.getSX()/2,t1.getSY()/2).dot(rotationMatrix1[0],rotationMatrix1[1]));

        return 0 < b.minus(a).dot(v.minus(a)) &&
                b.minus(a).dot(v.minus(a)) < b.minus(a).dot(b.minus(a)) &&
                0 < d.minus(a).dot(v.minus(a)) &&
                d.minus(a).dot(v.minus(a)) < d.minus(a).dot(d.minus(a));
    }
}
