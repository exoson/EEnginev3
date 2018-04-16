
package math;

public class Vector2i 
{
    private int x,y;
    
    public Vector2i()
    {
        this(0,0);
    }
    public Vector2i(int x,int y)
    {
        this.x = x;
        this.y = y;
    }
    public Vector2i(Vector2f vec)
    {
        this.x = (int)vec.getX();
        this.y = (int)vec.getY();
    }
    
    public float length()
    {
        return (float)Math.sqrt(x*x+y*y);
    }
    
    public Vector2i add(Vector2i vect)
    {
        return new Vector2i(x + vect.getX(),y + vect.getY());
    }
    
    public Vector2i minus(Vector2i vect)
    {
        return new Vector2i(x - vect.getX(),y - vect.getY());
    }
    public Vector2i mult(int mul)
    {
        return new Vector2i(x * mul,y * mul);
    }
    public Vector2i div(int div) {
        return new Vector2i(x / div,y / div);
    }
    public int dot(Vector2i vect)
    {
        return x * vect.getX() + y * vect.getY();
    }
    public Vector2i dot(Vector2i row1, Vector2i row2)
    {
        Vector2i temp;
        int tempX = this.dot(row1);
        int tempY = this.dot(row2);
        
        temp = new Vector2i(tempX, tempY);
        return temp;
    }
    public Vector2i normalize()
    {
        float length = length();
        if(length != 0) {
            return div((int)length);
        }
        else {
            return this;
        }
    }
    public int getX()
    {
        return x;
    }
    public int getY()
    {
        return y;
    }
    public void setX(int var) {
        x = var;
    }
    public void setY(int var) {
        y = var;
    }
    @Override
    public String toString() {
        return getX() + " " + getY();
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 19 * hash + Float.floatToIntBits(this.x);
        hash = 19 * hash + Float.floatToIntBits(this.y);
        return hash;
    }

    @Override
    public boolean equals(Object v) {
        if(!Vector2i.class.isInstance(v)) return false;
        return ((Vector2i)v).getX() == x && ((Vector2i)v).getY() == y;
    }
}
