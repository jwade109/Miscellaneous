package tictactoe;

import java.util.Arrays;

public class ProgramReferenceTest
{
    /**
     * The main test runner.
     * 
     * @param args
     */
    public static void main(String[] args)
    {
        // The supplied args is the array in question.
        System.out.println(Arrays.toString(args));
        ProgramReferenceTest.edit(args);
        System.out.println(Arrays.toString(args));
    }

    /**
     * Edits the argument for a test.
     * 
     * @param argument
     */
    public static void edit(String[] argument)
    {
        if (argument.length > 0)
        {
            System.out.println(argument[0]);
            argument[0] = "Problem!";
            System.out.println(argument[0]);
        }
    }
}
