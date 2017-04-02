package lindenmayer;

public class SRule
{
    private String input;
    private String[] outputs;
    private int[] probabilities;

    public SRule(String input, String[] outputs, int[] probs)
    {
        // unimplemented
    }
    
    public SRule(String input, String output1, String output2, int p1)
    {
        if (input.equals("") || output1.equals("") || p1 < 0 || p1 > 100)
        {
            throw new IllegalArgumentException("Arguments cannot be used");
        }
        this.input = input;
        outputs = new String[] {output1, output2};
        probabilities = new int[] {p1, 100};
    }

    public String apply(String string)
    {
        if (canApply(string))
        {
            int result = (int) (100*Math.random());
            for (int i = 0; i < outputs.length; i++)
            {
                if (result < probabilities[i])
                {
                    return outputs[i];
                }
            }
        }
        return string;
    }

    public boolean canApply(String string)
    {
        return string.equals(input);
    }

    public String getInput()
    {
        return input;
    }

    public String toString()
    {
        StringBuilder out = new StringBuilder("LRule: ");
        out.append("(");
        out.append(input);
        out.append(" -> ");
        out.append(probabilities[0]);
        out.append("%: ");
        out.append(outputs[0]);
        out.append(" / ");
        out.append(100 - probabilities[1]);
        out.append("%: ");
        out.append(outputs[1]);
        out.append(")");
        return out.toString();
    }
}
