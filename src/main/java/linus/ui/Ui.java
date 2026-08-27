package linus.ui;

import java.util.Scanner;

public class Ui {
    public static final String HORIZONTAL_LINE = "____________________________________________________________";
    public static final String BANNER = " _     _                 \n"
            + "| |   (_)_ __  _   _ ___ \n"
            + "| |   | | '_ \\| | | / __|\n"
            + "| |___| | | | | |_| \\__ \\\n"
            + "|_____|_|_| |_|\\__,_|___/\n";
    private final Scanner scanner;

    public Ui() {
        this.scanner = new Scanner(System.in);
    }


    public static void hello() {
        Ui.echo(Ui.BANNER +
                "\nHello! My name is Linus.\nHow may I help you today?");
    }

    public String read() {
        return this.scanner.nextLine();
    }

    public static void bye() {
        Ui.echo("Bye! Hope to see you again soon!");
    }

    public static void echo(String command) {
        System.out.println(Ui.HORIZONTAL_LINE);
        System.out.println(command);
        System.out.println(Ui.HORIZONTAL_LINE);
    }
}
