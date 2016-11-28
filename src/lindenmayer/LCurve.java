package lindenmayer;

import java.util.ArrayList;

public class LCurve
{
    private int angle;
    private String axiom;
    private ArrayList<LRule> rules;
    private AntRule[] antRules;

    public LCurve(int angle, String axiom, LRule rule)
    {
        this.angle = angle;
        this.axiom = axiom;
        rules = new ArrayList<LRule>();
        rules.add(rule);
        antRules = new AntRule[6];
    }

    public LCurve(int angle, String axiom, LRule rule1, LRule rule2)
    {
        this.angle = angle;
        rules = new ArrayList<LRule>();
        rules.add(rule1);
        rules.add(rule2);
        this.axiom = axiom;
        antRules = new AntRule[6];
    }

    public LCurve(int angle, String axiom, ArrayList<LRule> rules)
    {
        this.angle = angle;
        this.rules = rules;
        this.axiom = axiom;
        antRules = new AntRule[6];
    }

    public void addAntRule(String input, Action action)
    {
        int i = 5;
        switch (action)
        {
            case FORWARD:
                i = 0;
                break;
            case BACKWARD:
                i = 1;
                break;
            case TURNLEFT:
                i = 2;
                break;
            case TURNRIGHT:
                i = 3;
                break;
            case DRAW:
                i = 4;
                break;
            case WAIT:
                i = 5;
        }
        antRules[i] = new AntRule(input, action);
    }
    
    public Action getAction(String string)
    {
        for (int i = 0; i < antRules.length; i++)
        {
            if (antRules[i] != null && antRules[i].canApply(string))
            {
                return antRules[i].getAction();
            }
        }
        return Action.WAIT;
    }

    public int getAngle()
    {
        return angle;
    }

    public String generate(int order)
    {
        if (order < 1)
        {
            return axiom;
        }
        String prevOrder = generate(order - 1);
        String[] divided = new String[prevOrder.length()];
        StringBuilder out = new StringBuilder("");

        for (int i = 0; i < prevOrder.length(); i++)
        {
            divided[i] = prevOrder.substring(i, i + 1);

            boolean applied = false;
            for (int r = 0; r < rules.size() && !applied; r++)
            {
                if (rules.get(r).canApply(divided[i]))
                {
                    divided[i] = rules.get(r).apply(divided[i]);
                    applied = true;
                }
            }

            out.append(divided[i]);
        }

        return out.toString();
    }

    public int length(int order)
    {
        return generate(order).length();
    }
    
    public String toString()
    {
        StringBuilder out = new StringBuilder("LCurve: angle = ");
        out.append(angle);
        out.append(", ");
        out.append("axiom = ");
        out.append(axiom);
        out.append("\n");
        for (int i = 0; i < rules.size(); i++)
        {
            out.append(rules.get(i).toString());
            out.append(", ");
        }
        out.append("\n");
        for (int i = 0; i < antRules.length; i++)
        {
            if (antRules[i] != null)
            {
                out.append(antRules[i].toString());
                out.append(", ");
            }
        }
        out.delete(out.length() - 2, out.length());
        return out.toString();
    }
}
