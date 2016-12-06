package lindenmayer;

public class StochasticCrescentCurve extends LCurve
{
    public StochasticCrescentCurve(int angle)
    {
        super(angle, "A", new StochasticRule("A", "A+A+", "A+", 50));
        addAntRule("A", Action.DRAW);
        addAntRule("+", Action.TURNLEFT);
        addAntRule("-", Action.TURNRIGHT);
        setName("Crescent Curve");
    }

}
