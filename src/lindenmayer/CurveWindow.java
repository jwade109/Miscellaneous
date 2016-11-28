package lindenmayer;

import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import javax.swing.JPanel;

public class CurveWindow
{
    private DrawPanel p;
    private JFrame f;
    private ArrayList<CurveAnt> ants;
    private ArrayList<Line> lines;
    private int maxStep;
    private int tick;

    private int lineSize = 40;
    private int frameWidth = 2200;
    private int frameHeight = 1700;
    private final int order = 15;
    private final int runTime = 4000;
    private int k = 0;
    private int[] origin = { 0, 0 };
    private int[] newTranslate = { 0, 0 };
    private int[] translate = { frameWidth / 2, frameHeight / 2 };
    private int[] mousePos = { 0, 0 };
    private boolean moving = false;
    private boolean ALLSTOP = true;

    public void showWindow()
    {
        LCurve curve = new DragonCurve();
        ants = new ArrayList<CurveAnt>();
        ants.add(new CurveAnt(0, 0, curve, order, Direction.EAST, Color.BLACK));

        maxStep = curve.generate(order).length();
        tick = (int) ((double) runTime / (double) maxStep) + 1;

        f = new JFrame("Turtle Graphics (" + ants.size() + " actors)");
        lines = new ArrayList<Line>();

        f.setSize(frameWidth, frameHeight + 45);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        updateFrame(f);

        f.addMouseListener(new MouseListener()
        {

            public void mouseClicked(MouseEvent e)
            {
                ALLSTOP = !ALLSTOP;
                System.out.println("STOP");
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
                lineSize -= e.getWheelRotation();
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

            if (!ALLSTOP)
            {

                if (Math.floorMod(t, tick) == 0)
                {
                    for (int i = 0; i < ants.size(); i++)
                    {
                        Line a = ants.get(i).next();
                        if (a != null)
                        {
                            if (lines.contains(a))
                            {
                                lines.remove(a);
                            }
                            else
                            {
                                lines.add(a);
                            }
                        }
                    }
                    boolean done = true;
                    for (CurveAnt ant : ants)
                    {
                        if (!ant.isDone())
                        {
                            done = false;
                        }
                    }
                    if (!done)
                    {
                        k++;
                    }
                }

                t++;

            }

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

    class DrawPanel extends JPanel
    {

        private static final long serialVersionUID = 1L;

        public DrawPanel()
        {
        };

        public void paintComponent(Graphics g)
        {
            int buffer = 30;
            int shadowOffset = 8;
            int loadHeight = 7;

            g.setColor(new Color(230, 230, 230));
            g.fillRect(0, 0, 2 * frameWidth, 2 * frameHeight);

            g.setColor(Color.WHITE);
            g.fillRect(0, 0, 2 * frameWidth, 2 * frameHeight);

            if (k < maxStep)
            {

            }

            g.setColor(Color.BLUE);
            for (int i = 0; i < lines.size(); i++)
            {
                try
                {
                    drawThickLine(g, lines.get(i));
                }
                catch (Exception e)
                {

                }
            }

            g.setColor(new Color(230, 230, 230));
            g.fillRect(0, 0, buffer, 2 * frameHeight);
            g.fillRect(0, 0, 2 * frameHeight, buffer);
            g.fillRect(frameWidth - buffer, 0, frameWidth, 2 * frameHeight);
            g.fillRect(0, frameHeight - buffer, 2 * frameWidth, frameHeight);

            g.setColor(Color.GRAY);
            g.fillRect(frameWidth - buffer, buffer + shadowOffset, shadowOffset,
                    frameHeight - 2 * buffer);
            g.fillRect(buffer + shadowOffset, frameHeight - buffer,
                    frameWidth - 2 * buffer, shadowOffset);

            int loadWidth = (int) ((((double) (k)) / maxStep)
                    * (frameWidth - 2 * buffer));

            g.setColor(Color.BLUE);
            g.fillRect(buffer, frameHeight - loadHeight - buffer, loadWidth,
                    loadHeight);

            g.setColor(Color.BLACK);
            g.drawRect(buffer, buffer, frameWidth - 2 * buffer,
                    frameHeight - 2 * buffer);

            mousePos = getMousePos();
            double[] percents = getMousePercent();
            g.setColor(Color.GRAY);
            g.drawString("Paused: " + ALLSTOP + " Step: " + k + " of " + maxStep
                    + " Tick: " + tick + " Zoom: " + (lineSize / 40.0)
                    + " Mouse: " + mousePos[0] + " " + mousePos[1]
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
            g.drawString("Zoom and pan!", frameWidth - buffer - 90,
                    buffer + 20);

            /*
             * DIAGNOSTIC GRAPHICS g.setColor(Color.RED);
             * g.drawOval(frameWidth/2 - 10, frameHeight/2 - 10, 20, 20);
             * g.drawLine(0, 0, frameWidth, frameHeight); g.drawLine(frameWidth,
             * 0, 0, frameHeight);
             */
        }

    }

    private void drawThickLine(Graphics g, Line line)
    {
        int[] a = line.getLoc();
        g.setColor(line.getColor());

        if (a[0] < a[2]) // moving to the right
        {
            g.fillRect(
                    lineSize * a[0] + translate[0] + newTranslate[0]
                            - lineSize / 6,
                    lineSize * a[1] + translate[1] + newTranslate[1]
                            - lineSize / 6,
                    lineSize * a[2] - lineSize * a[0] + lineSize / 3 + 1,
                    lineSize / 3 + 1);
        }
        if (a[2] < a[0]) // moving to the left
        {
            g.fillRect(
                    lineSize * a[2] + translate[0] + newTranslate[0]
                            - lineSize / 6,
                    lineSize * a[1] + translate[1] + newTranslate[1]
                            - lineSize / 6,
                    lineSize * a[0] - lineSize * a[2] + lineSize / 3 + 1,
                    lineSize / 3 + 1);
        }
        if (a[1] < a[3]) // moving up
        {
            g.fillRect(
                    lineSize * a[0] + translate[0] + newTranslate[0]
                            - lineSize / 6,
                    lineSize * a[1] + translate[1] + newTranslate[1]
                            - lineSize / 6,
                    lineSize / 3 + 1,
                    lineSize * a[3] - lineSize * a[1] + lineSize / 3 + 1);
        }
        if (a[3] < a[1]) // moving down
        {
            g.fillRect(
                    lineSize * a[0] + translate[0] + newTranslate[0]
                            - lineSize / 6,
                    lineSize * a[3] + translate[1] + newTranslate[1]
                            - lineSize / 6,
                    lineSize / 3 + 1,
                    lineSize * a[1] - lineSize * a[3] + lineSize / 3 + 1);
        }
    }
}
