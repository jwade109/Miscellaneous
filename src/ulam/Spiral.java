package ulam;

import chunk.ChunkArray;
import langton.Ant;
import lindenmayer.Direction;

public class Spiral
{
    public ChunkArray<Boolean> getUlamSpiral(int maxWidth)
    {
        Ant ant = new Ant(0, 0, Direction.EAST);
        int count = 1;
        ChunkArray<Boolean> booleans = new ChunkArray<Boolean>();

        for (int width = 1; width < maxWidth; width++)
        {
            for (int i = 0; i < 2; i++)
            {
                for (int j = 0; j < width; j++)
                {
                    ant.move();
                    count++;
                    booleans.setEntry(isPrime(count), ant.getX(), ant.getY());
                }
                ant.turnLeft();
            }
        }
        
        return booleans;
    }

    private boolean isPrime(int n)
    {
        if (n == 2)
        {
            return true;
        }
        if (n % 2 == 0)
        {
            return false;
        }
        for (int i = 3; i * i <= n; i += 2)
        {
            if (n % i == 0)
            {
                return false;
            }
        }
        return true;
    }
}
