package lindenmayer;

public class HilbertCurve extends LCurve
{
    public HilbertCurve()
    {
        super(90, "A", new LRule("A", "-BF+AFA+FB-"), 
            new LRule("B", "+AF-BFB-FA+"));
        addAntRule("F", Action.DRAW);
        addAntRule("-", Action.TURNLEFT);
        addAntRule("+", Action.TURNRIGHT);
    }
}
