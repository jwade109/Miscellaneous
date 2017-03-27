package genetic;

public class NumberSnake implements Genetic<Integer>
{
    private Integer[] dna; // an array of numbers between 0 and 99.
    private static final int DNA_LENGTH = 25;
    private int fitness;

    /**
     * Constructs a new NumberSnake.
     */
    public NumberSnake()
    {
        dna = new Integer[DNA_LENGTH];
        for (int i = 0; i < DNA_LENGTH; i++)
        {
            dna[i] = (int) (Math.random() * 100);
        }
    }

    /**
     * Constructs a new NumberSnake with a given DNA.
     * 
     * @param dna The DNA for this NumberSnake.
     */
    private NumberSnake(Integer[] dna)
    {
        this.dna = dna;
    }

    /**
     * Returns the fitness value. For NumberSnakes, this is calculated
     * internally based on the DNA - the fitness is equal to the sum of the
     * entries in the DNA.
     * 
     * @return The fitness of this NumberSnake.
     */
    public int getFitness()
    {
        return fitness;
    }

    /**
     * Sets the fitness level of this NumberSnake.
     * 
     * @param fit The fitness to be set.
     */
    public void setFitness(int fit)
    {
        fitness = fit;
    }

    /**
     * Gets the DNA for this NumberSnake.
     * 
     * @return an Integer[] representing the DNA.
     */
    public Integer[] getDNA()
    {
        return dna.clone();
    }

    /**
     * Has a chance of introducing a random change to the NumberSnake's DNA.
     */
    @Override
    public void mutate()
    {
        int randomIndex = (int) (Math.random() * 100);
        int randomNumber = (int) (Math.random() * 100);
        if (randomIndex < DNA_LENGTH)
        {
            dna[randomIndex] = randomNumber;
        }
    }

    /**
     * Produces offspring between this NumberSnake and another.
     * 
     * @param other Another NumberSnake.
     * @return A NumberSnake representing the child of the two parents.
     */
    @Override
    public NumberSnake reproduceWith(Genetic<Integer> other)
    {
        if (other == null || other.getClass() != this.getClass()
                || other == this)
        {
            throw new SpeciesMismatchException();
        }
        Integer[] newdna = new Integer[DNA_LENGTH];
        for (int i = 0; i < DNA_LENGTH; i++)
        {
            if (Math.random() > 0.5)
            {
                newdna[i] = this.dna[i];
            }
            else
            {
                newdna[i] = other.getDNA()[i];
            }
        }
        NumberSnake child = new NumberSnake(newdna);
        child.mutate();
        return child;
    }

    /**
     * Gets a String representation of this NumberSnake.
     * 
     * @return A String printout of the DNA.
     */
    public String toString()
    {
        StringBuilder out = new StringBuilder("(");
        for (int i = 0; i < DNA_LENGTH; i++)
        {
            out.append(dna[i]);
            if (i < DNA_LENGTH - 1)
            {
                for (int j = 0; j < 3 - dna[i].toString().length(); j++)
                {
                    out.append(" ");
                }
            }
        }
        out.append(") ");
        return out.toString();
    }

}
