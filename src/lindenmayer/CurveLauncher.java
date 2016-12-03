package lindenmayer;

public class CurveLauncher
{
    public static void main(String[] args)
    {
        CurveWindow curve = new CurveWindow(
            new FastSierpinskiTriangleCurve(), 12);
        curve.setErasing(true);
        curve.setVisibility(true);
        curve.start(0);
    }
}
