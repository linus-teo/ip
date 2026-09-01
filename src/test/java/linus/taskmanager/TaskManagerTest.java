package linus.taskmanager;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import linus.storage.Storage;
import linus.tasklist.TaskList;

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
