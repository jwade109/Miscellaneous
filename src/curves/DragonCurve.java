package curves;

public class DragonCurve implements Curve
{
    public String generate(int order)
    {
        if (order < 2)
        {
            return "1";
        }

        String prevOrder = generate(order - 1);

        String toSwap = prevOrder.substring(prevOrder.length() / 2,
                prevOrder.length() / 2 + 1);

        if (toSwap.equals("1"))
        {
            toSwap = "0";
        }
        else if (toSwap.equals("0"))
        {
            toSwap = "1";
        }

        String end = prevOrder.substring(0, prevOrder.length() / 2) + toSwap
                + prevOrder.substring(prevOrder.length() / 2 + 1,
                        prevOrder.length());

        return prevOrder + "1" + end;
    }

    public int length(int order)
    {
        return generate(order).length();
    }
}
