package lindenmayer;

public class SplitCurve extends LCurve
{
    public SplitCurve(int angle)
    {
        super(angle, "NAAABA", new StochasticRule("A", "A", "AC", 70), new StochasticRule("B", "LAL", "RAR", 50));
        addAntRule("N", Action.NORTH);
        addAntRule("A", Action.DRAW);
        addAntRule("C", Action.DRAW);
        addAntRule("L", Action.TURNLEFT);
        addAntRule("R", Action.TURNRIGHT);
        setName("Splitcurve");
    }
}
