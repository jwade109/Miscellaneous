package lindenmayer;

public class HexagonalSierpinskiTriangle extends LCurve
{
    public HexagonalSierpinskiTriangle()
    {
        super(120, "F-G-G",
            new LRule("F", "F-G+F+G-F"), new LRule("G", "GG"));
        addAntRule("F", Action.DRAW);
        addAntRule("G", Action.DRAW);
        addAntRule("+", Action.TURNRIGHT);
        addAntRule("-", Action.TURNLEFT);
        setName("SierpinskiTriangle");
    }
    
}
