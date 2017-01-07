package physics;

import java.util.ArrayList;

public class FreeBody implements Accelerable
{
    private ArrayList<Vector> forces;
    private Vector acceleration;
    private Vector velocity;
    private Vector position;

    private ArrayList<Vector> moments;
    private Vector angular_acceleration;
    private Vector angular_velocity;
    private Vector angular_position;

    public final double mass;
    public final double moment_of_inertia;

    /**
     * Constructs a new Free Body.
     * 
     * @param mass The mass of the body.
     * @param inertia The moment of inertia of the body.
     */
    public FreeBody(double mass, double inertia)
    {
        if (mass * inertia <= 0)
        {
            throw new IllegalArgumentException("Mass and MoI must be positive nonzero");
        }
        this.mass = mass;
        moment_of_inertia = inertia;
        forces = new ArrayList<Vector>();
        acceleration = Vector.zero;
        velocity = Vector.zero;
        position = Vector.zero;
        moments = new ArrayList<Vector>();
        angular_acceleration = Vector.zero;
        angular_velocity = Vector.zero;
        angular_position = Vector.zero;
    }

    /**
     * Applies a force through the center of mass of this body.
     * 
     * @param force The force to apply.
     */
    public void addForce(Vector force)
    {
        addForce(force, Vector.zero);
    }

    /**
     * Applies a force on a specified moment-arm from the center of mass of this
     * body.
     * 
     * @param force The force to apply.
     * @param arm The moment arm of application.
     */
    public void addForce(Vector force, Vector arm)
    {
        forces.add(force);
        moments.add(arm.cross(force));
        
        acceleration = sum(forces).divide(mass);
        angular_acceleration = sum(moments).divide(moment_of_inertia);
    }

    /**
     * Removes a force from the free body.
     * 
     * @param force The force to remove.
     * @return Whether the force as successfully removed.
     */
    public boolean removeForce(Vector force)
    {
        for (int i = 0; i < forces.size(); i++)
        {
            if (forces.get(i).equals(force))
            {
                forces.remove(i);
                moments.remove(i);
                acceleration = sum(forces).divide(mass);
                angular_acceleration = sum(moments).divide(moment_of_inertia);
                return true;
            }
        }
        return false;
    }

    /**
     * Advances this body by dt seconds in time using Euler's method.
     * 
     * @param dt The change in time for this step.
     */
    public void step(double dt)
    {
        velocity = velocity.add(acceleration.multiply(dt));
        position = position.add(velocity.multiply(dt));

        angular_velocity = angular_velocity.add(angular_acceleration.multiply(dt));
        angular_position = angular_position.add(angular_velocity.multiply(dt));
    }
    
    /**
     * Gets a String representation of this free body.
     * 
     * @return A String.
     */
    public String toString()
    {
        StringBuilder out = new StringBuilder("Forces: ");
        out.append(forces.toString());
        out.append(" Newtons\nResultant: ");
        out.append(sum(forces));
        out.append(" Newtons\nAcceleration: ");
        out.append(acceleration);
        out.append(" m/s^2\nVelocity: ");
        out.append(velocity);
        out.append(" m/s\nPosition: ");
        out.append(position);
        out.append(" meters\nMoments: ");
        out.append(moments.toString());
        out.append(" N*m\nResultant: ");
        out.append(sum(moments));
        out.append(" N*m\nAngular acceleration: ");
        out.append(angular_acceleration);
        out.append(" rad/s^2\nAngular velocity: ");
        out.append(angular_velocity);
        out.append(" rad/s\nAngular position: ");
        out.append(angular_position);
        out.append(" radians\nMass: ");
        out.append(mass);
        out.append(" kg\nMoment of Inertia: ");
        out.append(moment_of_inertia);
        out.append(" kg*m^2");
        return out.toString();
    }
        
    /**
     * Sums all the vectors acting on this free body.
     * 
     * @return The resultant vector.
     */
    private Vector sum(ArrayList<Vector> vectors)
    {
        Vector resultant = Vector.zero;
        for (Vector v : vectors)
        {
            resultant = resultant.add(v);
        }
        return resultant;
    }
}
