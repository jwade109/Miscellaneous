package cellular;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import java.io.IOException;

import java.util.Observer;
import java.util.Observable;

public class GameWindow extends JPanel
{
    private Grid block;
    private FilingCabinet imager;
    private int cellDim;
    private BufferedImage[] pictures;
    private static final long serialVersionUID = 42L;


    /**
     * Creates a new GameWindow with a Grid to get data from.
     */
    public GameWindow(Grid place, String type)
    {
        // The maxState can be accessed by the length of pictures[].
        Profile profile = ProfileManager.getProfile(type);
        this.cellDim = profile.getCellSize();
        int stateBound = profile.getMaxState() + 1;
        block = place;
        imager = new FilingCabinet("resources/images/" + type + "/cells/");
        pictures = new BufferedImage[stateBound];
        for (int i = 0; i < stateBound; i++)
        {
            pictures[i] = createImageFromFile("cell" + i + ".png");
        }
    }
    

    /**
     * Paints the whole window at every repaint() call.
     */
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        for (int i = 0; i < block.getDimX(); i++)
        {
            for (int j = 0; j < block.getDimY(); j++)
            {
                int localState = block.getState(i, j);
                g.drawImage(pictures[localState], cellDim * i, cellDim * j,
                    null);
            }
        }
    }


    /**
     * Creates a BufferedImage from a file name.
     */
    private BufferedImage createImageFromFile(String filename)
    {
        BufferedImage image = null;
        try
        {
            image = ImageIO.read(imager.getFile(filename));
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        return image;
    }


    // The following method is here because I took it from online to understand
    // the writers involved.
    /**
     * Writes a BufferedImage to a file.
     */
    public void writeToFile(BufferedImage img, String filename)
    {
        try
        {
            ImageWriter writer = ImageIO.getImageWritersByFormatName("png")
                .next();
            writer.setOutput(ImageIO.createImageOutputStream(imager.getFile(
                filename)));
            writer.write(img);
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }
}
