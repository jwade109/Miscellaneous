package tree;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Window
{
    private DrawPanel p;
    private JFrame f;
    private int frameWidth = 2000;
    private int frameHeight = 1800;
    private Manager m;

    public static void main(String[] args)
    {
        Window w = new Window(new Manager(new SmartAI("Bob"), new SmartAI("Jill"), 2));
        w.start();
    }

    public Window(Manager m)
    {
        this.m = m;
        p = new DrawPanel();

        f = new JFrame("Drone Simulator 2017");

        f.setSize(frameWidth, frameHeight + 45);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.addMouseListener(new MouseListener()
        {

            public void mouseClicked(MouseEvent e)
            {
                if (!m.gameOver())
                {
                    m.turn();
                }
            }

            public void mouseEntered(MouseEvent e)
            {
            }

            public void mouseExited(MouseEvent e)
            {
            }

            public void mousePressed(MouseEvent e)
            {
            }

            public void mouseReleased(MouseEvent e)
            {
            }
        });
        f.addMouseWheelListener(new MouseWheelListener()
        {

            public void mouseWheelMoved(MouseWheelEvent e)
            {
            }

        });
        f.setExtendedState(JFrame.MAXIMIZED_BOTH);
        f.getContentPane().add(p);
    }

    public void start()
    {
        f.setVisible(true);

        while (true)
        {
            updateFrame(f);
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
        int[] pos = {x, y};
        return pos;
    }

    private int getTrueHeight(JFrame f)
    {
        return f.getHeight() - 45;
    }

    private int getTrueWidth(JFrame f)
    {
        return f.getWidth();
    }

    private double round(double n, double k)
    {
        return Math.round(n * k) / k;
    }

    class DrawPanel extends JPanel
    {

        private static final long serialVersionUID = 1L;

        public DrawPanel()
        {
        }

        public void paintComponent(Graphics g)
        {

            Graphics2D g2 = (Graphics2D) g;

            final int buffer = 50;
            final int edge = 25;
            final int shadowOffset = 8;
            final int cellSize = 1500 / m.getWidth();
            final int xCenter = frameWidth / 2;
            final int yCenter = frameHeight / 2;

            // background
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, 2 * frameWidth, 2 * frameHeight);

            /////////////////////////////////////////////////////////
            // canvas stuff goes here

            g.setColor(Color.BLACK);
            int totalRadius = m.getWidth() * cellSize / 2;
            for (int x = 0; x < m.getWidth(); x++)
            {
                for (int y = 0; y < m.getWidth(); y++)
                {
                    Play cell = m.get(x, y);
                    if (cell == Play.X)
                    {
                        g.setColor(Color.RED);
                    }
                    else if (cell == Play.O)
                    {
                        g.setColor(Color.GREEN);
                    }
                    else
                    {
                        g.setColor(Color.DARK_GRAY);
                    }
                    g.fillRect(xCenter + x * cellSize - totalRadius,
                            yCenter + y * cellSize - totalRadius, cellSize,
                            cellSize);
                }
            }

            /////////////////////////////////////////////////////////

            // gray edges
            g.setColor(new Color(230, 230, 230));
            g.fillRect(0, 0, buffer, 2 * frameHeight);
            g.fillRect(0, 0, 2 * frameHeight, buffer);
            g.fillRect(frameWidth - buffer - edge, 0, frameWidth,
                    2 * frameHeight);
            g.fillRect(0, frameHeight - buffer - edge, 2 * frameWidth,
                    frameHeight);

            // dark gray shadow
            g.setColor(Color.GRAY);
            g.fillRect(frameWidth - buffer - edge, buffer + shadowOffset,
                    shadowOffset, frameHeight - 2 * buffer - edge);
            g.fillRect(buffer + shadowOffset, frameHeight - buffer - edge,
                    frameWidth - 2 * buffer - edge, shadowOffset);

            g.setColor(Color.WHITE);
            g.fillRect(buffer, buffer, 200, 60);
            g.setColor(Color.BLACK);
            g.drawRect(buffer, buffer, 200, 60);

            // canvas border
            g.setColor(Color.BLACK);
            g.drawRect(buffer, buffer, frameWidth - 2 * buffer - edge,
                    frameHeight - 2 * buffer - edge);

            // text
            g.setColor(Color.GRAY);
            g.setFont(new Font("Calibri", Font.PLAIN, 20));
            g.drawString("Window: " + frameWidth + " " + frameHeight,
                    buffer + 10, buffer + 30);
        }

    }
}