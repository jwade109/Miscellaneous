package lindenmayer;

public class BushCurve extends LCurve
{
    public BushCurve(int angle)
    {
        super(angle, "NA", new StochasticRule("A", "ABBBBB+", "AB-", 40));
        addAntRule("B", Action.DRAW);
        addAntRule("+", Action.TURNRIGHT);
        addAntRule("-", Action.TURNLEFT);
        addAntRule("N", Action.NORTH);
    }
}