package linus.taskmanager;

import linus.invalidtaskexception.InvalidTaskException;
import linus.storage.Storage;
import linus.task.Deadline;
import linus.task.Event;
import linus.task.Task;
import linus.task.ToDo;
import linus.tasklist.TaskList;
import linus.ui.Ui;

import java.time.DateTimeException;
import java.time.LocalDate;

/**
 * Represents a controller that manages and edits the tasklist.
 */
public class TaskManager {
    private final TaskList taskList;
    private final Storage storage;

    /**
     * Creates a new TaskManager.
     *
     * @param taskList TaskList to be managed by the TaskManager.
     * @param storage Local hard drive storage location of the TaskList.
     */
    public TaskManager(TaskList taskList, Storage storage) {
        this.taskList = taskList;
        this.storage = storage;
    }

    /**
     * Executes the action on the TaskList based on the given command and input.
     *
     * @param command The action to be taken by the TaskManager.
     * @param input The plaintext input from the user which contains further information about the action.
     * @return Whether the chatbot should terminate after executing the action.
     * If the chatbot receives the "bye" command, chatbot should terminate, method returns true.
     * If the chatbot receives any other command, chatbot should continue receiving input, method returns false.
     */
    public boolean execute(String command, String input) {
        switch (input) {
            case "bye":
                return true;
            case "list":
                this.listAll();
                return false;
        }
        switch (command) {
            case "mark":
                this.mark(input);
                return false;
            case "unmark":
                this.unmark(input);
                return false;
            case "delete":
                this.delete(input);
                return false;
            case "todo":
            case "deadline":
            case "event":
                this.addToList(input);
                return false;
            default:
                Ui.echo("OOPS!!! I'm sorry, but I don't know what that means :-(");
                return false;
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
     * @param input The plaintext String input from the user.
     */
    public void mark(String input) {
        double position = Double.parseDouble(input.substring(5));
        if (position < 1 || position > this.taskList.size() || position != Math.floor(position)) {
            Ui.echo("OOPS!!! The task number entered is invalid!");
            return;
        }
        int taskId = (int) position - 1;
        Task task = this.taskList.get(taskId);
        task.mark();
        this.storage.saveFile(this.taskList);
        String output = "Nice! I've marked this task as done: \n" + task;
        Ui.echo(output);
    }

    /**
     * Marks the selected task as not completed.
     *
     * @param input The plaintext input from the user.
     */
    public void unmark(String input) {
        double position = Double.parseDouble(input.substring(7));
        if (position < 1 || position > this.taskList.size() || position != Math.floor(position)) {
            Ui.echo("OOPS!!! The task number entered is invalid!");
            return;
        }
        int taskId = (int) position - 1;
        Task task = this.taskList.get(taskId);
        task.unmark();
        this.storage.saveFile(this.taskList);
        String output = "OK, I've marked this task as not done yet: \n" + task;
        Ui.echo(output);
    }

    /**
     * Deletes the selected task from the TaskList.
     *
     * @param input The plaintext input from the user.
     */
    public void delete(String input) {
        double position = Double.parseDouble(input.substring(7));
        if (position < 1 || position > this.taskList.size() || position != Math.floor(position)) {
            Ui.echo("OOPS!!! The task number entered is invalid!");
            return;
        }
        int taskId = (int) position - 1;
        Task task = this.taskList.get(taskId);
        this.taskList.delete(task);
        this.storage.saveFile(this.taskList);
        StringBuilder output = new StringBuilder("Noted. I've removed this task: \n");
        output.append(task).append("\n");
        output.append("Now you have " + this.taskList.size() + " tasks in the list.");
        Ui.echo(output.toString());
    }

    /**
     * Adds the specified task to the TaskList.
     *
     * @param text The plaintext input from the user.
     */
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
                throw new InvalidTaskException("OOPS!!! The format of the task is invalid!");
            }
            this.taskList.add(task);
            this.storage.saveFile(this.taskList);
            StringBuilder output = new StringBuilder("Got it. I've added this task: \n");
            output.append(task).append("\n");
            output.append("Now you have " + this.taskList.size() + " tasks in the list.");
            Ui.echo(output.toString());
        } catch (InvalidTaskException e) {
            Ui.echo(e.getMessage());
        } catch (DateTimeException e) {
            Ui.echo("OOPS!!! The date entered is invalid!");
        }
    }
}
