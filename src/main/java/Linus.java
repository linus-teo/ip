import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.lang.StringBuilder;

public class Linus {
    public static final String HORIZONTAL_LINE = "____________________________________________________________";
    public static final String BANNER = " _     _                 \n"
            + "| |   (_)_ __  _   _ ___ \n"
            + "| |   | | '_ \\| | | / __|\n"
            + "| |___| | | | | |_| \\__ \\\n"
            + "|_____|_|_| |_|\\__,_|___/\n";
    private List<String> list;

    public static void main(String[] args) {
        Linus chatbot = new Linus();
        Scanner scanner = new Scanner(System.in);
        chatbot.hello();
        chatbot.parse(scanner);
    }

    public Linus() {
        this.list = new ArrayList<>();
    }

    public void hello() {
        System.out.println(Linus.HORIZONTAL_LINE);
        System.out.println(Linus.BANNER);
        System.out.println("Hello! My name is Linus.");
        System.out.println("How may I help you today?");
        System.out.println(Linus.HORIZONTAL_LINE);
    }

    public void parse(Scanner scanner) {
        while (true) {
            String command = scanner.nextLine();
            if (command.equals("bye")) {
                this.bye();
                break;
            }
            if (command.equals("list")){
                this.listAll();
                continue;
            }
            this.addToList(command);
        }
    }

    public void addToList(String text) {
        this.list.add("[ ] " + text);
        this.echo("added: " + text);
    }

    public void listAll() {
        StringBuilder string = new StringBuilder("Here are the tasks in your list: \n");
        int length = this.list.size();
        for (int i = 0; i < length; i++) {
            string.append(i + 1);
            string.append(". ");
            string.append(this.list.get(i));
            if (i == length - 1) {
                break;
            }
            string.append("\n");
        }
        this.echo(string.toString());
    }

    public void echo(String command) {
            System.out.println(Linus.HORIZONTAL_LINE);
            System.out.println(command);
            System.out.println(Linus.HORIZONTAL_LINE);
    }

    public void bye() {
        System.out.println(Linus.HORIZONTAL_LINE);
        System.out.println("Bye! Hope to see you again soon!");
        System.out.println(Linus.HORIZONTAL_LINE);
    }
}
