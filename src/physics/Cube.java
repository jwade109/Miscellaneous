package physics;

public class Cube extends FreeBody implements Collidable
{
    public final double side;
    public final Vector span;
    
    public static void main(String[] args)
    {
        Cube c = new Cube(2, 10);
        Sphere s = new Sphere(1, 5);
        
        System.out.println(s.isCollidingWith(c));
        System.out.println(c.isCollidingWith(s));
        
        s.position = new Vector(100, 0, 0);

        System.out.println(s.isCollidingWith(c));
        System.out.println(c.isCollidingWith(s));
    }

    public Cube(double side, double mass)
    {
        super(mass, mass * Math.pow(side, 2) / 6);
        this.side = side;
        span = new Vector(side / 2, side / 2, side / 2);
    }

    @Override
    public boolean isCollidingWith(Collidable other)
    {
        return contains(other.getClosestPointTo(position));
    }

    @Override
    public Vector getClosestPointTo(Vector point)
    {
        if (contains(point))
        {
            return point;
        }

        Vector upper = position.add(span);
        Vector lower = position.subtract(span);
        double xs = point.x + position.x;
        double ys = point.y + position.y;
        double zs = point.z + position.z;
        
        if (xs < lower.x)
        {
            xs = lower.x;
        }
        else if (xs > upper.x)
        {
            xs = upper.x;
        }
        
        if (ys < lower.y)
        {
            ys = lower.y;
        }
        else if (ys > upper.y)
        {
            ys = upper.y;
        }
        
        if (zs < lower.z)
        {
            zs = lower.z;
        }
        else if (zs > upper.z)
        {
            zs = upper.z;
        }
        
        return new Vector(xs, ys, zs);
    }

    @Override
    public boolean contains(Vector point)
    {
        Vector upper = position.add(span);
        Vector lower = position.subtract(span);
        boolean xBound = bound(point.x, lower.x, upper.x);
        boolean yBound = bound(point.y, lower.y, upper.y);
        boolean zBound = bound(point.z, lower.z, upper.z);
        return xBound && yBound && zBound;
    }

    private boolean bound(double a, double l, double u)
    {
        return l <= a && a <= u;
    }
    
    public String toString()
    {
        StringBuilder out = new StringBuilder("Cube of side length ");
        out.append(side);
        out.append(" meters.\n");
        out.append(super.toString());
        return out.toString();
    }

}
