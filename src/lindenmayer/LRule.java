package lindenmayer;

public class LRule
{
    private String input;
    private String output;
    
    public LRule(String input, String output)
    {
        if (input.equals("") || output.equals(""))
        {
            throw new IllegalArgumentException("Rules cannot map nothing!");
        }
        this.input = input;
        this.output = output;
    }
    
    public String apply(String string)
    {
        if (canApply(string))
        {
            return output;
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
    
    public String getOutput()
    {
        return output;
    }
    
    public String toString()
    {
        StringBuilder out = new StringBuilder("LRule: ");
        out.append("(");
        out.append(input);
        out.append(" -> ");
        out.append(output);
        out.append(")");
        return out.toString();
    }
}
