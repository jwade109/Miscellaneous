package lindenmayer;

public interface Curve
{
    public String generate(int order);
    
    public int length(int order);
}
