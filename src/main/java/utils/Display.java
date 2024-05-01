package utils;

import java.util.Objects;
import java.util.Scanner;

/**
 * Utility class for displaying I/O to the user via the command line.
 */
public class Display {

    /**
     * Scanner used for performing user input.
     */
    Scanner scanner;

    /**
     * Constructor.
     */
    public Display() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Prints a line to standard output with a newline character.
     *
     * @param str String to be printed.
     */
    public void printLine(String str) {
        System.out.println(str);
    }

    /**
     * Prints a line to standard output with no newline character.
     *
     * @param str String to be printed.
     */
    public void print(String str) {
        System.out.print(str);
    }

    /**
     * Reads a line inputted by the user inputted by the command line.
     *
     * @return The inputted line.
     */
    public String readLine() {
        return scanner.nextLine();
    }

    /**
     * Reads the next integer inputted by the user via the command line.
     *
     * @return The inputted integer.
     */
    public int readInteger() {
        return scanner.nextInt();
    }

    /**
     * Runs a "would you like to try again?" loop, until the user selects "y" or "n".
     *
     * @return true if "y" was selected, false if "n" was selected.
     */
    public boolean askTryAgain() {
        print("Would you like to try again? [y/n] ");
        String choice = readLine();

        while (!Objects.equals(choice, "y") && !Objects.equals(choice, "n")) {
            print("Would you like to try again? [y/n] ");
            choice = readLine();
        }

        return Objects.equals(choice, "y");
    }
}
