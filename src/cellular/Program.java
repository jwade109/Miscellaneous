package cellular;

import java.util.Map;
import java.util.HashMap;

import java.io.PrintWriter;

import java.io.Writer;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.IOException;

public class Program
{
	private String name;
	private final long WAIT_TIME = 50;
	private FilingCabinet programFiler;
	private String type;
	private Map<String, Box> city;
	/**
	 * Creates a Program with a given name.
	 */
	public Program(String name, String type)
	{
		this.name = name;
		this.type = type;
		city = new HashMap<String, Box>();
		programFiler = new FilingCabinet();
		System.out.println("Created program " + name);
	}
	/**
	 * Returns the name of the program.
	 */
	public String getName()
	{
		return name;
	}
	/**
	 *  Test whether this program has a Box of the specified name.
	 */
	public boolean hasBoxOfName(String name)
	{
		return city.containsKey(name);
	}
	/**
	 * Renames a Box to a new name.
	 */
	public void renameBox(String boxName, String newName)
	{
		Box b = city.get(boxName);
		city.remove(boxName);
		b.setName(newName);
		city.put(newName, b);
	}
	/**
	 * Creates a blank Box for this program, having specific dimensions.
	 */
	public void addBox(String name, int X, int Y)
	{
		if (!city.containsKey(name))
		{
			city.put(name, new Box(name, X, Y, type, WAIT_TIME));
		}
		else
		{
			System.out.println("That Box name is taken!");	
		}
	}
	/**
	 * Creates a Box from a file and adds it to the field.
	 */
	public void addBox(String boxName, String filename)
	{
		// Fill in method once a syntax is decided for files.
	}
	/**
	 * Ticks a Box forward one step.
	 */
	/*
	public void tickBox(String boxName)
	{
		if (city.containsKey(boxName))
		{
			city.get(boxName).tick();
		}
		else
		{
			System.out.println("No Box of that name.");
		}
	}
	*/
	/**
	 * Runs the tick method of a Box many times to make it animate.
	 */
	public void run(String boxName, int number)
	{
		if (city.containsKey(boxName))
		{
			System.out.println("Running Program...");
			city.get(boxName).run(number);
		}
		else
		{
			System.out.println("No Box with that name found!");
		}
	}
	/**
	 * Saves the given Box's Grid field to a file.
	 */
	public void saveToFile(String boxName)
	{
		if (!city.containsKey(boxName))
		{
			System.out.println("No Box with that name found!");
		}
		else
		{
			// Getting the FileWriter output.
			String fileName = "resources/saves/" + name + "_" 
			    + boxName + ".txt";
			PrintWriter writer = programFiler.createDocFileWriter(fileName);
			
			// Getting the Grid
			Grid input = city.get(boxName).getGrid();

			// Header
			this.append(writer, "Type=" + type + "\n");
			this.append(writer, "X=" + 
				Integer.toString(input.getDimX()) + "\n");
			this.append(writer, "Y=" + 
				Integer.toString(input.getDimY()) + "\n");
			// Data of grid values in Grid as numbers.
			// Consider using an array to store values if this is too slow?
			for (int y = 0; y < input.getDimY(); y++)
			{
				for (int x = 0; x < input.getDimX(); x++)
				{
					this.append(writer, 
						Integer.toString(input.getState(x, y)) + " ");
				}
				this.append(writer, "\n");
			}
			writer.close();
		}
	}
	/**
	 * Returns a FileWriter to write to a given String FileName.
	 */
	private Writer createWriter(String fileName)
	{
		Writer writer;
		try
		{
			writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(fileName), "utf-8"));
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
			System.out.println("File save failure: FileWriter creation!");
			writer = null;
		}
		return writer;
	}
	/**
	 * Appends a String of information to a FileWriter's output.
	 */
	private void append(Writer writer, String info)
	{
		try
		{
			writer.write(info);	
		}
		catch (IOException ex)
		{
			ex.printStackTrace();	
		}
	}
	/**
	 * Loads a Grid into the Program from a file.
	 */
	public void loadFromFile(String fileName)
	{
		// TODO
	}	
}