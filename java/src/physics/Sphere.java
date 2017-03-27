package physics;

public class Sphere extends FreeBody implements Collidable
{
    public final double radius;

    /**
     * Constructs a new Sphere object.
     * 
     * @param radius The radius of the sphere.
     * @param mass The mass.
     * @param inertia The moment of inertia.
     */
    public Sphere(double radius, double mass)
    {
        super(mass, (2.0 / 5.0) * mass * Math.pow(radius, 2));
        this.radius = radius;
    }

    public boolean isCollidingWith(Collidable other)
    {
        return contains(other.getClosestPointTo(position));
    }

    /**
     * Gets the point closest to a given point which is also bounded by this
     * Sphere.
     * 
     * @param point The point
     * @return
     */
    public Vector getClosestPointTo(Vector point)
    {
        if (this.contains(point))
        {
            return point;
        }
        Vector u = point.subtract(position);
        double k = radius / u.mag();
        u = u.multiply(k);
        return position.add(u);
    }

    /**
     * Checks whether a point is contained in this Collidable.
     * 
     * @return Whether the point is inside this sphere.
     */
    public boolean contains(Vector point)
    {
        return point.subtract(position).mag() <= radius;
    }
    
    public String toString()
    {
        StringBuilder out = new StringBuilder("Sphere of radius ");
        out.append(radius);
        out.append(" meters.\n");
        out.append(super.toString());
        return out.toString();
    }
}
