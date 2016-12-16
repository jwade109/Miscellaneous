package lindenmayer;

import java.util.ArrayList;

public class LogarithmicSpiral extends LCurve
{
    public LogarithmicSpiral()
    {
        super(60, "WA", makeRules());
        addAntRule("L", Action.TURNRIGHT);
        addAntRule("G", Action.DRAW);
        addAntRule("F", Action.DRAW);
        addAntRule("W", Action.WEST);
    }
    private static final ArrayList<SRule> makeRules()
    {
        ArrayList<SRule> rules = new ArrayList<SRule>();
        rules.add(new SRule("A", "ALF", "", 100));
        rules.add(new SRule("F", "FG", "", 100));
        rules.add(new SRule("G", "F", "", 100));
        /* Integral Curve
        rules.add(new SRule("A", ">BAC>", "", 100));
        rules.add(new SRule("B", "BL", "", 100));
        rules.add(new SRule("C", "CR", "", 100));
        */
        return rules;
    }
}
