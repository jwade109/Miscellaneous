package physics;

public interface Accelerable
{
    void step(double dt);
    
    void addForce(Vector force);

    void addForce(Vector force, Vector arm);
    
    boolean removeForce(Vector force);
}
