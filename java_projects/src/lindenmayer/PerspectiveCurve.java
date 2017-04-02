package lindenmayer;

public class PerspectiveCurve extends LCurve
{
    /**
     * Makes a Strange perspective shape that's kinda cool
     */
    public PerspectiveCurve()
    {
        super(60, "X>YR>L>L>R", 
            new LRule("X", ">L>RX>YL>R"),
            new LRule("Y", ">R>LY>XL>L"));
        addAntRule("L", Action.TURNLEFT);
        addAntRule("R", Action.TURNRIGHT);
        addAntRule(">", Action.DRAW);
        setName("PerspectiveCurve");
    }
}
