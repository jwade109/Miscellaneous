package lindenmayer;

public class CurveLauncher
{
    public static void main(String[] args)
    {
        CurveWindow curve = new CurveWindow(
            new BushCurve(0), 90, 50);
        curve.setErasing(false);
        curve.setVisibility(true);
        curve.start(10);
    }
}
