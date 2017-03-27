package lindenmayer;

public class WilliamTestCurve extends LCurve
{
    public WilliamTestCurve()
    {
        super(45, "XL",
            new LRule("X", "YXR>Y"),
            new LRule("Y", "Y>LX>Y>RX"));
        addAntRule("X", Action.DRAW);
        addAntRule("Y", Action.DRAW);
        addAntRule("L", Action.TURNLEFT);
        addAntRule("R", Action.TURNRIGHT);
        addAntRule(">", Action.DRAW);
        setName("Test Curve");
    }
    /*
    public WilliamTestCurve()
    {
        super(45, "XL",
            new LRule("X", "YXR>Y"),
            new LRule("Y", "Y>LX>Y>RX"));
        addAntRule("X", Action.DRAW);
        addAntRule("Y", Action.DRAW);
        addAntRule("L", Action.TURNLEFT);
        addAntRule("R", Action.TURNRIGHT);
        addAntRule(">", Action.DRAW);
        setName("Test Curve");
    }
    */
}
