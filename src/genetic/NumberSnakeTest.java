package genetic;

/**
 * A runner class for testing the evolution of a Population of NumberSnakes,
 * given a definition of maximum fitness in the static method at the bottom.
 * 
 * @author Wade Foster
 * @version 2016.12.12
 */
public class NumberSnakeTest
{
    /**
     * the rate at which NumberSnakes are killed off
     */
    private static final double KILLRATE = 0.5;
    /**
     * The maximum size of the Population
     */
    private static final int MAXSIZE = 100;
    /**
     * Maximum generations to iterate through
     */
    private static final int MAXITER = 1000;

    /**
     * Runs the program
     * 
     * @param args
     */
    public static void main(String[] args)
    {
        // create the population of snakes
        Population<NumberSnake> snakes = new Population<NumberSnake>(MAXSIZE);
        for (int i = 0; i < MAXSIZE; i++)
        {
            snakes.add(new NumberSnake());
        }
        
        int iter = 0;
        
        // iterate through the maximum number of generations
        while (iter < MAXITER)
        {
            snakes.killLeastFit(KILLRATE); // kill the least fit half of snakes

            snakes.repopulate(); // replace the snakes killed

            for (NumberSnake n : snakes)
            {
                setFitness(n); // update fitness values for each snake
            }

            iter++;
        }
        
        // toString for the end of the iterations
        
        System.out.println("Results after " + iter + " generations: ");
        System.out.print("Least fit : " + snakes.getLeastFit().toString());
        System.out.println(" fitness: " + snakes.getLeastFit().getFitness());
        System.out.print("Most fit :  " + snakes.getMostFit().toString());
        System.out.println(" fitness: " + snakes.getMostFit().getFitness());

    }

    /**
     * Sets a NumberSnake's fitness value based on whatever rules are required.
     * 
     * @param n The NumberSnake to update.
     */
    private static void setFitness(NumberSnake n)
    {
        int sum = 1000;
        int x = 0;
        for (Integer i : n.getDNA())
        {
            sum -= Math.abs(i - x);
            x++;
        }
        n.setFitness(sum);
    }
}
