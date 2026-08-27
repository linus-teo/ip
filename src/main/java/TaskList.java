import java.util.ArrayList;
import java.util.List;

public class TaskList {
    private final List<Task> taskList;

    public TaskList() {
        this.taskList = new ArrayList<>();
    }

    public int size() {
        return this.taskList.size();
    }

    public void add(Task task) {
        this.taskList.add(task);
    }

    public void delete(Task task) {
        this.taskList.remove(task);
    }

    public Task get(int index) {
        return this.taskList.get(index);
    }
}
