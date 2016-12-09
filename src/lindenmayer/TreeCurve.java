package lindenmayer;

public class TreeCurve extends LCurve
{
    public TreeCurve(int angle)
    {
        super(angle, "NAB", new SRule("A", "AB+++", "ABD", 3),
                new SRule("B", "AB---", "ABD", 3));
        addAntRule("N", Action.NORTH);
        addAntRule("D", Action.DRAW);
        addAntRule("+", Action.TURNLEFT);
        addAntRule("-", Action.TURNRIGHT);
    }
}
