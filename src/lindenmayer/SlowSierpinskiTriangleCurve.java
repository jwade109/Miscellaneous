package lindenmayer;

public class SlowSierpinskiTriangleCurve extends LCurve
{
    /**
     * Makes a Sierpinski triangle in exponentially-longer time,
     * so basically super slow.
     */
    public SlowSierpinskiTriangleCurve()
    {
        super(90, "X>YL>L>L>",
            new LRule("X", "RR>L>LX>YL>R"),
            new LRule("Y", "R>LX>YL>L>RR"));
        addAntRule("L", Action.TURNLEFT);
        addAntRule("R", Action.TURNRIGHT);
        addAntRule(">", Action.DRAW);
        setName("Sierpinski Curve");
    }
}
