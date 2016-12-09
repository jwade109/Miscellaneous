package lindenmayer;

public class FastSierpinskiTriangleCurve extends LCurve
{
    /**
     * Makes a Sierpinski triangle pretty fast.
     */
    public FastSierpinskiTriangleCurve()
    {
        super(5, "X",
            new LRule("X", "Y>RX>RX"),
            new LRule("Y", "Y>LY"));
        addAntRule("X", Action.DRAW);
        addAntRule("L", Action.TURNLEFT);
        addAntRule("R", Action.TURNRIGHT);
        addAntRule(">", Action.DRAW);
        setName("Sierpinski Triangle");
        
        /*
        super(5, "X",
            new LRule("X", "Y>RX>RX"),
            new LRule("Y", "Y>LY"));
        addAntRule("X", Action.DRAW);
        addAntRule("L", Action.TURNLEFT);
        addAntRule("R", Action.TURNRIGHT);
        addAntRule(">", Action.DRAW);
        setName("Brain Curve");
        */
        
        /*
        super(90, "XYXYXYXY",
            new LRule("X", "X>R>Y>LX"),
            new LRule("Y", "Y>R>X>LY"));
        addAntRule("L", Action.TURNLEFT);
        addAntRule("R", Action.TURNRIGHT);
        addAntRule(">", Action.DRAW);
        setName("Saw Curve");
        */
        
        /*
         * super(90, "XXXX",
            new LRule("X", "X>L>Y>LX"),
            new LRule("Y", "Y>R>X>RY"));
        addAntRule("X", Action.TURNLEFT);
        addAntRule("L", Action.TURNLEFT);
        addAntRule("R", Action.TURNRIGHT);
        addAntRule(">", Action.DRAW);
        setName("Frisbee Curve");
         */
        
        // One method.
        String o10 = ">R>R>R>R";
        String o1 = ">R>L>L>L>R>L>L>R>L>L>L>R";
        String o2 = ">R>L>R>R>L>L>L>R>R>L>L>L>R>L>R>L>R>L"
                  + ">L>R>L>R>L>R>L>L>L>R>R>L>L>L>R>R>L>R";
        
        // The other direction.
        String p0 = ">L>L>L>L";
        String p1 = ">R>L>R>R>R>L>L>R>R>R>L>R";
        String p2 = ">R>L>R>L>R>L>R>R>R>L>L>R>R>R>L>L>R>L"
                  + ">L>R>L>L>R>R>R>L>L>R>R>R>L>R>L>R>L>R";
        
        String ex0 = "R  R  R  R  ";
        String ex1 = "RLRRRLLRRRLR";
        
        
        for (int i = 0; i < ex1.length(); i++)
        {
            System.out.println(ex0);
            System.out.println(ex1);
            String prefix = ex1.substring(0, 1);
            ex1 = ex1.substring(1, ex1.length()) + prefix;
        }
    }
}
