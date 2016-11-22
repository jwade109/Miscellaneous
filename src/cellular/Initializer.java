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
        if (!checker.checkExists("resources"))
        {
            FilingCabinet.createDirectory(
                checker.getPathToFiles() + 
                File.pathSeparator + "/resources");
        }
        if (!checker.checkExists("saves"))
        {
            FilingCabinet.createDirectory(
                checker.getPathToFiles() + 
                File.pathSeparator + "saves");
        }
        System.out.println(checker.getPathToFiles());
    }
}