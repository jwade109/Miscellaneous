package lindenmayer;

public class CrescentCurve extends LCurve
{
    public CrescentCurve()
    {
        super(90, "A", new LRule("A", "A+A+"));
        addAntRule("A", Action.DRAW);
        addAntRule("+", Action.TURNLEFT);
        addAntRule("-", Action.TURNRIGHT);
    }
}
