package lindenmayer;

import java.util.ArrayList;

public class LCurve
{
    private String name;
    private int angle;
    private String axiom;
    private ArrayList<LRule> rules;
    private ArrayList<AntRule> antRules;

    public LCurve(int angle, String axiom, LRule rule)
    {
        name = "LCurve";
        this.angle = angle;
        this.axiom = axiom;
        rules = new ArrayList<LRule>();
        rules.add(rule);
        antRules = new ArrayList<AntRule>();
    }

    public LCurve(int angle, String axiom, LRule rule1, LRule rule2)
    {
        this.angle = angle;
        rules = new ArrayList<LRule>();
        rules.add(rule1);
        rules.add(rule2);
        this.axiom = axiom;
        antRules = new ArrayList<AntRule>();
    }

    public LCurve(int angle, String axiom, ArrayList<LRule> rules)
    {
        this.angle = angle;
        this.rules = rules;
        this.axiom = axiom;
        antRules = new ArrayList<AntRule>();
    }

    public void addAntRule(String input, Action action)
    {
        boolean contains = false;
        for (int i = 0; i < antRules.size(); i++)
        {
            if (input.equals(antRules.get(i).getInput()))
            {
                antRules.get(i).setAction(action);
                contains = true;
            }
        }
        if (!contains)
        {
            antRules.add(new AntRule(input, action));
        }
    }
    
    public String getName()
    {
        return name;
    }
    
    protected void setName(String name)
    {
        this.name = name;
    }
    
    public Action getAction(String string)
    {
        for (int i = 0; i < antRules.size(); i++)
        {
            if (antRules.get(i) != null && antRules.get(i).canApply(string))
            {
                return antRules.get(i).getAction();
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
    
    public int[] lengths(int maxOrder)
    {
        int[] lengths = new int[maxOrder + 1];
        for (int i = 0; i <= maxOrder; i++)
        {
            lengths[i] = length(i);
        }
        return lengths;
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
        for (int i = 0; i < antRules.size(); i++)
        {
            if (antRules.get(i) != null)
            {
                out.append(antRules.get(i).toString());
                out.append(", ");
            }
        }
        out.delete(out.length() - 2, out.length());
        return out.toString();
    }
}
