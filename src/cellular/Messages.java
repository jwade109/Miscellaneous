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
    public static final String EXIT_COMMAND_TEXT
        = "exit : Leaves the simulation. \n";
    public static final String CREATE_PROGRAM_COMMAND_TEXT
        = "createProgram <programName> [ant; life; test; wire]"
            + " : creates a new program for running windows.\n";
    public static final String CREATE_BOX_COMMAND_TEXT
        = "createBox <existingProgramName> <boxName> <#dimX> <#dimY>"
            + " : Creates a new automata within a program, with the given"
            + " dimensions and boxName, which will be displayed in the "
            + "window.\n";
    public static final String RUN_COMMAND_TEXT
        = "run <programName> <boxName> <#ticks> : Runs an existing Box"
            + "a certain number of ticks forward in the automata.";
    public static final String SAVE_COMMAND_TEXT
        = "save <"
    public static final String HELP_TEXT
        = "The following are usable commands: \n"
            + EXIT_COMMAND_TEXT
            + CREATE_PROGRAM_COMMAND_TEXT
            + CREATE_BOX_COMMAND_TEXT
            + RUN_COMMAND_TEXT;
    
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
    
    /**
     * Displays a list of usable commands in the interface.
     */
    public static void displayHelpText()
    {
        System.out.println(HELP_TEXT);
    }
}
