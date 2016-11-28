package lindenmayer;

public class DragonCurve extends LCurve
{    
    public DragonCurve()
    {
        super(90, "FX", new LRule("X", "X+YF+"), new LRule("Y", "-FX-Y"));
        addAntRule("F", Action.DRAW);
        addAntRule("-", Action.TURNLEFT);
        addAntRule("+", Action.TURNRIGHT);
        setName("Dragon Curve");
    }
}
