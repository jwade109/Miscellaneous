package lindenmayer;

import java.util.ArrayList;

public class LCurve
{
    private String name;
    private int angle;
    private String axiom;
    private ArrayList<SRule> rules;
    private ArrayList<AntRule> antRules;

    public LCurve()
    {
        this(1, "A", new LRule("A", "B"));
    }
    
    /**
     * Constructs a new LCurve of a specified turning angle, axiom, and SRule.
     * 
     * @param angle The turning angle for this Curve.
     * @param axiom The starting String.
     * @param rule A rule to be applied recursively to the axiom.
     */
    public LCurve(int angle, String axiom, SRule rule)
    {
        rules = new ArrayList<SRule>();
        rules.add(rule);
        construct(angle, axiom, rules);
    }

    /**
     * Constructs a new LCurve of a specified turning angle, axiom, and SRules.
     * 
     * @param angle The turning angle.
     * @param axiom The starting String.
     * @param rule1 The first SRule.
     * @param rule2 The second SRule.
     */
    public LCurve(int angle, String axiom, SRule rule1, SRule rule2)
    {
        rules = new ArrayList<SRule>();
        rules.add(rule1);
        rules.add(rule2);
        construct(angle, axiom, rules);
    }

    /**
     * Constructs a new LCurve given a turning angle, axiom, and List of SRules.
     * 
     * @param angle The turning angle.
     * @param axiom The starting String.
     * @param rules An ArrayList of SRules.
     */
    public LCurve(int angle, String axiom, ArrayList<SRule> rules)
    {
        construct(angle, axiom, rules);
    }

    /**
     * A helper method that takes angle, axiom, and an ArrayList of SRules and
     * initializes this object.
     * 
     * @param angle Turning angle.
     * @param axiom Axiom.
     * @param rules An ArrayList of SRules.
     */
    private void construct(int angle, String axiom, ArrayList<SRule> rules)
    {
        name = "LCurve";
        this.angle = angle;
        this.rules = rules;
        this.axiom = axiom;
        antRules = new ArrayList<AntRule>();
    }

    /**
     * Adds an AntRule to this LCurve.
     * 
     * @param input The String input of this AntRule.
     * @param action The Ant Action to take.
     */
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

    /**
     * Gets the name of this LCurve.
     * 
     * @return This curve's name.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets this curve's name.
     * 
     * @param name 
     */
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
    
    public void setAngle(int angle)
    {
        this.angle = angle;
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
