package cellular;

import java.awt.*;
import java.awt.image.*;

import javax.swing.*;
import javax.imageio.*;

import java.io.IOException;

public class GameWindow extends JPanel
{
	private Grid block;
	private FilingCabinet imager;
	private int cellDim;
	private int maxState;
	private BufferedImage[] pictures;
	/**
	 *  Creates a new GameWindow with a Grid to get data from.
	 */
	public GameWindow(Grid place, int maxState, int cellDim, String type)
	{
		this.maxState = maxState;
		this.cellDim = cellDim;
		block = place;
		this.cellDim = cellDim;
		imager = new FilingCabinet("resources/" + type + "/cells/");
		pictures = new BufferedImage[maxState];
		for (int i = 0; i < maxState; i++)
		{
			pictures[i] = createImageFromFile("cell" + i + ".png");
		}
	}
	/**
	 *  Paints the whole window at every repaint() call.
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
				g.drawImage(pictures[localState],
					cellDim * i, cellDim * j, null);
			}
		}
	}
	/**
	 *  Creates a BufferedImage from a file name.
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
	 *  Writes a BufferedImage to a file.
	 */
	public void writeToFile(BufferedImage img, String filename)
	{
		try
		{
			ImageWriter writer = 
				ImageIO.getImageWritersByFormatName("png").next();
			writer.setOutput(ImageIO.createImageOutputStream(
				imager.getFile(filename)));
			writer.write(img);
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
	}
}