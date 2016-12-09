package lindenmayer;

public class BrainCurve extends LCurve
{

    public BrainCurve()
    {
        super(8, "X",
            new StochasticRule("X", "Y>RX>RX", "X>LY>LY", 50),
            new LRule("Y", "Y>LY"));
        addAntRule("X", Action.DRAW);
        addAntRule("L", Action.TURNLEFT);
        addAntRule("R", Action.TURNRIGHT);
        addAntRule(">", Action.DRAW);
        setName("Brain Curve");
    }
}
