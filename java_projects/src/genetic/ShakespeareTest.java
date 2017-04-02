package genetic;

public class ShakespeareTest
{
    private static final int MAXSIZE = 10000;
    private static final int MAXITER = 10000;
    private static final double KILLRATE = 0.7;

    public static void main(String[] args)
    {
     // create the population of snakes
        Population<Shakespeare> shake = new Population<Shakespeare>(MAXSIZE);
        for (int i = 0; i < MAXSIZE; i++)
        {
            shake.add(new Shakespeare());
        }
        
        int iter = 0;
        
        // iterate through the maximum number of generations
        while (iter < MAXITER)
        {
            shake.killLeastFit(KILLRATE); // kill the least fit half of snakes
            
            System.out.println(shake.size() + " " + shake.getStats()[0] + " " + shake.getStats()[1] + " " + shake.getStats()[2]);
            
            shake.repopulate(); // replace the snakes killed

            iter++;
            
            System.out.println(shake.size() + " " + shake.getStats()[0] + " " + shake.getStats()[1] + " " + shake.getStats()[2]);
                    }
        
        // toString for the end of the iterations
        
        System.out.println("Results after " + iter + " generations: ");
        System.out.print("Least fit : " + shake.getLeastFit().toString());
        System.out.println(" fitness: " + shake.getLeastFit().getFitness());
        System.out.print("Most fit :  " + shake.getMostFit().toString());
        System.out.println(" fitness: " + shake.getMostFit().getFitness());
    }
}
