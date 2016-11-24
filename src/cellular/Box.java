package cellular;

import javax.swing.JTextField;
import javax.swing.JFrame;

public class Box
{
	private JFrame view;
	private Grid place;
	private GameWindow game;
	private long waitTime;
	private int width;
	private int height;
	private int cellDim;
	/**
	 * Creates a new Box.
	 */
	public Box(String name, int width, int height, String type, long waitTime)
	{
		this.width = width;
		this.height = height;
		this.waitTime = waitTime;
		Profile profile = ProfileManager.getProfile(type);
		this.cellDim = profile.getCellSize();
		place = new Grid(width, height, type);
		view = new JFrame(name);
		// The following closes all the windows together...
		view.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Adding the game panel
		game = new GameWindow(place, type);
		view.add(game);
		// Painting and revealing.
		view.pack();
		view.setVisible(true);
		// The 20 is for the header of the window.
		view.setSize(cellDim * width, 20 + cellDim * height);
	}
	/**
	 * Returns the Grid under this Box.
	 */
	public Grid getGrid()
	{
		return place;
	}
	/**
	 * Ticks the automata and updates the display.
	 */
	public void tick()
	{
		place.tick();
		view.repaint(waitTime, 0, 0, 
			width * cellDim, 20 + height * cellDim);
		try
		{
			Thread.sleep(waitTime);
		}
		catch (InterruptedException ex)
		{
			ex.printStackTrace();
		}
	}
	/**
	 * Runs the Box.
	 */
	public void run(int gens)
	{
		for (int i = 0; i < gens; i++)
		{
			this.tick();
		}
	}
	/**
	 * Returns the GameWindow JPanel under this Box.
	 */
	public GameWindow getGameWindow()
	{
		return game;
	}
	/**
	 * Sets the name of the box, and updates the window title.
	 */
	public void setName(String name)
	{
		JTextField newName = new JTextField(name);
		view.setTitle(newName.getText());
	}
}