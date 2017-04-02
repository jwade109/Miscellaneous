package lindenmayer;

public class LauraCurve extends LCurve
{
    public LauraCurve()
    {
        super(120, "A", new LRule("A", "A+A-A"));
        addAntRule("A", Action.DRAW);
        addAntRule("+", Action.TURNLEFT);
        addAntRule("-", Action.TURNRIGHT);
    }
}