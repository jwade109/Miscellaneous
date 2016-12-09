package lindenmayer;

public class CurveLauncher
{
    public static void main(String[] args)
    {
        System.out.println(CurveReader.findCurve("Bush").toString());
        
        
        // CurveWindow curve = new CurveWindow(
        // CurveReader.findCurve("Bush"), 80, 120);
        // curve.setErasing(false);
        // curve.setVisibility(false);
        // curve.setDrawTurtles(true);
        // curve.setDifferentiate(true);
        // curve.start(10);
    }
}
