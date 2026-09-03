package linus;

import java.time.format.DateTimeParseException;
import java.util.List;

import linus.executor.Executor;
import linus.invalidtaskexception.InvalidTaskException;
import linus.parser.Parser;
import linus.storage.Storage;
import linus.task.Task;
import linus.ui.Ui;
import linus.validator.Validator;


/**
 * Represents the Linus chatbot which orchestrates the logical flow.
 */
public class Linus {
    private Ui ui;
    private Parser parser;
    private Validator validator;
    private Executor executor;
    private Storage storage;

    /**
     * Creates a new Linus chatbot.
     * Constructs the Ui, Parser, Validator, Executor and Storage for the Linus chatbot.
     *
     * @param filePath Path that the storage file is located.
     */
    public Linus(String filePath) {
        this.ui = new Ui();
        this.parser = new Parser();
        this.storage = new Storage(filePath);
        List<Task> taskList = this.storage.loadFile();
        this.validator = new Validator(taskList);
        this.executor = new Executor(taskList, this.storage);
    }

    /**
     * Creates and starts up the chatbot.
     */
    public static void main() {
        Linus chatbot = new Linus("data/tasklist.txt");
        chatbot.run();
    }

    /**
     * Starts up the chatbot by loading the Ui and scanning
     * for user input to execute commands.
     */
    public void run() {
        Ui.hello();
        while (true) {
            String input = this.ui.read();
            try {
                List<String> parsedInput = this.parser.parse(input);
                if (parsedInput.equals(List.of("bye"))) {
                    break;
                }
                this.validator.validate(parsedInput);
                this.executor.execute(parsedInput);
            } catch (InvalidTaskException e) {
                Ui.echo(e.getMessage());
            } catch (NumberFormatException e) {
                Ui.echo("OOPS!!! Please enter a valid task ID :-(");
            } catch (DateTimeParseException e) {
                Ui.echo("OOPS!!! Please enter a valid date in the format \"yyyy-MM-dd\" :-(");
            }
        }
        Ui.bye();
    }
}
