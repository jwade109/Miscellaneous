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

    private int scaleable = 40;
    private int frameWidth = 2200;
    private int frameHeight = 1700;
    private int k = 0;
    private int[] origin = {0, 0};
    private int[] newTranslate = {0, 0};
    private int[] translate = {frameWidth / 2, frameHeight / 2};
    private int[] mousePos = {0, 0};
    private boolean moving = false;
    private boolean antEraser = false;
    private boolean ALLSTOP = true;

    public CurveWindow(LCurve curve, int order)
    {
        ants = new ArrayList<CurveAnt>();
        ants.add(new CurveAnt(0, 0, curve, order, Direction.EAST, Color.BLACK));

        maxStep = curve.generate(order).length();

        f = new JFrame(curve.getName() + " Order " + order);
        lines = new ArrayList<Line>();

        f.setSize(frameWidth, frameHeight + 45);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        updateFrame(f);

        f.addMouseListener(new MouseListener()
        {

            public void mouseClicked(MouseEvent e)
            {
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
                int ds = -e.getWheelRotation();
                scaleable += ds;
                scaleable = Math.max(1, scaleable);
                if (scaleable > 1)
                {
                    int[] per = getMouseTranslateRel();
                    translate[0] -= per[0] * ds;
                    translate[1] -= per[1] * ds;
                }
            }
        });

        p = new DrawPanel();

        f.getContentPane().add(p);
    }

    public void start(int tick)
    {
        if (tick < 0)
        {
            throw new IllegalArgumentException("tick length must be positive");
        }

        setVisibility(true);

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

                if (tick == 0 || Math.floorMod(t, Math.max(1, tick)) == 0)
                {
                    for (int i = 0; i < ants.size(); i++)
                    {
                        Line a = ants.get(i).next();
                        if (a != null)
                        {
                            if (!lines.contains(a))
                            {
                                lines.add(a);
                            }
                            else if (antEraser)
                            {
                                lines.remove(a);
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

            if (tick > 0)
            {
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
    }

    public void setErasing(boolean state)
    {
        antEraser = state;
    }

    public void setVisibility(boolean visible)
    {
        f.setVisible(visible);
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
        int y = MouseInfo.getPointerInfo().getLocation().y - f.getY() - 60;
        return new int[] {x, y};
    }

    private double[] getMouseRel()
    {
        int[] mousePos = getMousePos();
        double[] percents = new double[2];
        percents[0] = (double) (mousePos[0] - frameWidth / 2)
                / (frameWidth / 2);
        percents[1] = (double) (mousePos[1] - frameHeight / 2)
                / (frameHeight / 2);
        return percents;
    }

    private int[] getMouseTranslateRel()
    {
        int[] mousePos = getMousePos();
        int dx = Math.round(100 * (mousePos[0] - translate[0] - newTranslate[0])
                / frameWidth);
        int dy = Math.round(100 * (mousePos[1] - translate[1] - newTranslate[1])
                / frameHeight);

        return new int[] {dx, dy};
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
            double[] rel = getMouseRel();
            int[] trel = getMouseTranslateRel();
            g.setColor(Color.GRAY);
            g.drawString("Paused: " + ALLSTOP + " Step: " + k + " of " + maxStep
                    + " Tick: " + tick + " Zoom: " + (scaleable / 40.0)
                    + " Mouse: " + mousePos[0] + " " + mousePos[1]
                    + " Translate: " + newTranslate[0] + " " + newTranslate[1]
                    + " " + translate[0] + " " + translate[1], buffer + 10,
                    buffer + 20);
            g.drawString("Window: " + frameWidth + " " + frameHeight,
                    buffer + 10, buffer + 40);
            g.drawString(
                    "Mouse rel pos: " + (double) Math.round(rel[0] * 100) / 100
                            + " " + (double) Math.round(rel[1] * 100) / 100,
                    buffer + 10, buffer + 60);
            g.drawString(
                    "Mouse trel pos: "
                            + (double) Math.round(trel[0] * 100) / 100 + " "
                            + (double) Math.round(trel[1] * 100) / 100,
                    buffer + 10, buffer + 80);

            // DIAGNOSTIC GRAPHICS
            // g.setColor(Color.RED);
            // g.drawOval(frameWidth / 2 - 10, frameHeight / 2 - 10, 20, 20);
            // g.drawLine(0, 0, frameWidth, frameHeight);
            // g.drawLine(frameWidth, 0, 0, frameHeight);
            // g.drawOval(translate[0] + newTranslate[0] - 10,
            // translate[1] + newTranslate[1] - 10, 20, 20);
            // g.drawOval(translate[0] + newTranslate[0] - 10 - scaleable,
            // translate[1] + newTranslate[1] - 10, 20, 20);
            // g.drawOval(translate[0] + newTranslate[0] - 10 + scaleable,
            // translate[1] + newTranslate[1] - 10, 20, 20);
            // g.drawOval(translate[0] + newTranslate[0] - 10,
            // translate[1] + newTranslate[1] - 10 - scaleable, 20, 20);
            // g.drawOval(translate[0] + newTranslate[0] - 10,
            // translate[1] + newTranslate[1] - 10 + scaleable, 20, 20);
            // g.drawOval(translate[0] + newTranslate[0] - 10 - scaleable,
            // translate[1] + newTranslate[1] - 10 - scaleable, 20, 20);
            // g.drawOval(translate[0] + newTranslate[0] - 10 + scaleable,
            // translate[1] + newTranslate[1] - 10 - scaleable, 20, 20);
            // g.drawOval(translate[0] + newTranslate[0] - 10 - scaleable,
            // translate[1] + newTranslate[1] - 10 + scaleable, 20, 20);
            // g.drawOval(translate[0] + newTranslate[0] - 10 + scaleable,
            // translate[1] + newTranslate[1] - 10 + scaleable, 20, 20);
        }

    }

    private void drawThickLine(Graphics g, Line line)
    {
        int[] a = line.getLoc();
        g.setColor(line.getColor());

        if (a[0] < a[2]) // moving to the right
        {
            g.fillRect(
                    scaleable * a[0] + translate[0] + newTranslate[0]
                            - scaleable / 6,
                    scaleable * a[1] + translate[1] + newTranslate[1]
                            - scaleable / 6,
                    scaleable * a[2] - scaleable * a[0] + scaleable / 3 + 1,
                    scaleable / 3 + 1);
        }
        if (a[2] < a[0]) // moving to the left
        {
            g.fillRect(
                    scaleable * a[2] + translate[0] + newTranslate[0]
                            - scaleable / 6,
                    scaleable * a[1] + translate[1] + newTranslate[1]
                            - scaleable / 6,
                    scaleable * a[0] - scaleable * a[2] + scaleable / 3 + 1,
                    scaleable / 3 + 1);
        }
        if (a[1] < a[3]) // moving up
        {
            g.fillRect(
                    scaleable * a[0] + translate[0] + newTranslate[0]
                            - scaleable / 6,
                    scaleable * a[1] + translate[1] + newTranslate[1]
                            - scaleable / 6,
                    scaleable / 3 + 1,
                    scaleable * a[3] - scaleable * a[1] + scaleable / 3 + 1);
        }
        if (a[3] < a[1]) // moving down
        {
            g.fillRect(
                    scaleable * a[0] + translate[0] + newTranslate[0]
                            - scaleable / 6,
                    scaleable * a[3] + translate[1] + newTranslate[1]
                            - scaleable / 6,
                    scaleable / 3 + 1,
                    scaleable * a[1] - scaleable * a[3] + scaleable / 3 + 1);
        }
    }
}
