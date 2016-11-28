package lindenmayer;

public class ExampleCurve implements Curve
{
    public String generate(int order)
    {
        return "1111111111111111111111111111";
    }

    public int length(int order)
    {
        return generate(order).length();
    }

    public int getAngle()
    {
        return 45;
    }

}
