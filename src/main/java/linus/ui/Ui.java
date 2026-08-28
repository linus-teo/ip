package linus.ui;

import java.util.Scanner;

/**
 * Represents the frontend of the Linus chatbot.
 * Responsible for printing output to the interface.
 */
public class Ui {
    /** Horizontal Line Divider */
    public static final String HORIZONTAL_LINE = "____________________________________________________________";
    /** Custom Banner for the Linus chatbot */
    public static final String BANNER = " _     _                 \n"
            + "| |   (_)_ __  _   _ ___ \n"
            + "| |   | | '_ \\| | | / __|\n"
            + "| |___| | | | | |_| \\__ \\\n"
            + "|_____|_|_| |_|\\__,_|___/\n";
    /** Scanner to scan input from command line */
    private final Scanner scanner;

    /**
     * Creates both a new Ui for the chatbot and the scanner
     * to receive the user input.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Prints the default welcome message when starting the Linus
     * chatbot.
     */
    public static void hello() {
        Ui.echo(Ui.BANNER +
                "\nHello! My name is Linus.\nHow may I help you today?");
    }

    /**
     * Reads the next line of user input using the scanner.
     *
     * @return Input from command line from user.
     */
    public String read() {
        return this.scanner.nextLine();
    }

    /**
     * Prints the default goodbye message when the user exits.
     */
    public static void bye() {
        Ui.echo("Bye! Hope to see you again soon!");
    }

    /**
     * Prints the input text to the command line.
     * Formatted with horizontal line above and below for spacing.
     *
     * @param text Text to be printed to the screen.
     */
    public static void echo(String text) {
        System.out.println(Ui.HORIZONTAL_LINE);
        System.out.println(text);
        System.out.println(Ui.HORIZONTAL_LINE);
    }
}
