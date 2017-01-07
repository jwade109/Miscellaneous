package physics;

import java.text.DecimalFormat;

public class Vector
{
    public final double x;
    public final double y;
    public final double z;

    public static final Vector i = new Vector(1, 0, 0);
    public static final Vector j = new Vector(0, 1, 0);
    public static final Vector k = new Vector(0, 0, 1);
    public static final Vector zero = new Vector(0, 0, 0);

    /**
     * Creates a new Vector object.
     * 
     * @param x The x component.
     * @param y The y component.
     * @param z The z component.
     */
    public Vector(double x, double y, double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Gets a random unit vector.
     * 
     * @return A new random 3D unit vector.
     */
    public static Vector random3D()
    {
        double x = Math.random() * 2 - 1;
        double y = Math.random() * 2 - 1;
        double z = Math.random() * 2 - 1;
        Vector u = new Vector(x, y, z); 
        return u.divide(u.mag());
    }

    /**
     * Gets a random unit vector.
     * 
     * @return A new random 2D unit vector.
     */
    public static Vector random2D()
    {
        double x = Math.random() * 2 - 1;
        double y = Math.random() * 2 - 1;
        Vector u = new Vector(x, y, 0); 
        return u.divide(u.mag());
    }

    /**
     * Returns the negative of this vector.
     * 
     * @return The vector pointing in the opposite direction.
     */
    public Vector neg()
    {
        return new Vector(-x, -y, -z);
    }

    /**
     * Gets the magnitude of this Vector.
     * 
     * @return The magnitude, a decimal value.
     */
    public double mag()
    {
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
    }

    /**
     * Adds this vector to another.
     * 
     * @param other The vector to add.
     * @return A new Vector object.
     */
    public Vector add(Vector other)
    {
        double newX = this.x + other.x;
        double newY = this.y + other.y;
        double newZ = this.z + other.z;
        return new Vector(newX, newY, newZ);
    }

    /**
     * Subtracts this vector from another.
     * 
     * @param other The vector to subtract.
     * @return A new Vector object.
     */
    public Vector subtract(Vector other)
    {
        return add(other.neg());
    }

    /**
     * Multiplies this vector by a scalar.
     * 
     * @param scalar The constant to multiply by.
     * @return A new Vector object.
     */
    public Vector multiply(double scalar)
    {
        double newX = this.x * scalar;
        double newY = this.y * scalar;
        double newZ = this.z * scalar;
        return new Vector(newX, newY, newZ);
    }

    /**
     * Divides this vector by a scalar.
     * 
     * @param scalar The constant to divide by.
     * @return A new Vector object.
     */
    public Vector divide(double scalar)
    {
        return multiply(1 / scalar);
    }

    /**
     * Gets the dot product of this vector and another.
     * 
     * @param other Another Vector object.
     * @return The dot product, a decimal value.
     */
    public double dot(Vector other)
    {
        return x * other.x + y * other.y + z * other.z;
    }

    /**
     * Gets the cross product of this vector and another. Since this operation
     * is anticommutative, this can be interpreted as returning "this x other".
     * 
     * @param other Another Vector object.
     * @return The cross product, another vector.
     */
    public Vector cross(Vector other)
    {
        double newX = y * other.z - z * other.y;
        double newY = z * other.x - x * other.z;
        double newZ = x * other.y - y * other.x;
        return new Vector(newX, newY, newZ);
    }

    /**
     * Returns the angle between this vector and another, in radians.
     * 
     * @param other Another vector.
     * @return The angle in radians.
     */
    public double angle(Vector other)
    {
        return Math.acos(dot(other) / (mag() * other.mag()));
    }

    /**
     * Gets a String representation of this Vector.
     * 
     * @return A String of the form: (x, y, z)
     */
    public String toString()
    {
        DecimalFormat d = new DecimalFormat("#.###");
        return "<" + d.format(x) + ", " + d.format(y) + ", " + d.format(z) + ">";
    }

    /**
     * Checks of this Vector object is equal to another.
     * 
     * @param Another Vector object to compare to.
     * @return Whether the other vector is identical to this one.
     */
    public boolean equals(Object other)
    {
        if (other == null || other.getClass() != getClass())
        {
            return false;
        }
        if (other == this)
        {
            return true;
        }

        Vector otherVector = (Vector) other;

        return x == otherVector.x && y == otherVector.y && z == otherVector.z;
    }
}
