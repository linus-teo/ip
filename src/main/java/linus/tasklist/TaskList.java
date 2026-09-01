package linus.tasklist;

import java.util.ArrayList;
import java.util.List;

import linus.task.Task;

/**
 * Represents a list of tasks entered by the user.
 */
public class TaskList {
    private final List<Task> taskList;

    /**
     * Creates a new TaskList.
     */
    public TaskList() {
        this.taskList = new ArrayList<>();
    }

    /**
     * Returns the number of tasks in the TaskList.
     *
     * @return Number of tasks in the TaskList.
     */
    public int size() {
        return this.taskList.size();
    }

    /**
     * Adds a task to the end of the TaskList.
     *
     * @param task Task to be added to the TaskList.
     */
    public void add(Task task) {
        this.taskList.add(task);
    }

    /**
     * Removes a Task from the TaskList.
     *
     * @param task Task to be removed from the TaskList.
     */
    public void delete(Task task) {
        this.taskList.remove(task);
    }

    /**
     * Returns the task at the specified position in the TaskList.
     *
     * @param index Position of Task
     * @return Task at the specified position in the TaskList
     */
    public Task get(int index) {
        return this.taskList.get(index);
    }
}
