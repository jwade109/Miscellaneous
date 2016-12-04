package lindenmayer;

public class CurveLauncher
{
    public static void main(String[] args)
    {
        CurveWindow curve = new CurveWindow(
            new WilliamTestCurve(), 9);
        curve.setErasing(false);
        curve.setVisibility(true);
        curve.start(0);
    }
}
