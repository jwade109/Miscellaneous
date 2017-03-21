package lindenmayer;

public class SquareCurve extends LCurve
{
    public SquareCurve()
    {
        super(90, "F", new LRule("F", "A-FB-FA-F"), new LRule("B", "AA"));
        addAntRule("F", Action.WAIT);
        addAntRule("-", Action.TURNLEFT);
        addAntRule("A", Action.DRAW);
        addAntRule("C", Action.FORWARD);
        setName("Square Curve");
    }
}
