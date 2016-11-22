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
    
    /**
     * Displays a message for entering the program.
     */
    public static void displayWelcomeText()
    {
        System.out.println(WELCOME_TEXT);
    }
    
    /**
     * Displays a message for when you leave the program.
     */
    public static void displayExitText()
    {
        System.out.println(EXIT_TEXT);
    }
}
