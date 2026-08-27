import java.io.FileNotFoundException;

public class Linus {
    private Ui ui;
    private Parser parser;
    private TaskManager taskManager;
    private Storage storage;

    public static void main(String[] args) {
        Linus chatbot = new Linus("data/tasklist.txt");
        chatbot.run();
    }

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
