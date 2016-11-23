package cellular;

/**
 * A static class designed to hold some messages for the program to display.
 * These are not error messages, but various user messages the program can use.
 * 
 * @author William McDermott
 * @version 2016.11.22
 */
public class Messages
{
    /**
     * The Welcome text.
     */
    public static final String WELCOME_TEXT
        = "Welcome to the Cellular Automata Interface.";
    /**
     * The Exit text.
     */
    public static final String EXIT_TEXT
        = "Hope that you enjoyed yourself!";
    
    public static final String MISSING_DIR_TEXT
        = "The program can't find the resources folder or " +
            "one of the subfolders. Please recreate the sub folders " + 
            "and be sure to add in the relevant pictures or save files!";
    
    /**
     * Displays a message for entering the program.
     */
    public static void displayWelcomeText()
    {
        System.out.println(WELCOME_TEXT);
    }
    
    /**
     * Displays a message for exiting the program.
     */
    public static void displayExitText()
    {
        System.out.println(EXIT_TEXT);
    }
    
    /**
     * Displays a message for what to do when the directories are gone.
     */
    public static void displayMissingDirectoryText()
    {
        System.out.println(MISSING_DIR_TEXT);
    }
}
