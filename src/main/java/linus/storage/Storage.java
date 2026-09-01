package linus.storage;

import linus.task.Deadline;
import linus.task.Event;
import linus.task.Task;
import linus.task.ToDo;
import linus.tasklist.TaskList;
import linus.ui.Ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Scanner;

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
    public Storage (String path) {
        Path filepath = Paths.get(path);
        this.file = new File(path);
        if (!this.file.exists()) {
            try {
                File parent = this.file.getParentFile();
                if (parent != null) {
                    Files.createDirectories(parent.toPath());
                }
                Files.createFile(filepath);
            } catch (IOException e) {
                Ui.echo("OOPS!!! Unable to create tasklist file.");
            }
        }
    }

    /**
     * Load the tasks in the storage file into the TaskList.
     *
     * @return Tasklist of tasks corresponding to the local storage tasklist
     * file.
     * @throws FileNotFoundException If the local storage tasklist cannot be
     * found.
     */
    public TaskList loadFile() throws FileNotFoundException {
        Scanner scanner = new Scanner(this.file);
        TaskList taskList = new TaskList();
        while (scanner.hasNextLine()) {
            String task = scanner.nextLine();
            String[] parts = task.split("\\s*\\|\\s*");
            boolean isDone = parts[1].equals("X");
            String description = parts[2];
            switch (parts[0]) {
                case "T":
                    taskList.add(new ToDo(isDone, description));
                    break;
                case "D":
                    String deadlineText = parts[3];
                    LocalDate deadline = LocalDate.parse(deadlineText);
                    taskList.add(new Deadline(isDone, description, deadline));
                    break;
                case "E":
                    String startText = parts[3];
                    String endText = parts[4];
                    LocalDate start = LocalDate.parse(startText);
                    LocalDate end = LocalDate.parse(endText);
                    taskList.add(new Event(isDone, description, start, end));
                    break;
            }
        }
        scanner.close();
        return taskList;
    }

    /**
     * Save the tasks from the TaskList into the local storage tasklist.
     *
     * @param taskList The collection of Task objects.
     */
    public void saveFile(TaskList taskList) {
        try {
            FileWriter fileWriter = new FileWriter(this.file);
            for (int i = 0; i < taskList.size(); i++) {
                Task task = taskList.get(i);
                fileWriter.append(task.toFileFormat());
            }
            fileWriter.close();
        } catch (IOException e) {
            Ui.echo("OOPS!!! Unable to open and save tasklist file.");
        }
    }
}
