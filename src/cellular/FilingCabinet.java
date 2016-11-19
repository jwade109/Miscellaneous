package cellular;

import java.io.File;
import java.nio.file.Files;

import java.lang.Exception;
import java.io.FileNotFoundException;

import java.util.Scanner;
import java.io.PrintWriter;

public class FilingCabinet
{
	private String pathToFiles;
	/**
	 *  Creates a FilingCabinet with the default Documentation address.
	 */
	public FilingCabinet()
	{
		pathToFiles = "/Users/macuser/programs/Project Euler/Documentation/";
	}
	/**
	 *  Creates a FilingCabinet with a path to the files for documentation.
	 */
	public FilingCabinet(String path)
	{
		pathToFiles = path;
	}
	/**
	 *  Creates a new documentation file for a problem.
	 */
	public void makeNewDocFile(String newNumber)
	{
		String fileName = "Problem " + newNumber + ".txt";
		if (checkExisting(fileName))
		{
			System.out.println("File already exists!");
		}
		else
		{
			PrintWriter printer = createDocFileWriter(fileName);
			if (printer == null)
			{
				System.out.println("Error Creating PrintWriter!");
			}
			else
			{
				printer.println("This is the documentation file for " + 
					"Project Euler problem " + newNumber + ".");
				printer.println("This problem has not been documented yet.");
			}
			printer.close();
		}
	}
	/**
	 *  Returns a File object with the specified name.
	 */
	public File getFile(String filename)
	{
		try
		{
			File obj = new File(pathToFiles + filename);
			return obj;
		}
		catch (NullPointerException ex)
		{
			ex.printStackTrace();
			return null;
		}
	}
	/**
	 *  Checks if a file of the given fileName already exists in the directory.
	 */ 
	private boolean checkExisting(String fileName)
	{
		try 
		{
			File f = new File(pathToFiles + fileName);
			Scanner dummy = new Scanner(f);
			dummy.close();
		}
		catch (FileNotFoundException ex) 
		{
			return false;
		}
		return true;
	}
	/**
	 *  Creates a new PrintWriter to print to a new doc file.
	 */
	public PrintWriter createDocFileWriter(String fileName)
	{
		PrintWriter printer;
		try 
		{
			printer = new PrintWriter(pathToFiles + fileName, "UTF-8");
		}
		catch (Exception ex) 
		{
			ex.printStackTrace();
			printer = null;
		}
		return printer;
	}
	/**
	 *  Prints out a documentation file with the given name.
	 */
	public void printFile(String fileName)
	{
		printLines(20);
		System.out.println(fileName);
		printLines(20);
		Scanner out = this.createScannerForFile(fileName);
		while (out.hasNext())
		{
			System.out.println(out.nextLine());
		}
		out.close();
		printLines(20);
	}
	/**
	 *  Creates a scanner that reads a file.
	 */
	public Scanner createScannerForFile(String fileName)
	{
		Scanner out;
		try 
		{
			File f = new File(pathToFiles + fileName);
			out = new Scanner(f);
		}
		catch (FileNotFoundException ex) 
		{
			out = new Scanner("File was not found!");
			ex.printStackTrace();
		}
		return out;
	}
	/**
	 *  Helper method that makes some lines in the command terminal.
	 */
	private void printLines(int numLines)
	{
		for (int i = 0; i < numLines; i++)
		{
			System.out.print("-");
		}
		System.out.println();
	}
}