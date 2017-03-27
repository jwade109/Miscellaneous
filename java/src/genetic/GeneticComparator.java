package genetic;

import java.util.Comparator;

public class GeneticComparator implements Comparator<Genetic<?>>
{
    /**
     * Returns the difference in fitness between two Genetic objects.
     * 
     * @param g1 The first Genetic object.
     * @param g2 The second Genetic object.
     */
    @Override
    public int compare(Genetic<?> g1, Genetic<?> g2)
    {
        return g1.getFitness() - g2.getFitness();
    }
    
}
