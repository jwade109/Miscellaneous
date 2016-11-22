package chunk;

import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.JPanel;

public class ChunkDemo
{
    private DrawPanel p;
    private JFrame f;
    private ChunkArray<Integer> grid;
    private int t;
    private int frameWidth = 1300;
    private int frameHeight = 1300;
    private int cellSize = 10;
    private int radius = 103;
    private int tick = 1;
    private int[] origin = { 0, 0 };
    private int[] translate = { frameWidth / 2, frameHeight / 2 };
    private int[] newTranslate = { 0, 0 };
    private int[] mousePos = { 0, 0 };
    private boolean moving = false;
    private boolean ALLSTOP = false;

    public static void main(String[] args)
    {
        try
        {
            new ChunkDemo().start();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            try
            {
                e.wait();
            }
            catch (Exception f)
            {

            }
        }
    }

    public void start()
    {
        grid = new ChunkArray<Integer>();

        f = new JFrame("Langton's Ant");

        f.setSize(frameWidth, frameHeight + 45);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        f.addMouseListener(new MouseListener()
        {

            public void mouseClicked(MouseEvent e)
            {
                System.out.println("dumb");
                ALLSTOP = !ALLSTOP;
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

        t = 0;

        while (true)
        {
            updateFrame(f);

            if (moving)
            {
                newTranslate[0] = mousePos[0] - origin[0];
                newTranslate[1] = mousePos[1] - origin[1];
            }

            if (!ALLSTOP)
            {

                if (Math.floorMod(t, tick) == 0)
                {
                    int x = (int) (Math.random() * radius * 2) - radius;
                    int y = (int) (Math.random() * radius * 2) - radius;

                    while ((x * x + y * y) > (radius * radius))
                    {
                        x = (int) (Math.random() * radius * 2) - radius;
                        y = (int) (Math.random() * radius * 2) - radius;
                    }

                    if (grid.getEntry(x, y) == null)
                    {
                        grid.setEntry(1, x, y);
                    }
                    else if (grid.getEntry(x, y) == 1)
                    {
                        grid.setEntry(0, x, y);
                    }
                    else if (grid.getEntry(x, y) == 0)
                    {
                        grid.setEntry(1, x, y);
                    }
                }

                t++;

            }

            p.repaint();

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
        translate[0] += (getTrueWidth(f) - frameWidth) / 2;
        translate[1] += (getTrueHeight(f) - frameHeight) / 2;

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
        return f.getHeight() - 75;
    }

    private int getTrueWidth(JFrame f)
    {
        return f.getWidth() - 30;
    }

    private class DrawPanel extends JPanel
    {
        private static final long serialVersionUID = 1L;

        public void paintComponent(Graphics g)
        {
            int buffer = 50;
            int edge = 25;
            int shadowOffset = 8;
            int stopWidth = 50;

            g.setColor(new Color(230, 230, 230));
            g.fillRect(0, 0, 2 * frameWidth, 2 * frameHeight);

            g.setColor(Color.WHITE);
            g.fillRect(0, 0, 2 * frameWidth, 2 * frameHeight);

            for (int i = grid.getDomain()[0]; i < grid.getDomain()[1]; i++)
            {
                for (int j = grid.getRange()[0]; j < grid.getRange()[1]; j++)
                {
                    if (grid.getEntry(i, j) != null)
                    {
                        if (grid.getEntry(i, j) == 1)
                        {
                            g.setColor(Color.GREEN);
                        }
                        else if (grid.getEntry(i, j) == 0)
                        {
                            g.setColor(Color.BLUE);
                        }
                        g.fillRect(
                                buffer + cellSize * i + translate[0]
                                        + newTranslate[0],
                                buffer + cellSize * j + translate[1]
                                        + newTranslate[1],
                                cellSize, cellSize);
                    }
                }
            }

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
            g.drawString(t + " Mouse: " + mousePos[0] + " " + mousePos[1]
                    + " Translate: " + newTranslate[0] + " " + newTranslate[1]
                    + " " + translate[0] + " " + translate[1], buffer + 10,
                    buffer + 20);
            g.drawString("Window: " + frameWidth + " " + frameHeight,
                    buffer + 10, buffer + 40);
            g.drawString(
                    "Scaling: " + (double) Math.round(percents[0] * 100) / 100
                            + " "
                            + (double) Math.round(percents[1] * 100) / 100,
                    buffer + 10, buffer + 60);
            g.drawString("Zoom and pan!", frameWidth - buffer - edge - 90,
                    buffer + 20);

            if (ALLSTOP)
            {
                g.setColor(Color.GRAY);
                g.fillRect(3 * buffer / 2 + shadowOffset / 2,
                        frameHeight - 2 * buffer - stopWidth + shadowOffset / 2,
                        stopWidth, stopWidth);
                g.setColor(Color.RED);
                g.fillRect(3 * buffer / 2, frameHeight - 2 * buffer - stopWidth,
                        stopWidth, stopWidth);
            }
        }

    }
}
