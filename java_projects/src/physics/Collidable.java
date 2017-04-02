package physics;

public interface Collidable extends Accelerable
{
    public boolean isCollidingWith(Collidable other);
    
    public Vector getClosestPointTo(Vector point);
    
    public boolean contains(Vector point);
}