package linus.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import linus.invalidtaskexception.InvalidTaskException;
import linus.task.Deadline;
import linus.task.Event;
import linus.task.Task;
import linus.task.ToDo;
import linus.ui.Ui;

/**
 * Represents the file stored on the local hard drive which corresponds
 * to the TaskList.
 */
public class Storage {
    private final File file;

    /**
     * Creates the file to store the TaskList. If the file already exists,
     * use the existing file.
     *
     * @param path The filepath of the file to be used as storage.
     */
    public Storage(String path) {
        assert path != null : "No filepath provided to create storage.";
        this.file = new File(path);
        if (!this.file.exists()) {
            try {
                this.createStorage(file);
            } catch (IOException e) {
                Ui.display("Here's a tech tip! I'm unable to create the tasklist file.");
            }
        }
    }

    private void createStorage(File file) throws IOException {
        File parent = file.getParentFile();
        if (parent != null) {
            parent.mkdirs();
        }
        this.file.createNewFile();
    }

    /**
     * Loads the tasks in the storage file into the TaskList.
     *
     * @return Tasklist of tasks corresponding to the local storage tasklist file.
     */
    public List<Task> loadFile() {
        List<Task> taskList = new ArrayList<>();
        assert this.file != null : "File has not been initialised";
        try {
            Scanner scanner = new Scanner(this.file);
            while (scanner.hasNextLine()) {
                String text = scanner.nextLine();
                Task task = this.loadTask(text);
                taskList.add(task);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            Ui.display("Here's a tech tip! I'm unable to open and load the tasklist file.");
        } catch (InvalidTaskException e) {
            Ui.display(e.getMessage());
        }
        return taskList;
    }

    private Task loadTask(String text) throws InvalidTaskException {
        String[] parts = text.split("\\s*\\|\\s*");
        assert parts.length >= 3 : "Task does not have correct number of details";
        boolean isDone = parts[1].equals("X");
        String description = parts[2];
        switch (parts[0]) {
            case "T":
                assert parts.length == 3 : "Todo task does not have correct number of details";
                return new ToDo(isDone, description);
            case "D":
                assert parts.length == 4 : "Deadline task does not have correct number of details";
                String deadlineText = parts[3];
                LocalDate deadline = LocalDate.parse(deadlineText);
                return new Deadline(isDone, description, deadline);
            case "E":
                assert parts.length == 5 : "Event task does not have correct number of details";
                String startText = parts[3];
                String endText = parts[4];
                LocalDate start = LocalDate.parse(startText);
                LocalDate end = LocalDate.parse(endText);
                return new Event(isDone, description, start, end);
            default:
                throw new InvalidTaskException("Here's a tech tip! I'm unable to load the invalid task from storage.");
        }
    }

    /**
     * Saves the tasks from the TaskList into the local storage tasklist.
     *
     * @param taskList The collection of Task objects.
     */
    public void saveFile(List<Task> taskList) {
        try {
            FileWriter fileWriter = new FileWriter(this.file);
            for (Task task : taskList) {
                fileWriter.append(task.toFileFormat());
            }
            fileWriter.close();
        } catch (IOException e) {
            Ui.display("Here's a tech tip! I'm unable to open and save tasklist file.");
        }
    }
}
