package langton;

import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JPanel;

import curves.Direction;

public class Main
{

    private DrawPanel p;
    private JFrame f;
    private Board board;
    private Ant ant;
    private int frameWidth = 1300;
    private int frameHeight = 1300;
    private int cellSize = 20;
    private int tick = 1; // milliseconds: 20 ms = 50 fps
    private int k = 0;
    private int steps = 12000;
    private int[] origin = { 0, 0 };
    private int[] newTranslate = { frameWidth/2 , frameHeight/2 };
    private int[] translate = { 0, 0 };
    private int[] mousePos = { 0, 0 };
    private boolean moving = false;
    private int dir = 0;

    public static void main(String[] args)
    {
        Main m = new Main();
        m.start();
    }

    public void start()
    {

        board = new Board();
        ant = new Ant(0, 0, Direction.NORTH);

        f = new JFrame("Langton's Ant");

        f.setSize(frameWidth, frameHeight + 45);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        f.addMouseListener(new MouseListener()
        {

            public void mouseClicked(MouseEvent e)
            {
            }

            public void mouseEntered(MouseEvent e)
            {
            }

            public void mouseExited(MouseEvent e)
            {
            }

            public void mousePressed(MouseEvent e)
            {
                moving = true;
                origin[0] = mousePos[0];
                origin[1] = mousePos[1];
            }

            public void mouseReleased(MouseEvent e)
            {
                moving = false;
                translate[0] += newTranslate[0];
                translate[1] += newTranslate[1];
                newTranslate[0] = 0;
                newTranslate[1] = 0;
            }

        });

        f.addMouseWheelListener(new MouseWheelListener()
        {

            public void mouseWheelMoved(MouseWheelEvent e)
            {
                cellSize -= e.getWheelRotation();
            }

        });

        f.setVisible(true);

        p = new DrawPanel();

        f.getContentPane().add(p);

        try
        {
            Thread.sleep(tick);
        }
        catch (InterruptedException e1)
        {
            e1.printStackTrace();
        }

        int t = 0;

        while (true)
        {

            updateFrame(f);

            p.repaint();

            if (moving)
            {
                newTranslate[0] = mousePos[0] - origin[0];
                newTranslate[1] = mousePos[1] - origin[1];
            }

            if (Math.floorMod(t, tick) == 0)
            {
                if (k == 0)
                    dir = 1;
                else if (k == steps)
                    dir = -1;

                if (dir == 1)
                {

                    if (board.getCellState(ant.getX(), ant.getY()))
                    {
                        ant.turnLeft();
                    }
                    else
                    {
                        ant.turnRight();
                    }
                    board.invertCell(ant.getX(), ant.getY(), 1);
                    ant.move();
                }
                else
                {
                    ant.reverse();
                    board.invertCell(ant.getX(), ant.getY(), -1);
                    if (board.getCellState(ant.getX(), ant.getY()))
                    {
                        ant.turnRight();
                    }
                    else
                    {
                        ant.turnRight();
                    }
                }

                k += dir;

            }

            t++;

            try
            {
                Thread.sleep(1);
            }
            catch (InterruptedException e1)
            {
                e1.printStackTrace();
            }

        }

    }

    private void updateFrame(JFrame f)
    {
        if (getTrueWidth(f) > (getTrueHeight(f)))
            frameHeight = getTrueHeight(f);
        else
            frameWidth = f.getWidth();

        frameWidth = getTrueWidth(f);
        frameHeight = getTrueHeight(f);
    }

    private int[] getMousePos()
    {
        int x = MouseInfo.getPointerInfo().getLocation().x - f.getX() - 13;
        int y = MouseInfo.getPointerInfo().getLocation().y - f.getY() - 13;
        int[] pos = { x, y };
        return pos;
    }

    private double[] getMousePercent()
    {
        int[] mousePos = getMousePos();
        double[] percents = { 0, 0 };
        percents[0] = (double) mousePos[0] / frameWidth;
        percents[1] = (double) mousePos[1] / frameHeight;

        for (int i = 0; i < 2; i++)
        {
            if (percents[i] < 0)
                percents[i] = 0;
            if (percents[i] > 1)
                percents[i] = 1;
        }

        return percents;
    }

    private int getTrueHeight(JFrame f)
    {
        return f.getHeight() - 45;
    }

    private int getTrueWidth(JFrame f)
    {
        return f.getWidth();
    }

    private class DrawPanel extends JPanel
    {
        private static final long serialVersionUID = 1L;

        public void paintComponent(Graphics g)
        {
            int buffer = 50;
            int edge = 25;
            int shadowOffset = 8;

            int antXLoc = (buffer + cellSize * ant.getX() + translate[0]
                    + newTranslate[0]);
            int antYLoc = (buffer + cellSize * ant.getY() + translate[1]
                    + newTranslate[1]);

            g.setColor(new Color(230, 230, 230));
            g.fillRect(0, 0, 2 * frameWidth, 2 * frameHeight);

            g.setColor(Color.WHITE);
            g.fillRect(0, 0, 2 * frameWidth, 2 * frameHeight);

            for (int i = -100; i < 100; i++)
            {
                for (int j = -100; j < 100; j++)
                {
                    g.setColor(new Color(255 - board.getCellCount(i, j) * 6,
                            255 - board.getCellCount(i, j) * 6,
                            255 - board.getCellCount(i, j) * 6));
                    g.fillRect(
                            buffer + cellSize * i + translate[0]
                                    + newTranslate[0],
                            buffer + cellSize * j + translate[1]
                                    + newTranslate[1],
                            cellSize, cellSize);

                    if (board.getCellState(i, j))
                        g.setColor(Color.BLACK);
                    else
                        g.setColor(Color.WHITE);
                    g.fillRect(
                            buffer + cellSize * i + translate[0]
                                    + newTranslate[0] + cellSize / 4,
                            buffer + cellSize * j + translate[1]
                                    + newTranslate[1] + cellSize / 4,
                            cellSize / 2, cellSize / 2);
                }
            }

            int[] xpoints = new int[5];
            int[] ypoints = new int[5];

            switch (ant.getDir())
            {

                case NORTH:
                    xpoints = new int[] { antXLoc, antXLoc + cellSize / 2,
                            antXLoc + cellSize };
                    ypoints = new int[] { antYLoc + cellSize, antYLoc,
                            antYLoc + cellSize };
                    break;
                case EAST:
                    xpoints = new int[] { antXLoc, antXLoc + cellSize,
                            antXLoc };
                    ypoints = new int[] { antYLoc, antYLoc + cellSize / 2,
                            antYLoc + cellSize };
                    break;
                case SOUTH:
                    xpoints = new int[] { antXLoc, antXLoc + cellSize / 2,
                            antXLoc + cellSize };
                    ypoints = new int[] { antYLoc, antYLoc + cellSize,
                            antYLoc };
                    break;
                case WEST:
                    xpoints = new int[] { antXLoc + cellSize, antXLoc,
                            antXLoc + cellSize };
                    ypoints = new int[] { antYLoc, antYLoc + cellSize / 2,
                            antYLoc + cellSize };
                    break;
            }

            g.setColor(Color.RED);
            g.fillPolygon(xpoints, ypoints, 3);

            g.setColor(new Color(230, 230, 230));
            g.fillRect(0, 0, buffer, 2 * frameHeight);
            g.fillRect(0, 0, 2 * frameHeight, buffer);
            g.fillRect(frameWidth - buffer - edge, 0, frameWidth,
                    2 * frameHeight);
            g.fillRect(0, frameHeight - buffer - edge, 2 * frameWidth,
                    frameHeight);

            g.setColor(Color.GRAY);
            g.fillRect(frameWidth - buffer - edge, buffer + shadowOffset,
                    shadowOffset, frameHeight - 2 * buffer - edge);
            g.fillRect(buffer + shadowOffset, frameHeight - buffer - edge,
                    frameWidth - 2 * buffer - edge, shadowOffset);

            g.setColor(Color.BLACK);
            g.drawRect(buffer, buffer, frameWidth - 2 * buffer - edge,
                    frameHeight - 2 * buffer - edge);

            mousePos = getMousePos();
            double[] percents = getMousePercent();
            g.setColor(Color.GRAY);
            g.drawString(
                    "Step: " + k + " of " + steps + " Mouse: " + mousePos[0]
                            + " " + mousePos[1] + " Translate: "
                            + newTranslate[0] + " " + newTranslate[1] + " "
                            + translate[0] + " " + translate[1],
                    buffer + 10, buffer + 20);
            g.drawString("Window: " + frameWidth + " " + frameHeight,
                    buffer + 10, buffer + 40);
            g.drawString(
                    "Scaling: " + (double) Math.round(percents[0] * 100) / 100
                            + " "
                            + (double) Math.round(percents[1] * 100) / 100,
                    buffer + 10, buffer + 60);
            g.drawString("Zoom and pan!", frameWidth - buffer - edge - 90,
                    buffer + 20);
        }

    }
}
