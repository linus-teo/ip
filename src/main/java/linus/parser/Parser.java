package linus.parser;

import java.util.List;

import linus.invalidtaskexception.InvalidTaskException;
/**
 * Represents a parser that takes the plaintext input from
 * the command line and makes sense of the input.
 */
public class Parser {
    private static final String BYE_COMMAND = "bye";
    private static final String LIST_COMMAND = "list";
    private static final String DEADLINE_COMMAND = "deadline";
    private static final String EVENT_COMMAND = "event";

    private static final String SIMPLE_COMMAND_REGEX = "^(mark|unmark|delete|find|todo) .+$";
    private static final String DEADLINE_COMMAND_REGEX = "^deadline .+ /by .+$";
    private static final String EVENT_COMMAND_REGEX = "^event .+ /from .+ /to .+$";

    private static final String DEADLINE_PREFIX = "deadline ";
    private static final String BY_SEPARATOR = " /by ";
    private static final String EVENT_PREFIX = "event ";
    private static final String FROM_SEPARATOR = " /from ";
    private static final String TO_SEPARATOR = " /to ";


    /**
     * Returns a list of strings containing the parsed input.
     * Only parses the input, does not do validation of arguments.
     *
     * @param input Plaintext string input from user.
     * @return Parsed input as a List of Strings.
     */
    public List<String> parse(String input) throws InvalidTaskException {
        if (input.equals(BYE_COMMAND)) {
            return List.of(BYE_COMMAND);
        } else if (input.equals(LIST_COMMAND)) {
            return List.of(LIST_COMMAND);
        } else if (input.matches(SIMPLE_COMMAND_REGEX)) {
            return this.parseSimpleCommand(input);
        } else if (input.matches(DEADLINE_COMMAND_REGEX)) {
            return this.parseDeadlineCommand(input);
        } else if (input.matches(EVENT_COMMAND_REGEX)) {
            return this.parseEventCommand(input);
        } else {
            throw new InvalidTaskException("OOPS!!! Please re-enter the command with a valid format! :-(");
        }
    }

    private List<String> parseSimpleCommand(String input) {
        String[] parts = input.split(" ", 2);
        return List.of(parts[0], parts[1]);
    }

    private List<String> parseDeadlineCommand(String input) {
        int byIndex = input.indexOf(BY_SEPARATOR);
        String description = input.substring(DEADLINE_PREFIX.length(), byIndex);
        String date = input.substring(byIndex + BY_SEPARATOR.length());
        return List.of(DEADLINE_COMMAND, description, date);
    }

    private List<String> parseEventCommand(String input) {
        int fromIndex = input.indexOf(FROM_SEPARATOR);
        int toIndex = input.indexOf(TO_SEPARATOR);
        String description = input.substring(EVENT_PREFIX.length(), fromIndex);
        String startDate = input.substring(fromIndex + FROM_SEPARATOR.length(), toIndex);
        String endDate = input.substring(toIndex + TO_SEPARATOR.length());
        return List.of(EVENT_COMMAND, description, startDate, endDate);
    }
}
