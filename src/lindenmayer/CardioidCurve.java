package lindenmayer;

import java.util.ArrayList;

public class CardioidCurve extends LCurve
{
    public CardioidCurve()
    {
        super(1, "A", makeRules());
        addAntRule(">", Action.DRAW);
        addAntRule("R", Action.TURNRIGHT);
        addAntRule("L", Action.TURNLEFT);
    }
    
    private static final ArrayList<SRule> makeRules()
    {
        ArrayList<SRule> rules = new ArrayList<SRule>();
        rules.add(new SRule("A", ">BAC>", "", 100));
        rules.add(new SRule("B", "BL", "", 100));
        rules.add(new SRule("C", "CR", "", 100));
        return rules;
    }
}
