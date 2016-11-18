package dragon;

public class KochCurve implements Curve
{
    public String generate(int order)
    {
        if (order < 2)
        {
            return "1";
        }
        
        String prev = generate(order - 1);
        
        String[] stA = new String[prev.length()];
        
        for (int i = 0; i < prev.length(); i++)
        {
            stA[i] = prev.substring(i, i + 1);
            if (stA[i].equals("1"))
            {
                stA[i] = "1001";
            }
        }
        
        String out = "";
        for (int i = 0; i < stA.length; i++)
        {
            out += stA[i];
        }
        
        return out;
    }

    public int length(int order)
    {
        return generate(order).length();
    }
}
