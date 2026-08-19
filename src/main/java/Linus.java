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
    private List<Task> taskList;

    public static void main(String[] args) {
        Linus chatbot = new Linus();
        Scanner scanner = new Scanner(System.in);
        chatbot.hello();
        chatbot.parse(scanner);
    }

    public Linus() {
        this.taskList = new ArrayList<>();
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
            if (command.matches("^mark -?\\d+(\\.\\d+)?$")) {
                double position = Double.parseDouble(command.substring(5));
                this.mark(position);
                continue;
            }
            if (command.matches("^unmark -?\\d+(\\.\\d+)?$")) {
                double position = Double.parseDouble(command.substring(7));
                this.unmark(position);
                continue;
            }
            if (command.matches("^delete -?\\d+(\\.\\d+)?$")) {
                double position = Double.parseDouble(command.substring(7));
                this.delete(position);
                continue;
            }
            this.addToList(command);
        }
    }

    public void addToList(String text) {
        Task task;
        try {
            if (text.matches("^todo\\s*$")) {
                throw new InvalidTaskException("OOPS!!! The description of a todo cannot be empty.");
            }
            else if (text.matches("^todo .*$")) {
                String description = text.substring(5);
                task = new ToDo(false, description);
            } else if (text.matches("^deadline .+ /by .+$")) {
                int byIndex = text.indexOf(" /by");
                String description = text.substring(9, byIndex);
                String deadline = text.substring(byIndex + 5);
                task = new Deadline(false, description, deadline);
            } else if (text.matches("^event .+ /from .+ /to .+$")) {
                int fromIndex = text.indexOf(" /from");
                int toIndex = text.indexOf(" /to");
                String description = text.substring(6, fromIndex);
                String start = text.substring(fromIndex + 7, toIndex);
                String end = text.substring(toIndex + 5);
                task = new Event(false, description, start, end);
            } else {
                throw new InvalidTaskException("OOPS!!! I'm sorry, but I don't know what that means :-(");
            }
            this.taskList.add(task);
            StringBuilder output = new StringBuilder("Got it. I've added this task: \n");
            output.append(task).append("\n");
            output.append("Now you have " + this.taskList.size() + " tasks in the list.");
            this.echo(output.toString());
        } catch (InvalidTaskException e) {
            this.echo(e.getMessage());
        }
    }

    public void listAll() {
        StringBuilder string = new StringBuilder("Here are the tasks in your list: \n");
        int length = this.taskList.size();
        for (int i = 0; i < length; i++) {
            string.append(i + 1);
            string.append(". ");
            string.append(this.taskList.get(i));
            if (i == length - 1) {
                break;
            }
            string.append("\n");
        }
        this.echo(string.toString());
    }

    public void mark(double position) {
        if (position < 1 || position > this.taskList.size() || position != Math.floor(position)) {
            System.out.println("Please enter a valid task number.");
            return;
        }
        int taskId = (int) position - 1;
        Task task = this.taskList.get(taskId);
        task.mark();
        String output = "Nice! I've marked this task as done: \n" + task;
        this.echo(output);
    }

    public void unmark(double position) {
        if (position < 1 || position > this.taskList.size() || position != Math.floor(position)) {
            System.out.println("Please enter a valid task number.");
            return;
        }
        int taskId = (int) position - 1;
        Task task = this.taskList.get(taskId);
        task.unmark();
        String output = "OK, I've marked this task as not done yet: \n" + task;
        this.echo(output);
    }

    public void delete(double position) {
        if (position < 1 || position > this.taskList.size() || position != Math.floor(position)) {
            System.out.println("Please enter a valid task number.");
            return;
        }
        int taskId = (int) position - 1;
        Task task = this.taskList.get(taskId);
        this.taskList.remove(taskId);
        StringBuilder output = new StringBuilder("Noted. I've removed this task: \n");
        output.append(task).append("\n");
        output.append("Now you have " + this.taskList.size() + " tasks in the list.");
        this.echo(output.toString());
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
