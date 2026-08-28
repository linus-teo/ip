package linus.taskmanager;

import linus.storage.Storage;
import linus.tasklist.TaskList;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class TaskManagerTest {
    @Test
    public void executeBye() {
        TaskList taskList = new TaskList();
        Storage storage = new Storage("data/tasklist.txt");
        TaskManager taskManager = new TaskManager(taskList, storage);
        assertTrue(taskManager.execute("bye", "bye"));
    }

    @Test
    public void executeList() {
        TaskList taskList = new TaskList();
        Storage storage = new Storage("data/tasklist.txt");
        TaskManager taskManager = new TaskManager(taskList, storage);
        assertFalse(taskManager.execute("list", "list"));
    }
}
