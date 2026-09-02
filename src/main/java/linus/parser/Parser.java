package linus.parser;

import java.util.List;

import linus.invalidtaskexception.InvalidTaskException;
/**
 * Represents a parser that takes the plaintext input from
 * the command line and makes sense of the input.
 */
public class Parser {
    private static final String DEADLINE_PREFIX = "deadline ";
    private static final String BY_SEPARATOR = " /by ";
    private static final String EVENT_PREFIX = "event ";
    private static final String FROM_SEPARATOR = " /from ";
    private static final String TO_SEPARATOR = " /to ";


    /**
     * Returns a List of Strings containing the parsed input.
     * Only parses the input, does not do validation of arguments.
     *
     * @param input Plaintext string input from user.
     * @return Parsed input as a List of Strings.
     */
    public List<String> parse(String input) throws InvalidTaskException {
        switch (input) {
            case "bye":
                return List.of("bye");
            case "list":
                return List.of("list");
            default:
                break;
        }
        if (input.matches("^(mark|unmark|delete) -?\\d+$")) {
            String[] parts = input.split(" ", 2);
            return List.of(parts[0], parts[1]);
        } else if (input.matches("^(find|todo) (.+)$")) {
            String[] parts = input.split(" ", 2);
            return List.of(parts[0], parts[1]);
        } else if (input.matches("^deadline .+ /by .+$")) {
            String[] parts = input.split(" ", 2);
            int byIndex = input.indexOf(Parser.BY_SEPARATOR);
            String description = input.substring(Parser.DEADLINE_PREFIX.length(), byIndex);
            String date = input.substring(byIndex + Parser.BY_SEPARATOR.length());
            return List.of(parts[0], description, date);
        } else if (input.matches("^event .+ /from .+ /to .+$")) {
            String[] parts = input.split(" ", 2);
            int fromIndex = input.indexOf(Parser.FROM_SEPARATOR);
            int toIndex = input.indexOf(Parser.TO_SEPARATOR);
            String description = input.substring(Parser.EVENT_PREFIX.length(), fromIndex);
            String startDate = input.substring(fromIndex + Parser.FROM_SEPARATOR.length(), toIndex);
            String endDate = input.substring(toIndex + Parser.TO_SEPARATOR.length());
            return List.of(parts[0], description, startDate, endDate);
        } else {
            throw new InvalidTaskException("OOPS!!! Please re-enter the command with a valid format! :-(");
        }
    }
}
