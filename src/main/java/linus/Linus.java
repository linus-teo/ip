package linus;

import java.io.IOException;
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
    private final Ui ui;
    private final Parser parser;
    private final Validator validator;
    private final Executor executor;

    /**
     * Creates a new Linus chatbot.
     * Constructs the Ui, Parser, Validator, Executor and Storage for the Linus chatbot.
     *
     * @param filePath Path that the storage file is located.
     */
    public Linus(String filePath) throws IOException, InvalidTaskException {
        this.ui = new Ui();
        this.parser = new Parser();
        Storage storage = new Storage(filePath);
        List<Task> taskList = storage.loadFile();
        this.validator = new Validator(taskList);
        this.executor = new Executor(taskList, storage);
    }

    /**
     * Creates and starts up the chatbot.
     */
    static void main() {
        try {
            Linus chatbot = new Linus("data/tasklist.txt");
            chatbot.run();
        } catch (IOException | InvalidTaskException e) {
            Ui.display(e.getMessage());
        }
    }

    /**
     * Starts up the chatbot by loading the Ui and scanning
     * for user input to execute commands.
     */
    public void run() {
        Ui.sayHello();
        while (true) {
            String input = this.ui.read();
            boolean isBye = processInput(input);
            if (isBye) {
                break;
            }
        }
        Ui.sayBye();
    }

    private boolean processInput(String input) {
        String response = this.getResponse(input);
        if (response.equals("bye")) {
            return true;
        }
        Ui.display(response);
        return false;
    }

    /**
     * Generates a response for the user's chat message.
     * Method is meant to be used for GUI display.
     *
     * @param input The plaintext String input from the user.
     * @return Response message after attempting to execute command.
     */
    public String getResponse(String input) {
        try {
            List<String> parsedInput = this.parser.parse(input);
            if (parsedInput.equals(List.of("bye"))) {
                return input;
            }
            this.validator.validate(parsedInput);
            return this.executor.execute(parsedInput);
        } catch (IOException | InvalidTaskException e) {
            return e.getMessage();
        } catch (NumberFormatException e) {
            return "Here's a tech tip! Enter a valid task ID :-(";
        } catch (DateTimeParseException e) {
            return "Here's a tech tip! Enter a valid date in the format \"yyyy-MM-dd\" :-(";
        }
    }
}
