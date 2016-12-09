package lindenmayer;

import java.util.ArrayList;

public class HeartCurve extends LCurve
{

    public HeartCurve()
    {
        super(20, "S", rules());
        addAntRule("D", Action.DRAW);
        addAntRule("W", Action.WEST);
        addAntRule("E", Action.EAST);
        addAntRule("+", Action.TURNRIGHT);
        addAntRule("-", Action.TURNLEFT);
        addAntRule("C", Action.DRAW);
    }
    
    private static ArrayList<SRule> rules()
    {
        ArrayList<SRule> list = new ArrayList<SRule>();
        list.add(new SRule("S", "WA", "EB", 50));
        list.add(new LRule("A", "+++DDDD++D++D++D++D++D+D"));
        list.add(new LRule("B", "---DDDD--D--D--D--D--D-D"));
        list.add(new SRule("D", "D", "DC", 95));
        return list;
    }

}
