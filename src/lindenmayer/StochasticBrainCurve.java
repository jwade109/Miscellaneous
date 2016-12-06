package lindenmayer;

public class StochasticBrainCurve extends LCurve
{
    public StochasticBrainCurve()
    {
        super(8, "X",
            new StochasticRule("X", "Y>RX>RX", "X", 50),
            new StochasticRule("Y", "Y>LY", "Y", 50));
        addAntRule("X", Action.DRAW);
        addAntRule("L", Action.TURNLEFT);
        addAntRule("R", Action.TURNRIGHT);
        addAntRule(">", Action.DRAW);
        setName("Brain Curve");
    }
}