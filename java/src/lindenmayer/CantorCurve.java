package lindenmayer;

public class CantorCurve extends LCurve
{
    public CantorCurve()
    {
        super(0, "A", new LRule("A", "ABA"), new LRule("B", "BBB"));
        addAntRule("A", Action.DRAW);
        addAntRule("B", Action.FORWARD);
    }
}
