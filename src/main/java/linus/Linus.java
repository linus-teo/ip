package linus;
import linus.parser.Parser;
import linus.storage.Storage;
import linus.tasklist.TaskList;
import linus.taskmanager.TaskManager;
import linus.ui.Ui;

import java.io.FileNotFoundException;

/**
 * Represents the Linus chatbot which orchestrates the logical flow.
 */
public class Linus {
    private Ui ui;
    private Parser parser;
    private TaskManager taskManager;
    private Storage storage;

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
            String command = this.parser.parse(input);
            boolean isBye = this.taskManager.execute(command, input);
            if (isBye) {
                break;
            }
        }
        Ui.bye();
    }

    /**
     * Creates a new Linus chatbot.
     * Constructs the Ui, Parser, Storage, TaskList and TaskManager for
     * the Linus chatbot.
     *
     * @param filePath Path that the storage file is located.
     */
    public Linus(String filePath) {
        this.ui = new Ui();
        this.parser = new Parser();
        this.storage = new Storage(filePath);
        try {
            TaskList taskList = this.storage.loadFile();
            this.taskManager = new TaskManager(taskList, this.storage);
        } catch (FileNotFoundException e) {
            Ui.echo("OOPS!!! Unable to load tasklist file.");
            this.taskManager = new TaskManager(new TaskList(), this.storage);
        }
    }
}
