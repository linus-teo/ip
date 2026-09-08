package linus.executor;

import java.time.LocalDate;
import java.util.List;

import linus.invalidtaskexception.InvalidTaskException;
import linus.storage.Storage;
import linus.task.Deadline;
import linus.task.Event;
import linus.task.Task;
import linus.task.ToDo;
import linus.validator.Validator;

/**
 * Represents an executor that carries out specified actions on a tasklist.
 */
public class Executor {
    private final List<Task> taskList;
    private final Storage storage;

    /**
     * Creates a new Executor.
     *
     * @param taskList The tasklist to be actioned on by the executor.
     * @param storage Local hard drive storage location of the tasklist.
     */
    public Executor(List<Task> taskList, Storage storage) {
        this.taskList = taskList;
        this.storage = storage;
    }

    /**
     * Executes the action based on the command from user input.
     *
     * @param parsedInput Parsed user input from the parser.
     * @return Message that describes the status of the execution of the command.
     * @throws InvalidTaskException If addTask() does not recognise input, throw InvalidTaskException.
     */
    public String execute(List<String> parsedInput) throws InvalidTaskException {
        assert parsedInput != null : "Parsed input to execute is null";
        assert !parsedInput.isEmpty() : "No parsed input provided for execution";
        String command = parsedInput.getFirst();
        switch (command) {
            case "list":
                assert parsedInput.size() == 1 : "Invalid format for list command";
                return this.listAll();
            case "mark":
                assert parsedInput.size() == 2 : "Invalid format for mark command";
                return this.mark(Integer.parseInt(parsedInput.getLast()));
            case "unmark":
                assert parsedInput.size() == 2 : "Invalid format for unmark command";
                return this.unmark(Integer.parseInt(parsedInput.getLast()));
            case "delete":
                assert parsedInput.size() == 2 : "Invalid format for delete command";
                return this.delete(Integer.parseInt(parsedInput.getLast()));
            case "find":
                assert parsedInput.size() == 2 : "Invalid format for find command";
                return this.find(parsedInput.getLast());
            case "todo":
            case "deadline":
            case "event":
                return this.addTask(command, parsedInput);
            default:
                // Should not reach here
                return "Unknown command could not be executed";
        }
    }

    /**
     * Lists all the tasks in the tasklist.
     *
     * @return String representation of all tasks in the tasklist.
     */
    public String listAll() {
        assert this.taskList != null : "The tasklist to be listed is null";
        StringBuilder string = new StringBuilder("Here are the tasks in your list: ");
        int length = this.taskList.size();
        for (int i = 0; i < length; i++) {
            string.append("\n");
            string.append(i + 1);
            string.append(". ");
            string.append(this.taskList.get(i));
        }
        return string.toString();
    }

    /**
     * Marks the selected task as completed.
     *
     * @param position The task number to be marked as completed.
     * @return Message to indicate successful marking of task.
     */
    public String mark(int position) {
        assert this.taskList != null : "The tasklist is null and has not been initialised";
        int taskId = position - 1;
        Task task = this.taskList.get(taskId);
        assert task != null : "The task to be marked is null";
        task.mark();
        assert this.storage != null : "The storage is null and has not been initialised";
        this.storage.saveFile(this.taskList);
        String output = "Nice! I've marked this task as done: \n" + task;
        return output;
    }

    /**
     * Marks the selected task as not completed.
     *
     * @param position The task number to be marked as incomplete.
     * @return Message to indicate successful unmarking of task.
     */
    public String unmark(int position) {
        assert this.taskList != null : "The tasklist is null and has not been initialised";
        int taskId = position - 1;
        Task task = this.taskList.get(taskId);
        assert task != null : "The task to be unmarked is null";
        task.unmark();
        assert this.storage != null : "The storage is null and has not been initialised";
        this.storage.saveFile(this.taskList);
        String output = "OK, I've marked this task as not done yet: \n" + task;
        return output;
    }

    /**
     * Deletes the selected task from the tasklist.
     *
     * @param position The task number to be deleted from the task list.
     * @return Message to indicate successful deletion of task.
     */
    public String delete(int position) {
        assert this.taskList != null : "The tasklist is null and has not been initialised";
        int taskId = position - 1;
        Task task = this.taskList.get(taskId);
        assert task != null : "The task to be deleted is null";
        this.taskList.remove(task);
        assert this.storage != null : "The storage is null and has not been initialised";
        this.storage.saveFile(this.taskList);
        StringBuilder output = new StringBuilder("Noted. I've removed this task: \n");
        output.append(task).append("\nNow you have " + this.taskList.size() + " tasks in the list.");
        return output.toString();
    }

    /**
     * Search the tasklist to find tasks with the specified keyword in the task description.
     *
     * @param keyword The string to search for in the task description of all tasks.
     * @return Message containing all the tasks whose description contains keyword.
     */
    public String find(String keyword) {
        assert this.taskList != null : "The tasklist is null and has not been initialised";
        StringBuilder string = new StringBuilder("Here are the matching tasks in your list: ");
        for (int i = 0; i < this.taskList.size(); i++) {
            Task currentTask = this.taskList.get(i);
            assert currentTask != null : "The current task to be searched for the keyword is null";
            if (currentTask.getDescription().contains(keyword)) {
                string.append("\n");
                string.append(i + 1);
                string.append(". ");
                string.append(this.taskList.get(i));
            }
        }
        return string.toString();
    }

    /**
     * Adds the specified task to the TaskList.
     *
     * @param command The string that describes which type of task to add.
     * @param parsedInput The parsed plaintext input from the user.
     * @return Message to indicate successful addition of the task.
     * @throws InvalidTaskException If the command is not recognised. Should not reach this point.
     */
    public String addTask(String command, List<String> parsedInput) throws InvalidTaskException {
        assert parsedInput != null : "Parsed input to execute is null";
        assert !parsedInput.isEmpty() : "No parsed input provided for execution";
        assert parsedInput.size() >= 2 : "Invalid format for todo/deadline/event add task command";
        String description = parsedInput.get(1);
        Task task;
        switch (command) {
            case "todo":
                assert parsedInput.size() == 2 : "Invalid number of details provided for a todo command";
                task = new ToDo(false, description);
                break;
            case "deadline":
                assert parsedInput.size() == 3 : "Invalid number of details provided for a deadline command";
                LocalDate deadline = LocalDate.parse(parsedInput.getLast(), Validator.DATE_FORMAT);
                task = new Deadline(false, description, deadline);
                break;
            case "event":
                assert parsedInput.size() == 4: "Invalid number of details provided for an event command";
                LocalDate startDate = LocalDate.parse(parsedInput.get(2), Validator.DATE_FORMAT);
                LocalDate endDate = LocalDate.parse(parsedInput.getLast(), Validator.DATE_FORMAT);
                task = new Event(false, description, startDate, endDate);
                break;
            default:
                // Should not be reached.
                throw new InvalidTaskException("OOPS!!! I'm sorry, but I don't know what that means :-(");
        }
        assert this.taskList != null : "The tasklist is null and has not been initialised";
        this.taskList.add(task);
        assert this.storage != null : "The storage is null and has not been initialised";
        this.storage.saveFile(this.taskList);
        String output = "Got it. I've added this task: \n" + task + "\n"
                + "Now you have " + this.taskList.size() + " tasks in the list.";
        return output;
    }
}
