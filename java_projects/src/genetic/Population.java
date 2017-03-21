package genetic;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Represents a population of a particular Genetic species of object, and
 * supports operations simulating the process of natural selection and
 * evolution. Genetics should tend toward a maximum possible fitness, whatever
 * the definition of maximum fitness happens to be.
 * 
 * @author Wade Foster
 * @version 2016.12.12
 *
 * @param <T> A Genetic type to be used.
 */
public class Population<T extends Genetic<?>> implements Iterable<T>
{
    private ArrayList<T> population;
    private int maxSize;

    /**
     * Constructs a new population of a maximum size.
     * 
     * @param size The max size.
     */
    public Population(int size)
    {
        this.maxSize = size;
        population = new ArrayList<T>();
    }

    /**
     * Adds a Genetic object to the Population.
     * 
     * @param genetic The Genetic to add.
     */
    public void add(T genetic)
    {
        if (population.size() < maxSize)
        {
            population.add(genetic);
        }
    }

    /**
     * Removes a percent of total population, starting with Genetics with the
     * lowest fitness.
     * 
     * @param percent The percent to remove, where 1 represents 100 percent.
     */
    public void killLeastFit(double percent)
    {
        int numberToKill = (int) (population.size() * (percent));
        population.sort(new GeneticComparator());
        for (int i = 0; i < numberToKill; i++)
        {
            population.remove(0);
        }
    }

    /**
     * Makes Genetic objects produce offspring until the maximim size of the
     * Population is reached.
     * 
     * @param <E>
     */
    @SuppressWarnings("unchecked")
    public <E> void repopulate()
    {
        while (population.size() < maxSize)
        {
            // choose random two parents
            int motherIndex = 0;
            int fatherIndex = 0;
            while (motherIndex == fatherIndex)
            {
                motherIndex = (int) (Math.random() * population.size());
                fatherIndex = (int) (Math.random() * population.size());
            }
            Genetic<E> mother = (Genetic<E>) population.get(motherIndex);
            Genetic<E> father = (Genetic<E>) population.get(fatherIndex);
            population.add((T) mother.reproduceWith(father));
        }
    }

    /**
     * Gets the fittest Genetic object in the Population.
     * 
     * @return The most swole Genetic.
     */
    public T getMostFit()
    {
        if (population.size() == 0)
        {
            return null;
        }

        T mostFit = population.get(0);
        int maxFit = mostFit.getFitness();
        for (T g : population)
        {
            if (g.getFitness() > maxFit)
            {
                mostFit = g;
                maxFit = g.getFitness();
            }
        }
        return mostFit;
    }

    /**
     * Gets the least fit Genetic object.
     * 
     * @return The wimpiest, scrawniest, nerdiest little worm.
     */
    public T getLeastFit()
    {
        if (population.size() == 0)
        {
            return null;
        }

        T leastFit = population.get(0);
        int minFit = leastFit.getFitness();
        for (T g : population)
        {
            if (g.getFitness() < minFit)
            {
                leastFit = g;
                minFit = g.getFitness();
            }
        }
        return leastFit;
    }
    
    /**
     * Gets the min, median, and maximum fitness for this population.
     * 
     * @return The minimum, median, and maximum fitness.
     */
    public int[] getStats()
    {
        population.sort(new GeneticComparator());
        int min = population.get(0).getFitness();
        int median = population.get(population.size() / 2).getFitness();
        int max = population.get(population.size() - 1).getFitness();
        return new int[] {min, median, max};
    }

    /**
     * Counts how many Genetics are in the Population.
     * 
     * @return The number of Genetics.
     */
    public int size()
    {
        return population.size();
    }

    /**
     * Gets an iterator for this Population.
     * 
     * @return an Iterator.
     */
    @Override
    public Iterator<T> iterator()
    {
        return population.iterator();
    }

}
