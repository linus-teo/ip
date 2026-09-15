package linus.validator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import linus.invalidtaskexception.InvalidTaskException;
import linus.task.Task;

/**
 * Represents a validator that ensures input is valid for acting on a specific tasklist.
 */
public class Validator {
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final String EMPTY_DESCRIPTION_ERROR = "OOPS!!! Please enter a valid task description :-(";
    private static final String INVALID_TASK_INDEX_ERROR = "OOPS!!! Please enter a valid task ID :-(";
    private static final String UNKNOWN_COMMAND_ERROR = "OOPS!!! I'm sorry, but I don't know what that means :-(";

    /** The tasklist to validate the input against. */
    private final List<Task> taskList;

    /**
     * Creates a new Validator to validate input.
     *
     * @param taskList List of tasks to validate input against.
     */
    public Validator(List<Task> taskList) {
        this.taskList = taskList;
    }

    /**
     * Validates that the parsed input is valid for the specific tasklist.
     *
     * @param parsedInput Parsed input from the parser.
     * @throws InvalidTaskException If description is blank or task number is negative, 0 or larger than tasklist size.
     * @throws NumberFormatException If the task ID is not a valid integer.
     * @throws DateTimeParseException If date does not follow yyyy-MM-dd format.
     */
    public void validate(List<String> parsedInput) throws InvalidTaskException, NumberFormatException,
            DateTimeParseException {
        assert parsedInput != null : "Parsed input provided for validation is null";
        assert !parsedInput.isEmpty() : "No parsed input provided for validation";
        String command = parsedInput.getFirst();
        if (command.equals("list")) {
            return;
        }
        String description = parsedInput.get(1);
        switch (command) {
            case "find":
                // Fallthrough
            case "todo":
                assert parsedInput.size() == 2 : "Invalid number of details provided for a find/todo command";
                this.validateDescription(description);
                break;
            case "mark":
                // Fallthrough
            case "unmark":
                // Fallthrough
            case "delete":
                assert parsedInput.size() == 2 : "Invalid number of details provided for a mark/unmark/delete command";
                // For mark/unmark/delete command, the description represents the task index.
                this.validateTaskIndex(description);
                break;
            case "deadline":
                assert parsedInput.size() == 3 : "Invalid number of details provided for a deadline command";
                this.validateDescription(description);
                String deadline = parsedInput.getLast();
                this.validateDateFormat(deadline);
                break;
            case "event":
                assert parsedInput.size() == 4 : "Invalid number of details provided for an event command";
                this.validateDescription(description);
                String startDate = parsedInput.get(2);
                String endDate = parsedInput.getLast();
                this.validateDateFormat(startDate);
                this.validateDateFormat(endDate);
                break;
            default:
                // Should not reach here.
                throw new InvalidTaskException(UNKNOWN_COMMAND_ERROR);
        }
    }

    private void validateDescription(String description) throws InvalidTaskException {
        if (description.isBlank()) {
            throw new InvalidTaskException(EMPTY_DESCRIPTION_ERROR);
        }
    }

    private void validateTaskIndex(String index) throws NumberFormatException, InvalidTaskException {
        int taskIndex = Integer.parseInt(index);
        if (taskIndex < 1 || taskIndex > this.taskList.size()) {
            throw new InvalidTaskException(INVALID_TASK_INDEX_ERROR);
        }
    }

    private void validateDateFormat(String date) throws DateTimeParseException {
        LocalDate.parse(date, Validator.DATE_FORMAT);
    }
}
