package lindenmayer;

public class AntRule
{
    String input;
    Action action;
    
    public AntRule(String input, Action action)
    {
        this.input = input;
        this.action = action;
    }
    
    public String getInput()
    {
        return input;
    }
    
    public Action getAction()
    {
        return action;
    }
    
    public void setAction(Action action)
    {
        this.action = action;
    }
    
    public boolean canApply(String instruction)
    {
        return instruction.equals(input);
    }
    
    public String toString()
    {
        StringBuilder out = new StringBuilder("AntRule: ");
        out.append("(");
        out.append(input);
        out.append(" -> ");
        out.append(action);
        out.append(")");
        return out.toString();
    }
}
