package cellular;

/**
 * A static class designed to hold some messages for the program to display.
 * These are not error messages, but various user messages the program can use.
 * 
 * @author William McDermott
 * @version 2016.11.24
 */
public class Messages
{
    public static final String WELCOME_TEXT
        = "Welcome to the Cellular Automata Interface.";
    
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
            + " a certain number of ticks forward in the automata.";
    
    public static final String SAVE_COMMAND_TEXT
        = "save <programName> <boxName> : Saves a Box by its program"
            + " name and box name basically.\n";
    
    public static final String LOAD_COMMAND_TEXT
        = "load : Not yet implemented.\n";
    
    public static final String HELP_COMMAND_TEXT
        = "help <optional: command name> : Returns the list of usable"
            + " commands, or if a command was put in, the information"
            + " for that specific command.\n";
    
    public static final String HELP_TEXT
        = "The following are usable commands: \n"
            + EXIT_COMMAND_TEXT
            + CREATE_PROGRAM_COMMAND_TEXT
            + CREATE_BOX_COMMAND_TEXT
            + RUN_COMMAND_TEXT
            + LOAD_COMMAND_TEXT
            + SAVE_COMMAND_TEXT
            + HELP_COMMAND_TEXT;
    
    /**
     * Displays a message for entering the program.
     */
    public static void displayWelcomeText()
    {
        System.out.println(WELCOME_TEXT);
        System.out.println(HELP_TEXT);
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
    
    /**
     * Displays the exit command instructions.
     */
    public static void displayHelpExitText()
    {
        System.out.println(EXIT_COMMAND_TEXT);
    }
    /**
     * Displays the createProgram command instructions.
     */
    public static void displayHelpCreateProgramText()
    {
        System.out.println(CREATE_PROGRAM_COMMAND_TEXT);
    }
    /**
     * Displays the createBox command instructions.
     */
    public static void displayHelpCreateBoxText()
    {
        System.out.println(CREATE_BOX_COMMAND_TEXT);
    }
    /**
     * Displays the run command instructions.
     */
    public static void displayHelpRunText()
    {
        System.out.println(RUN_COMMAND_TEXT);
    }
    /**
     * Displays the load command instructions.
     */
    public static void displayHelpLoadText()
    {
        System.out.println(LOAD_COMMAND_TEXT);
    }
    /**
     * Displays the save command instructions.
     */
    public static void displayHelpSaveText()
    {
        System.out.println(SAVE_COMMAND_TEXT);
    }
    /**
     * Displays the help command instructions.
     */
    public static void displayHelpCommandText()
    {
        System.out.println(HELP_COMMAND_TEXT);
    }
}
