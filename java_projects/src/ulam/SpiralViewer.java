package ulam;

import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JPanel;

import chunk.ChunkArray;

public class SpiralViewer
{
    private DrawPanel p;
    private JFrame f;
    private ChunkArray<Boolean> spiral;
    private int frameWidth = 1300;
    private int frameHeight = 1300;
    private int cellSize = 20;
    private int spiralWidth = 400;
    private int[] origin = { 0, 0 };
    private int[] translate = { frameWidth / 2, frameHeight / 2 };
    private int[] newTranslate = { 0, 0 };
    private int[] mousePos = { 0, 0 };
    private boolean moving = false;

    public static void main(String[] args)
    {
        SpiralViewer m = new SpiralViewer();
        m.start();
    }

    public void start()
    {
        f = new JFrame(
                "Ulam Spiral (Size " + spiralWidth + " x " + spiralWidth);
        f.setSize(frameWidth, frameHeight + 45);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
        p = new DrawPanel();
        f.getContentPane().add(p);
        spiral = new Spiral().getUlamSpiral(spiralWidth);

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

        while (true)
        {
            updateFrame(f);

            p.repaint();

            if (moving)
            {
                newTranslate[0] = mousePos[0] - origin[0];
                newTranslate[1] = mousePos[1] - origin[1];
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
            
            g.setColor(new Color(230, 230, 230));
            g.fillRect(0, 0, 2 * frameWidth, 2 * frameHeight);

            g.setColor(Color.WHITE);
            g.fillRect(0, 0, 2 * frameWidth, 2 * frameHeight);

            g.setColor(Color.BLACK);
            try
            {
                for (int i = spiral.domain()[0]; i < spiral.domain()[1]; i++)
                {
                    for (int j = spiral.range()[0]; j < spiral
                            .range()[1]; j++)
                    {
                        Boolean isPrime = spiral.getEntry(i, j);
                        if (isPrime != null && isPrime)
                        {
                            g.fillRect(
                                    buffer + cellSize * i + translate[0]
                                            + newTranslate[0],
                                    buffer + cellSize * j + translate[1]
                                            + newTranslate[1],
                                    cellSize, cellSize);
                        }
                    }
                }
            }
            catch (Exception e)
            {
                
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
            g.drawString(" Mouse: " + mousePos[0] + " " + mousePos[1]
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
        }

    }
}
