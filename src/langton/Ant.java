package langton;

import curves.Direction;

public class Ant
{
    private int x;
    private int y;
    private Direction dir;

    public Ant(int x, int y, Direction dir)
    {
        this.x = x;
        this.y = y;
        this.dir = dir;
    }

    public void move()
    {
        switch (dir)
        {
            case NORTH:
                y--;
                break;
            case EAST:
                x++;
                break;
            case SOUTH:
                y++;
                break;
            case WEST:
                x--;
                break;
        }
    }

    public void reverse()
    {
        switch (dir)
        {
            case NORTH:
                y++;
                break;
            case EAST:
                x--;
                break;
            case SOUTH:
                y--;
                break;
            case WEST:
                x++;
        }
    }

    // turn(false) turns right, while turn (true) will turn left
    public void turnRight()
    {
        switch (dir)
        {
            case NORTH:
                dir = Direction.EAST;
                break;
            case EAST:
                dir = Direction.SOUTH;
                break;
            case SOUTH:
                dir = Direction.WEST;
                break;
            case WEST:
                dir = Direction.NORTH;
                break;
        }
    }

    public void turnLeft()
    {
        switch (dir)
        {
            case NORTH:
                dir = Direction.WEST;
                break;
            case EAST:
                dir = Direction.NORTH;
                break;
            case SOUTH:
                dir = Direction.EAST;
                break;
            case WEST:
                dir = Direction.SOUTH;
                break;
        }
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public Direction getDir()
    {
        return dir;
    }

    public void setDir(Direction dir)
    {
        this.dir = dir;
    }

    public String toString()
    {
        return x + " " + y + " " + dir;
    }

}
