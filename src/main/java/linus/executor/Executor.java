package linus.executor;

import java.time.LocalDate;
import java.util.List;

import linus.invalidtaskexception.InvalidTaskException;
import linus.storage.Storage;
import linus.task.Deadline;
import linus.task.Event;
import linus.task.Task;
import linus.task.ToDo;
import linus.ui.Ui;
import linus.validator.Validator;

/**
 * Represents a controller that manages and edits the tasklist.
 */
public class Executor {
    private final List<Task> taskList;
    private final Storage storage;

    /**
     * Creates a new TaskManager.
     *
     * @param taskList TaskList to be managed by the TaskManager.
     * @param storage Local hard drive storage location of the TaskList.
     */
    public Executor(List<Task> taskList, Storage storage) {
        this.taskList = taskList;
        this.storage = storage;
    }

    /**
     * Executes the action based on the command from user input.
     *
     * @param parsedInput Parsed user input from Parser.
     * @throws InvalidTaskException If addTask() does not recognise input, throw InvalidTaskException
     */
    public void execute(List<String> parsedInput) throws InvalidTaskException {
        String command = parsedInput.getFirst();
        switch (command) {
            case "list":
                this.listAll();
                break;
            case "mark":
                this.mark(Integer.parseInt(parsedInput.getLast()));
                break;
            case "unmark":
                this.unmark(Integer.parseInt(parsedInput.getLast()));
                break;
            case "delete":
                this.delete(Integer.parseInt(parsedInput.getLast()));
                break;
            case "find":
                this.find(parsedInput.getLast());
                break;
            case "todo":
            case "deadline":
            case "event":
                this.addTask(command, parsedInput);
                break;
            default:
                break;
        }
    }
    /**
     * Lists all the tasks in the TaskList.
     * Prints the String representation of each task in the TaskList to the command line.
     */
    public void listAll() {
        StringBuilder string = new StringBuilder("Here are the tasks in your list: ");
        int length = this.taskList.size();
        for (int i = 0; i < length; i++) {
            string.append("\n");
            string.append(i + 1);
            string.append(". ");
            string.append(this.taskList.get(i));
        }
        Ui.echo(string.toString());
    }

    /**
     * Marks the selected task as completed.
     *
     * @param position The plaintext String input from the user.
     */
    public void mark(int position) {
        int taskId = position - 1;
        Task task = this.taskList.get(taskId);
        task.mark();
        this.storage.saveFile(this.taskList);
        String output = "Nice! I've marked this task as done: \n" + task;
        Ui.echo(output);
    }

    /**
     * Marks the selected task as not completed.
     *
     * @param position The plaintext input from the user.
     */
    public void unmark(int position) {
        int taskId = position - 1;
        Task task = this.taskList.get(taskId);
        task.unmark();
        this.storage.saveFile(this.taskList);
        String output = "OK, I've marked this task as not done yet: \n" + task;
        Ui.echo(output);
    }

    /**
     * Deletes the selected task from the TaskList.
     *
     * @param position The plaintext input from the user.
     */
    public void delete(int position) {
        int taskId = position - 1;
        Task task = this.taskList.get(taskId);
        this.taskList.remove(task);
        this.storage.saveFile(this.taskList);
        StringBuilder output = new StringBuilder("Noted. I've removed this task: \n");
        output.append(task).append("\nNow you have " + this.taskList.size() + " tasks in the list.");
        Ui.echo(output.toString());
    }

    /**
     * Search the TaskList to find tasks with the specified keyword in the task description.
     *
     * @param keyword The plaintext input from the user.
     */
    public void find(String keyword) {
        StringBuilder string = new StringBuilder("Here are the matching tasks in your list: ");
        for (int i = 0; i < taskList.size(); i++) {
            Task currentTask = taskList.get(i);
            if (currentTask.getDescription().contains(keyword)) {
                string.append("\n");
                string.append(i + 1);
                string.append(". ");
                string.append(this.taskList.get(i));
            }
        }
        Ui.echo(string.toString());
    }

    /**
     * Adds the specified task to the TaskList.
     *
     * @param command The plaintext input from the user.
     */
    public void addTask(String command, List<String> parsedInput) throws InvalidTaskException {
        String description = parsedInput.get(1);
        Task task;
        switch (command) {
            case "todo":
                task = new ToDo(false, description);
                break;
            case "deadline":
                LocalDate deadline = LocalDate.parse(parsedInput.getLast(), Validator.DATE_FORMAT);
                task = new Deadline(false, description, deadline);
                break;
            case "event":
                LocalDate startDate = LocalDate.parse(parsedInput.get(2), Validator.DATE_FORMAT);
                LocalDate endDate = LocalDate.parse(parsedInput.getLast(), Validator.DATE_FORMAT);
                task = new Event(false, description, startDate, endDate);
                break;
            default:
                throw new InvalidTaskException("OOPS!!! I'm sorry, but I don't know what that means :-(");
        }
        this.taskList.add(task);
        this.storage.saveFile(this.taskList);
        String output = "Got it. I've added this task: \n" + task + "\n"
                + "Now you have " + this.taskList.size() + " tasks in the list.";
        Ui.echo(output);
    }
}
