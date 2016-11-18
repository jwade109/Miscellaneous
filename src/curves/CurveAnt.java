package curves;

import java.awt.Color;

public class CurveAnt
{
    private int x;
    private int y;
    private Direction dir;
    private String sequence;
    private int step;
    private boolean done;
    private Color color;

    public CurveAnt(Curve curve, int order, Color color)
    {
        sequence = curve.generate(order);
        x = 0;
        y = 0;
        dir = Direction.NORTH;
        step = 0;
        done = false;
        this.color = color;
    }

    public Line next()
    {
        if (step == sequence.length())
        {
            done = true;
            return null;
        }
        if (sequence.substring(step, step + 1).equals("1"))
        {
            Line a = move();
            turnRight();
            step++;
            return a;
        }
        else if (sequence.substring(step, step + 1).equals("0"))
        {
            Line a = move();
            turnLeft();
            step++;
            return a;
        }
        else
        {
            System.out.println("Something broke!");
            return null;
        }
    }

    public int getSteps()
    {
        return sequence.length();
    }

    public boolean getDone()
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
