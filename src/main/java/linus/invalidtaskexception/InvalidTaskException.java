package linus.invalidtaskexception;

/**
 * Represents an exception thrown when the Task to be added to TaskList
 * contains invalid input or arguments.
 */
public class InvalidTaskException extends Exception{
    /**
     * Creates an InvalidTaskException with the specified message.
     *
     * @param message the details of the error causing the exception.
     */
    public InvalidTaskException(String message) {
        super(message);
    }
}
