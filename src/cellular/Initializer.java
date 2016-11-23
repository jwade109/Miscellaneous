package cellular;

import java.io.File;

/**
 * Initializes the cellular automata program's file structure.
 * This program initializes a file structure for pictures,
 * input text, and other files that the program needs to run.
 * Any changes to the file structure should be reflected in this program,
 * which assumes that the program is in a jar file.
 * 
 * @author William McDermott
 * @version 2016.11.22
 */
public class Initializer
{
    /**
     * Initializes the file structure.
     */
    public static void initialize()
    {
        FilingCabinet checker = new FilingCabinet();
        System.out.println(checker.getPathToFiles());
        boolean missingDir = false;
        if (!checker.checkExists("resources/images"))
        {
            FilingCabinet.createDirectories(
                checker.getPathToFiles() + 
                File.separator + "images");
            missingDir = true;
        }
        if (!checker.checkExists("resources/saves"))
        {
            FilingCabinet.createDirectories(
                checker.getPathToFiles() + 
                File.separator + "saves");
            missingDir = true;
        }
        if (missingDir)
        {
            Messages.displayMissingDirectoryText();
            System.exit(0);
        }
    }
}