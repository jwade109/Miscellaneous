package lindenmayer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class CurveReader
{
    public static LCurve findCurve(String searchTerm)
    {
        Scanner scanner = null;

        try
        {
            scanner = new Scanner(new File("curves"));
        }
        catch (FileNotFoundException e)
        {
            System.out.println("Could not bind to curves.txt.");
        }

        for (int i = 0; i < 5; i++)
        {
            scanner.nextLine();
        }

        if (scanner != null)
        {
            while (scanner.hasNextLine())
            {
                LCurve curve = parse(scanner.nextLine(), searchTerm);
                if (curve != null)
                {
                    return curve;
                }
            }

            scanner.close();
        }
        return null;
    }

    private static LCurve parse(String nextLine, String search)
    {
        LCurve curve = new LCurve();
        Scanner line = new Scanner(nextLine);
        line.useDelimiter("\\s*;\\s*");

        if (line.hasNext())
        {
            String name = line.next();
            if (name.contains(search) || search.contains(name))
            {
                curve.setName(name);
                System.out.println(line.nextInt() + "cheese");
            }
            line.close();
        }
        return curve;
    }
}
