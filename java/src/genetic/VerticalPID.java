package genetic;

public class VerticalPID implements Genetic<Double>
{
    private Double[] dna;
    private final double mutationRate = 0.2;
    private double t = -1;
    
    public VerticalPID()
    {
        dna = new Double[2];
        dna[0] = Math.random() * 10;
        dna[1] = Math.random() * 10;
        System.out.println(dna[0] + " : " + dna[1]);
    }
    
    private VerticalPID(Double[] dna)
    {
        this.dna = dna;
    }
    
    @Override
    public int getFitness()
    {
        int target = 700;
        double t = 0;
        double dt = 0.001;
        double y = 0;
        double ey = 1;
        double vy = 0;
        double ay = 0;
        double f = 0;
        
        while (ey > 0.1 || Math.abs(vy) > 0.1 && t < 1000)
        {
            if (y > 800)
            {
                this.t = t;
                return (int) (target - y);
            }
            
            double eyold = ey;
            ey = target - y;
            double eyr = ey - eyold;
            
            f = 2 * (dna[0] * ey + dna[1] * eyr);
            
            ay = f;
            vy += ay * dt;
            y = y += vy * dt;
            
            t += dt;
        }
        
        this.t = t;
        return - (int) (1000 * Math.abs(t - 5));
    }

    @Override
    public void setFitness(int fit)
    {
    }

    @Override
    public Double[] getDNA()
    {
        return dna.clone();
    }

    @Override
    public void mutate()
    {
        if (Math.random() < mutationRate)
        {
            dna[(int) Math.round(Math.random())] = Math.random() * 10;
        }
    }

    @Override
    public Genetic<Double> reproduceWith(Genetic<Double> other)
    {
        Double[] newDna = new Double[2];
        if (Math.random() < 0.5)
        {
            newDna[0] = dna[0];
            newDna[1] = other.getDNA()[1];
        }
        else
        {
            newDna[0] = other.getDNA()[0];
            newDna[1] = dna[1];
        }
        return new VerticalPID(newDna);
    }
    
    public String toString()
    {
        return "(" + Math.round(dna[0]) + ", " + Math.round(dna[1]) + ", " + t + ")";
    }

}
