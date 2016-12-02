package lindenmayer;

public class SierpinskiCurve extends LCurve
{
    /**
     * Makes a Sierpinski triangle in exponential time,
     * so basically super slow.
     */
    public SierpinskiCurve()
    {
        super(60, "X>YL>L>L>",
            new LRule("X", "RR>L>LX>YL>R"),
            new LRule("Y", "R>LX>YL>L>RR"));
        addAntRule("L", Action.TURNLEFT);
        addAntRule("R", Action.TURNRIGHT);
        addAntRule(">", Action.DRAW);
        setName("Sierpinski Curve");
    }
}
