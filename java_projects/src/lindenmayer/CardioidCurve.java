package lindenmayer;

import java.util.ArrayList;

public class CardioidCurve extends LCurve
{
    public CardioidCurve()
    {
        super(1, "AB", makeRules());
        addAntRule(">", Action.DRAW);
        addAntRule("R", Action.TURNRIGHT);
        addAntRule("L", Action.TURNLEFT);
        addAntRule("S", Action.SOUTH);
    }
    
    private static final ArrayList<SRule> makeRules()
    {
        ArrayList<SRule> rules = new ArrayList<SRule>();
        
        rules.add(new SRule("A", ">BA", "", 100));
        rules.add(new SRule("B", "LB", "", 100));
        /* Integral Curve
        rules.add(new SRule("A", ">BAC>", "", 100));
        rules.add(new SRule("B", "BL", "", 100));
        rules.add(new SRule("C", "CR", "", 100));
        */
        return rules;
    }
}
