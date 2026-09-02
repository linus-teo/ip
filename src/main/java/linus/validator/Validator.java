package linus.validator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import linus.invalidtaskexception.InvalidTaskException;
import linus.task.Task;

/**
 * Represents a validator that ensures input follows rules for acting on a tasklist.
 */
public class Validator {
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final List<Task> taskList;

    /**
     * Creates a new Validator to validate input.
     *
     * @param taskList List of tasks it validates input against.
     */
    public Validator(List<Task> taskList) {
        this.taskList = taskList;
    }

    /**
     * Validate that the parsed input follows business rules.
     *
     * @param parsedInput Parsed input from Parser.
     * @throws InvalidTaskException If description is blank.
     * @throws NumberFormatException If number is negative, 0 or larger than size of tasklist.
     * @throws DateTimeParseException If date does not follow yyyy-MM-dd format.
     */
    public void validate(List<String> parsedInput) throws InvalidTaskException, NumberFormatException,
            DateTimeParseException {
        String command = parsedInput.getFirst();
        switch (command) {
            case "find":
            case "list":
            case "todo":
                if (parsedInput.getLast().isBlank()) {
                    throw new InvalidTaskException("OOPS!!! Please enter a valid task description :-(");
                }
                break;
            case "mark":
            case "unmark":
            case "delete":
                int index = Integer.parseInt(parsedInput.get(1));
                if (index < 1 || index > this.taskList.size()) {
                    throw new InvalidTaskException("OOPS!!! Please enter a valid task ID :-(");
                }
                break;
            case "deadline":
                if (parsedInput.get(1).isBlank()) {
                    throw new InvalidTaskException("OOPS!!! Please enter a valid task description :-(");
                }
                LocalDate.parse(parsedInput.getLast(), Validator.DATE_FORMAT);
                break;
            case "event":
                if (parsedInput.get(1).isBlank()) {
                    throw new InvalidTaskException("OOPS!!! Please enter a valid task description :-(");
                }
                LocalDate.parse(parsedInput.get(2), Validator.DATE_FORMAT);
                LocalDate.parse(parsedInput.getLast(), Validator.DATE_FORMAT);
                break;
            default:
                throw new InvalidTaskException("OOPS!!! I'm sorry, but I don't know what that means :-(");
        }
    }
}
