package cellular;

public class main
{
	public static void main(String[] args)
	{
		if (args[0].equals("run"))
		{
			System.out.println("Running program...");
			Control.main();
		}
		else
		{
			System.out.println("Command not recognized!");
			System.out.println("Use a recognized command on the" 
				+ " terminal, like:");
			System.out.println("run");
		}
	}
}