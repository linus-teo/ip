package linus.parser;

/**
 * Represents a parser that takes the plaintext input from
 * the command line and makes sense of the input
 */
public class Parser {
    /**
     * Returns the substring before the first space which
     * represents the command to be executed
     *
     * @param input Plaintext string input from user
     * @return Command action to be executed
     */
    public String parse(String input) {
        return input.split(" ")[0];
    }
}
