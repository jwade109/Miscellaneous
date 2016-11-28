package lindenmayer;

public class SerpinskiArrowheadCurve extends LCurve
{
    public SerpinskiArrowheadCurve()
    {
        super(60, "A", new LRule("A", "+B-A-B+"), new LRule("B", "-A+B+A-"));
    }
}
