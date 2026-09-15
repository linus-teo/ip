package linus.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import linus.invalidtaskexception.InvalidTaskException;
import linus.task.Deadline;
import linus.task.Event;
import linus.task.Task;
import linus.task.ToDo;

/**
 * Represents the file stored on the local hard drive which corresponds
 * to the TaskList.
 */
public class Storage {
    private static final String CREATE_STORAGE_ERROR =
            "Here's a tech tip! I'm unable to create the storage for the tasklist file :-(";
    private static final String LOAD_FILE_ERROR = "Here's a tech tip! I'm unable to open and load the tasklist file.";
    private static final String LOAD_TASK_ERROR = "Here's a tech tip! I'm unable to load the invalid task from storage";
    private static final String LOAD_TODO_TASK_ERROR =
            "Here's a tech tip! I'm unable to load the invalid ToDo task from storage";
    private static final String LOAD_DEADLINE_TASK_ERROR =
            "Here's a tech tip! I'm unable to load the invalid deadline task from storage";
    private static final String LOAD_EVENT_TASK_ERROR =
            "Here's a tech tip! I'm unable to load the invalid Event task from storage";
    private static final String SAVE_FILE_ERROR = "Here's a tech tip! I'm unable to open and save the tasklist file.";

    private final File file;

    /**
     * Creates the file to store the TaskList. If the file already exists,
     * use the existing file.
     *
     * @param path The filepath of the file to be used as storage.
     * @throws IOException When the tasklist storage file cannot be created.
     */
    public Storage(String path) throws IOException {
        assert path != null : "No filepath provided to create storage.";
        this.file = new File(path);
        if (this.file.exists()) {
            return;
        }
        try {
            this.createStorage(this.file.toPath());
        } catch (IOException e) {
            throw new IOException(CREATE_STORAGE_ERROR, e);
        }
    }

    private void createStorage(Path path) throws IOException {
        if (path.getParent() != null) {
            Files.createDirectories(path.getParent());
        }
        Files.createFile(path);
    }


    /**
     * Loads the tasks in the storage file into the TaskList.
     *
     * @return Tasklist of tasks corresponding to the local storage tasklist file.
     */
    public List<Task> loadFile() throws FileNotFoundException, InvalidTaskException {
        try (Scanner scanner = new Scanner(this.file)) {
            return createTasklist(scanner);
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException(LOAD_FILE_ERROR);
        }
    }

    private List<Task> createTasklist(Scanner scanner) throws InvalidTaskException {
        List<Task> taskList = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String text = scanner.nextLine();
            Task task = this.loadTask(text);
            taskList.add(task);
        }
        return taskList;
    }

    private Task loadTask(String text) throws InvalidTaskException {
        String[] parts = text.split("\\s*\\|\\s*");
        if (parts.length < 3) {
            throw new InvalidTaskException(LOAD_TASK_ERROR);
        }
        String taskType = parts[0];
        String taskStatus = parts[1];
        String description = parts[2];
        boolean isDone = parseTaskStatus(taskStatus);
        switch (taskType) {
            case "T":
                return createToDoTask(parts, isDone, description);
            case "D":
                return createDeadlineTask(parts, isDone, description);
            case "E":
                return createEventTask(parts, isDone, description);
            default:
                throw new InvalidTaskException(LOAD_TASK_ERROR);
        }
    }

    private boolean parseTaskStatus(String status) throws InvalidTaskException {
        if (status.equals("X")) {
            return true;
        } else if (status.isEmpty()) {
            return false;
        } else {
            throw new InvalidTaskException(LOAD_TASK_ERROR);
        }
    }

    private ToDo createToDoTask(String[] parts, boolean isDone, String description) throws InvalidTaskException {
        if (parts.length != 3) {
            throw new InvalidTaskException(LOAD_TODO_TASK_ERROR);
        }
        return new ToDo(isDone, description);
    }

    private Deadline createDeadlineTask(String[] parts, boolean isDone, String description)
            throws InvalidTaskException {
        if (parts.length != 4) {
            throw new InvalidTaskException(LOAD_DEADLINE_TASK_ERROR);
        }
        try {
            LocalDate deadline = LocalDate.parse(parts[3]);
            return new Deadline(isDone, description, deadline);
        } catch (DateTimeParseException e) {
            throw new InvalidTaskException(LOAD_DEADLINE_TASK_ERROR);
        }
    }

    private Event createEventTask(String[] parts, boolean isDone, String description) throws InvalidTaskException {
        if (parts.length != 5) {
            throw new InvalidTaskException(LOAD_EVENT_TASK_ERROR);
        }
        String startText = parts[3];
        String endText = parts[4];
        try {
            LocalDate start = LocalDate.parse(startText);
            LocalDate end = LocalDate.parse(endText);
            return new Event(isDone, description, start, end);
        } catch (DateTimeParseException e) {
            throw new InvalidTaskException(LOAD_EVENT_TASK_ERROR);
        }
    }

    /**
     * Saves the tasks from the TaskList into the local storage tasklist.
     *
     * @param taskList The collection of Task objects.
     */
    public void saveFile(List<Task> taskList) throws IOException {
        try (FileWriter fileWriter = new FileWriter(this.file)) {
            writeTasks(fileWriter, taskList);
        } catch (IOException e) {
            throw new IOException(SAVE_FILE_ERROR);
        }
    }

    private void writeTasks(FileWriter fileWriter, List<Task> taskList) throws IOException {
        for (Task task : taskList) {
            fileWriter.append(task.toFileFormat());
        }
    }
}
