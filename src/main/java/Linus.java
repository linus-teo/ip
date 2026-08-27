import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.DateTimeException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.lang.StringBuilder;
import java.time.LocalDate;

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
        try {
            this.loadFile();
        } catch (IOException e) {
            this.echo(e.getMessage());
        }
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
                String deadlineText = text.substring(byIndex + 5);
                LocalDate deadline = LocalDate.parse(deadlineText);
                task = new Deadline(false, description, deadline);
            } else if (text.matches("^event .+ /from .+ /to .+$")) {
                int fromIndex = text.indexOf(" /from");
                int toIndex = text.indexOf(" /to");
                String description = text.substring(6, fromIndex);
                String startText = text.substring(fromIndex + 7, toIndex);
                String endText = text.substring(toIndex + 5);
                LocalDate start = LocalDate.parse(startText);
                LocalDate end = LocalDate.parse(endText);
                task = new Event(false, description, start, end);
            } else {
                throw new InvalidTaskException("OOPS!!! I'm sorry, but I don't know what that means :-(");
            }
            this.taskList.add(task);
            this.saveFile();
            StringBuilder output = new StringBuilder("Got it. I've added this task: \n");
            output.append(task).append("\n");
            output.append("Now you have " + this.taskList.size() + " tasks in the list.");
            this.echo(output.toString());
        } catch (InvalidTaskException e) {
            this.echo(e.getMessage());
        } catch (DateTimeException e) {
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
        this.saveFile();
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
        this.saveFile();
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
        this.saveFile();
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

    public void loadFile() throws IOException {
        Path path = Paths.get("data", "tasklist.txt");
        if (!Files.exists(path)) {
            Files.createDirectories(path.getParent());
            Files.createFile(path);
            return;
        }
        Scanner scanner = new Scanner(path);
        while (scanner.hasNextLine()) {
            String task = scanner.nextLine();
            String[] parts = task.split("\\s*\\|\\s*");
            switch (parts[0]) {
                case("T"):
                    boolean isDone = parts[1].equals("X");
                    String description = parts[2];
                    this.taskList.add(new ToDo(isDone, description));
                    break;
                case("D"):
                    isDone = parts[1].equals("X");
                    description = parts[2];
                    String deadlineText = parts[3];
                    LocalDate deadline = LocalDate.parse(deadlineText);
                    this.taskList.add(new Deadline(isDone, description, deadline));
                    break;
                case("E"):
                    isDone = parts[1].equals("X");
                    description = parts[2];
                    String startText = parts[3];
                    String endText = parts[4];
                    LocalDate start = LocalDate.parse(startText);
                    LocalDate end = LocalDate.parse(endText);
                    this.taskList.add(new Event(isDone, description, start, end));
                    break;
            }
        }
        scanner.close();
    }

    public void saveFile() {
        try {
            FileWriter fileWriter = new FileWriter("data/tasklist.txt");
            for (int i = 0; i < taskList.size(); i++) {
                Task task = taskList.get(i);
                fileWriter.append(task.toFileFormat());
            }
            fileWriter.close();
        } catch (IOException e) {
            this.echo(e.getMessage());
        }
    }

    public void bye() {
        System.out.println(Linus.HORIZONTAL_LINE);
        System.out.println("Bye! Hope to see you again soon!");
        System.out.println(Linus.HORIZONTAL_LINE);
    }
}
