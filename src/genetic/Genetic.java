package genetic;

public interface Genetic<T>
{    
    /**
     * Returns the fitness value for this Genetic.
     * 
     * @return fitness, an int.
     */
    public int getFitness();

    /**
     * Sets the fitness value for this Genetic.
     * 
     * @param fit The fitness.
     */
    public void setFitness(int fit);

    /**
     * Gets the DNA of this Genetic, which encodes what the object is, exactly.
     * 
     * @return The DNA, a T[].
     */
    public T[] getDNA();

    /**
     * Adds occasional random defects to this Genetic's DNA.
     */
    public void mutate();

    /**
     * Produces offspring between this Genetic and another.
     * 
     * @param other Another Genetic object.
     * @return 
     * @return A Genetic object which represents a child.
     */
    public Genetic<T> reproduceWith(Genetic<T> other);
}
