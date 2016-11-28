package lindenmayer;

import java.awt.Color;

public class CurveAnt
{
    private int x;
    private int y;
    private Direction dir;
    private LCurve curve;
    private String sequence;
    private int step;
    private boolean done;
    private Color color;
    private String splash;

    public CurveAnt(int x, int y, LCurve curve, int order, Direction dir, Color color)
    {
        this.curve = curve;
        sequence = curve.generate(order);
        this.x = x;
        this.y = y;
        this.dir = dir;
        step = 0;
        done = false;
        this.color = color;
        splash = "NEW ANT FACING " + dir + " at (" + x + ", " + y + ")";
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
            turnLeft();
            turnLeft();
            move();
            turnLeft();
            turnLeft();
            splash = "MOVED BACKWARD";
        }
        else if (action == Action.TURNLEFT)
        {
            turnLeft();
            splash = "TURNED LEFT";
        }
        else if (action == Action.TURNRIGHT)
        {
            turnRight();
            splash = "TURNED RIGHT";
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

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public Direction getDir()
    {
        return dir;
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

    public void setDir(Direction dir)
    {
        this.dir = dir;
    }
    
    public void shift(int dx, int dy)
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
        switch (dir)
        {
            case NORTH:
            {
                dir = Direction.EAST;
                break;
            }
            case EAST:
            {
                dir = Direction.SOUTH;
                break;
            }
            case SOUTH:
            {
                dir = Direction.WEST;
                break;
            }
            case WEST:
            {
                dir = Direction.NORTH;
            }
                
        }
    }
    
    public void turnLeft()
    {
        switch (dir)
        {
            case NORTH:
            {
                dir = Direction.WEST;
                break;
            }
            case EAST:
            {
                dir = Direction.NORTH;
                break;
            }
            case SOUTH:
            {
                dir = Direction.EAST;
                break;
            }
            case WEST:
            {
                dir = Direction.SOUTH;
            }
        }
    }

    public Line move()
    {
        int xi = x;
        int yi = y;

        switch (dir)
        {
            case NORTH:
            {
                y--;
                break;
            }
            case EAST:
            {
                x++;
                break;
            }
            case SOUTH:
            {
                y++;
                break;
            }
            case WEST:
            {
                x--;
                break;
            }
        }

        int xf = x;
        int yf = y;

        return new Line(new int[]{xi, yi, xf, yf}, color);
    }

}
