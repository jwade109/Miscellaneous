package lindenmayer;

public class BrainCurve extends LCurve
{

    public BrainCurve()
    {
        super(8, "X",
            new LRule("X", "Y>RX>RX"),
            new LRule("Y", "Y>LY"));
        addAntRule("X", Action.DRAW);
        addAntRule("L", Action.TURNLEFT);
        addAntRule("R", Action.TURNRIGHT);
        addAntRule(">", Action.DRAW);
        setName("Brain Curve");
    }
}
