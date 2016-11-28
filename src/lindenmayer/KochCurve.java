package lindenmayer;

public class KochCurve extends LCurve
{
    public KochCurve()
    {
        super(90, "F", new LRule("F", "F+F-F-F+F"));
        addAntRule("F", Action.DRAW);
        addAntRule("+", Action.TURNLEFT);
        addAntRule("-", Action.TURNRIGHT);
    }
}
