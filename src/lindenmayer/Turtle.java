package lindenmayer;

import java.awt.Color;

public class Turtle
{
    private double x;
    private double y;
    private int angle;
    private LCurve curve;
    private String sequence;
    private int step;
    private boolean done;
    private Color color;
    private String splash;

    public Turtle(int x, int y, LCurve curve, int order, int angle,
            Color color)
    {
        this.curve = curve;
        sequence = curve.generate(order);
        this.x = x;
        this.y = y;
        this.angle = angle;
        step = 0;
        done = false;
        this.color = color;
        splash = "NEW ANT FACING " + angle + " at (" + x + ", " + y + ")";
    }

    public Line next()
    {
        if (step >= sequence.length())
        {
            done = true;
            splash = "DONE";
            return null;
        }

        Action action = curve.getAction(sequence.substring(step, step + 1));
        step++;

        if (action == Action.FORWARD)
        {
            move();
            splash = "MOVED FORWARD";
        }
        else if (action == Action.BACKWARD)
        {
            angle = (angle + 180) % 360;
            move();
            angle = (angle + 180) % 360;
            splash = "MOVED BACKWARD";
        }
        else if (action == Action.TURNLEFT)
        {
            turnLeft();
            splash = "TURNED LEFT - Angle = " + angle;
        }
        else if (action == Action.TURNRIGHT)
        {
            turnRight();
            splash = "TURNED RIGHT - Angle = " + angle;
        }
        else if (action == Action.DRAW)
        {
            splash = "DREW A LINE";
            return move();
        }
        else if (action == Action.WAIT)
        {
            splash = "DID NOTHING";
        }
        return null;
    }

    public int getSteps()
    {
        return sequence.length();
    }

    public boolean isDone()
    {
        return done;
    }

    public double getX()
    {
        return x;
    }

    public double getY()
    {
        return y;
    }

    public int getAngle()
    {
        return angle;
    }

    public String splash()
    {
        return splash;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public void setAngle(int angle)
    {
        this.angle = angle;
    }

    public void shift(double dx, double dy)
    {
        x += dx;
        y += dy;
    }

    public void setColor(Color color)
    {
        this.color = color;
    }

    public Color getColor()
    {
        return color;
    }

    public void turnRight()
    {
        angle = (angle - curve.getAngle()) % 360;
    }

    public void turnLeft()
    {
        angle = (angle + curve.getAngle()) % 360;
    }

    public Line move()
    {
        double xi = x;
        double yi = y;

        shift(Math.sin(Math.toRadians(angle)), Math.cos(Math.toRadians(angle)));

        double xf = x;
        double yf = y;

        return new Line(new double[] {xi, yi, xf, yf}, color);
    }

}
