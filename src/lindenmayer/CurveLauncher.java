package lindenmayer;

public class CurveLauncher
{
    public static void main(String[] args)
    {
        CurveWindow curve = new CurveWindow(
            new BushCurve(90), 80, 120);
        curve.setErasing(false);
        curve.setVisibility(true);
        curve.setDrawTurtles(false);
        curve.setDifferentiate(false);
        curve.start(10);
    }
}
