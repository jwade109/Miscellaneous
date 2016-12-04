package lindenmayer;

import java.awt.Color;

public class Line
{
    private double[] a;
    private Color color;
    
    public Line(double[] ds, Color color)
    {
        this.a = ds;
        this.color = color;
    }
    
    public double[] getLoc()
    {
        return a;
    }
    
    public Color getColor()
    {
        return color;
    }
    
    public boolean equals(Object other)
    {
        if (other == null || other.getClass() != getClass())
        {
            return false;
        }
        if (other == this)
        {
            return true;
        }
        
        Line otherLine = (Line) other;
        
        double[] oa = otherLine.getLoc();
        
        if (oa[0] == a[0] && oa[1] == a[1] && oa[2] == a[2] && oa[3] == a[3])
        {
            return true;
        }
        if (oa[0] == a[2] && oa[1] == a[3] && oa[2] == a[0] && oa[3] == a[1])
        {
            return true;
        }
        
        return false;
    }
    
    public String toString()
    {
        return a[0] + " " + a[1] + " " + a[2] + " " + a[3] + " " + color.toString();
    }
    
}
