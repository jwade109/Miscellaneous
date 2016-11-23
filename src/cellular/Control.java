package cellular;

import java.util.Scanner;

import java.util.Map;
import java.util.HashMap;

public class Control
{
	private static Map<String, Program> programs;
	/**
	 * Interprets commands into actions,
	 * based on user input in the command line.
	 */
	public static void main()
	{
		programs = new HashMap<String, Program>();
		Messages.displayWelcomeText();
		String[] commands = new String[10];
		commands[0] = "";
		Scanner scan = new Scanner(System.in);
		while (!commands[0].equals("exit"))
		{
			// Clear the commands array.
			for (int i = 0; i < 10; i++)
			{
				commands[i] = null;	
			}
			// Ask for commands
			System.out.print("Command: ");
			String line = scan.nextLine();
			Scanner reader = new Scanner(line);
			// Record the commands
			int index = -1;
			while (reader.hasNext())
			{
				index++;
				commands[index] = reader.next();
			}
			if (commands[0] == null
				|| commands[0] == "")
			{
				continue;
			}
			reader.close();
			// Interpret the commands into action
			if (commands[0].equals("createBox"))
			{
				if (index < 4)
				{
					System.out.println("Not enough arguments!");
				}
				else
				{
					createBox(commands);
				}
			}
			else if (commands[0].equals("createProgram"))
			{
				if (index < 2)
				{
					System.out.println("Not enough arguments!");
				}
				else
				{
					createProgram(commands);
				}
			}
			else if (commands[0].equals("run"))
			{
				if (index == 0)
				{
					System.out.println("Specify Name!");
				}
				else if (index == 1)
				{
					System.out.println("Specify Box!");
				}
				else if (index == 2)
				{
					System.out.println("Specify number of ticks!");
				}
				else
				{
					run(commands);
				}
			}
			else if (commands[0].equals("save"))
			{
				if (index < 2)
				{
					System.out.println("Too few arguments!");
				}
				else 
				{
					save(commands);	
				}
			}
			else if (commands[0].equals("exit"))
			{
			    // Do nothing.
			}
			else
			{
				System.out.println("Unrecognized Command!");	
			}
		}
		scan.close();
		Messages.displayExitText();
		System.exit(0);
	}
	/**
	 * Creates a new program and adds it to the list.
	 */
	public static void createProgram(String[] input)
	{
		if (!(input[2].equals("life")
			|| input[2].equals("ant")
			|| input[2].equals("test")
			|| input[2].equals("wire")))
		{
			System.out.println("No automata of that type!");
		}
		else
		{
			if (!programs.containsKey(input[1]))
			{
				Program p = new Program(input[1], input[2]);
				programs.put(input[1], p);
			}
			else
			{
				System.out.println("Already a Program of that name!");
			}
		}
	}
	/**
	 * Creates a new Box for a given program with a given name.
	 */
	public static void createBox(String[] input)
	{
		System.out.println("Creating New Box...");
		if (programs.containsKey(input[1]))
		{
			Program prgm = programs.get(input[1]);
			int X = Integer.parseInt(input[3]);
			int Y = Integer.parseInt(input[4]);
			prgm.addBox(input[2], X, Y);
		}
		else
		{
			System.out.println("Name taken!");
		}
	}
	/**
	 * Runs a grid in the specified program.
	 */
	public static void run(String input[])
	{
		if (programs.containsKey(input[1]))
		{
			programs.get(input[1]).run(input[2], Integer.parseInt(input[3]));
		}
		else
		{
			System.out.println("Program not found");
		}
	}
	/**
	 * Saves a Grid to a file.
	 */
	public static void save(String[] input)
	{
		if (programs.containsKey(input[1]))
		{
			programs.get(input[1]).saveToFile(input[2]);
		}
		else
		{
			System.out.println("Program not found");
		}
	}
	/**
	 * Loads a Grid from a file into a new Grid.
	 */
	public static void load(String[] input)
	{
		System.out.println("In Development!");
	}
}