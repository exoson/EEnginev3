
package math;

public class Vector3f 
{
    private float x,y,z;
    
    public Vector3f() {
        this(0,0,0);
    }
    
    public Vector3f(String str) {
        String[] splitted = str.split(",");
        this.x = Float.parseFloat(splitted[0]);
        this.y = Float.parseFloat(splitted[1]);
        this.z = Float.parseFloat(splitted[2]);
    }
    
    public Vector3f(Vector2f v) {
        this(v.getX(),v.getY(),0);
    }
    
    public Vector3f(float x,float y,float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public float length() {
        return (float)Math.sqrt(x*x+y*y+z*z);
    }
    
    public Vector3f add(Vector2f vect) {
        return new Vector3f(x + vect.getX(),y + vect.getY(), z);
    }
    public Vector3f add(Vector3f vect) {
        return new Vector3f(x + vect.getX(),y + vect.getY(), z + vect.getZ());
    }
    public float dot(Vector3f vect) {
        return vect.x*x+vect.y*y+vect.z*z;
    }
    public Vector3f minus(Vector3f vect) {
        return new Vector3f(x - vect.getX(),y - vect.getY(),z - vect.getZ());
    }
    public Vector3f mult(float mul) {
        return new Vector3f(x * mul,y * mul,z * mul);
    }
    public Vector3f div(float div) {
        return new Vector3f(x / div,y / div,z / div);
    }
    public Vector3f normalize() {
        float length = length();
        if(length != 0) {
            return div(length);
        }
        else {
            return this;
        }
    }
    public float getX() {
        return x;
    }
    public float getY() {
        return y;
    }
    public float getZ() {
        return z;
    }
    public void setX(float var) {
        x = var;
    }
    public void setY(float var) {
        y = var;
    }
    public void setZ(float var) {
        z = var;
    }
    @Override
    public String toString() {
        return "" + x + ", " + y + ", " + z;
    }

}
