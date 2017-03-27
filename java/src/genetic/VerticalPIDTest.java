package genetic;

public class VerticalPIDTest
{
    public static void main(String[] args)
    {
        Population<VerticalPID> p = new Population<VerticalPID>(100);
        
        for (int i = 0; i < 1000; i++)
        {
            p.add(new VerticalPID());
        }
        
        int iter = 0;
        while (iter < 1 && p.getStats()[2] < -20)
        {
            p.killLeastFit(0.9);
            p.repopulate();
            iter++;
        }
        System.out.println(p.getStats()[0] + " " + p.getStats()[1] + " " + p.getStats()[2] + " " + p.getMostFit().toString());
    }
}
