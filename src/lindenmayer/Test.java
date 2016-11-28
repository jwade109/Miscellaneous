package lindenmayer;

import java.awt.Color;

public class Test
{
    public static void main(String[] args)
    {
        int order = 6;
        LCurve curve = new DragonCurve();
        CurveAnt ant = new CurveAnt(0, 0, curve, order, Direction.EAST,
                Color.BLACK);
        System.out.println(curve.toString());
        System.out.println(curve.generate(order));
        while (!ant.isDone())
        {
            System.out.println(ant.splash());
            ant.next();
        }
        System.out.println(ant.splash());
    }
}