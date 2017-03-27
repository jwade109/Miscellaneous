package genetic;

import java.util.Random;

public class Shakespeare implements Genetic<Character>
{
    private Character[] dna;

    public static final String TARGET = "To be or not to be.";
    private static final int DNA_LENGTH = TARGET.length();
    private static final double MUTATION_RATE = 0.5;

    public Shakespeare()
    {
        dna = new Character[DNA_LENGTH];
        for (int i = 0; i < DNA_LENGTH; i++)
        {
            dna[i] = randomChar();
        }
    }
    
    private Shakespeare(Character[] dna)
    {
        this.dna = dna;
    }

    @Override
    public int getFitness()
    {
        int sum = 0;
        for (int i = 0; i < DNA_LENGTH; i++)
        {
            sum -= Math.abs(TARGET.charAt(i) - dna[i]);
        }
        return sum;
    }

    @Override
    public void setFitness(int fit)
    {
        
    }

    @Override
    public Character[] getDNA()
    {
        return dna.clone();
    }

    @Override
    public void mutate()
    {
        for (int i = 0; i < DNA_LENGTH; i++)
        {
            if (Math.random() < MUTATION_RATE)
            {
                dna[i] = randomChar();
            }
        }
    }

    @Override
    public Genetic<Character> reproduceWith(Genetic<Character> other)
    {
        Character[] newdna = new Character[DNA_LENGTH];
        for (int i = 0; i < DNA_LENGTH; i++)
        {
            if (i < DNA_LENGTH / 2)
            {
                newdna[i] = this.dna[i];
            }
            else
            {
                newdna[i] = other.getDNA()[i];
            }
        }
        return new Shakespeare(newdna);
    }

    public String toString()
    {
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < DNA_LENGTH; i++)
        {
            out.append(dna[i]);
        }
        return out.toString();
    }

    private char randomChar()
    {
        Random r = new Random();
        return (char) (r.nextInt(90) + 32);
    }
}
