package snyder;

public class SnyderTriangle
{
    public SnyderTriangle()
    {
        
    }
    
    public String generate(String seed, int line)
    {
        String out = seed + "\n";
        for (int i = 0; i < line; i++)
        {
            seed = nextLine(seed);
            out += seed + "\n";
        }
        return out;
    }
    
    private String nextLine(String line)
    {
        String nextLine = "";
        String current = line.substring(0, 1);
        int count = 0;
        
        for (int i = 0; i < line.length(); i++)
        {
            if (line.substring(i, i + 1).equals(current))
            {
                count++;
            }
            else
            {
                nextLine += count + current;
                current = line.substring(i, i + 1);
                count = 1;
            }
        }
        nextLine += count + current;
        
        return nextLine;
    }
    
    public String length(String seed, int line)
    {
        String currentLine = seed;
        String out = "";
        for (int i = 0; i < line; i++)
        {
            int size = currentLine.length();
            currentLine = nextLine(currentLine);
            out += size + "\n";
        }
        return out;
    }
}
