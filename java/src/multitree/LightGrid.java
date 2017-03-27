package tree;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Arrays;

import drawable.DrawWindow;
import drawable.Drawable;

public class LightGrid implements Drawable
{
    public static void main(String[] args)
    {
        DrawWindow win = new DrawWindow(1200, 1800);
        win.add(new LightGrid(15));
        win.background = Color.BLACK;
        win.draw();
    }

    private final static int scl = 50;
    private int width;
    private ArrayList<Light> lights;

    public LightGrid(int width)
    {
        this.width = width;
        lights = new ArrayList<Light>();

        while (!isFull())
        {
            int rLength = (int) (Math.random() * 2 * width / 3 + 1);
            int rX = (int) (Math.random() * width);
            int rY = (int) (Math.random() * width);
            Dir rDir = Dir.values()[(int) (Math.random() * 4)];

            Point randomPoint = new Point(rX, rY);
            Light generatedLight = new Light(randomPoint, rLength, rDir);
            addLight(generatedLight);
        }
    }

    private boolean addLight(Light newLight)
    {
        for (Point newPoint : newLight.points)
        {
            for (Light existingLight : lights)
            {
                for (Point oldPoint : existingLight.points)
                {
                    if (oldPoint.equals(newPoint))
                    {
                        return false;
                    }
                }
            }
        }
        lights.add(newLight);
        return true;
    }

    private boolean isFull()
    {
        ArrayList<Point> points = new ArrayList<Point>();
        for (Light l : lights)
        {
            for (Point p : l.points)
            {
                if (!points.contains(p) && p.x < width && p.y < width && p.x > -1 && p.y > -1)
                {
                    points.add(p);
                }
            }
        }
        return points.size() == width * width;
    }

    public String toString()
    {
        return lights.toString();
    }

    @Override
    public void draw(Graphics2D g)
    {
        g.translate(-width * scl / 2, -width * scl / 2);
        for (Light l : lights)
        {
            if (l.points.length > 1)
            {
                l.draw(g);
            }
        }
    }

    private class Light implements Drawable
    {
        public final Point[] points;

        public Light(Point start, int size, Dir dir)
        {
            points = new Point[size];
            points[0] = start;
            for (int i = 1; i < size; i++)
            {
                points[i] = points[i - 1].get(dir);
            }
        }

        public String toString()
        {
            return Arrays.toString(points);
        }

        @Override
        public void draw(Graphics2D g)
        {
            g.setColor(Color.WHITE);
            Point start = points[0];
            Point end = points[points.length - 1];
            int shake = 6;
            int sxi = (int) (Math.random() * shake - shake / 2);
            int syi = (int) (Math.random() * shake - shake / 2);
            int sxf = (int) (Math.random() * shake - shake / 2);
            int syf = (int) (Math.random() * shake - shake / 2);
            for (int i = 0; i < Math.random() * 2; i++)
            {
                int randomRadius = (int) (Math.random() * 6 * scl);
                g.setStroke(new BasicStroke(randomRadius / scl));
                g.drawOval(start.x * scl - randomRadius / 2, start.y * scl - randomRadius / 2, randomRadius,
                        randomRadius);
            }
            g.setColor(Color.WHITE);
            g.setStroke(new BasicStroke(Point.radius));
            g.drawLine(sxi + start.x * scl, syi + start.y * scl, sxf + end.x * scl, syf + end.y * scl);
        }
    }

    private enum Dir
    {
        NORTH, EAST, SOUTH, WEST;
    }

    private class Point implements Drawable
    {
        public final int x;
        public final int y;
        public static final int radius = 8 * scl / 10;

        public Point(int x, int y)
        {
            this.x = x;
            this.y = y;
        }

        public Point get(Dir dir)
        {
            switch (dir)
            {
                case NORTH:
                    return new Point(x, y + 1);
                case EAST:
                    return new Point(x + 1, y);
                case SOUTH:
                    return new Point(x, y - 1);
                case WEST:
                    return new Point(x - 1, y);
            }
            throw new IllegalArgumentException();
        }

        public String toString()
        {
            return "(" + x + ", " + y + ")";
        }

        public boolean equals(Object other)
        {
            if (other == null || other.getClass() != this.getClass())
            {
                return false;
            }
            if (other == this)
            {
                return true;
            }
            Point otherPoint = (Point) other;
            return otherPoint.x == x && otherPoint.y == y;
        }

        @Override
        public void draw(Graphics2D g)
        {
            g.setColor(Color.WHITE);
            g.fillOval(x * scl - radius / 2, y * scl - radius / 2, radius, radius);
        }
    }
}
