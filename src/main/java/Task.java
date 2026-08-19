public class Task {
    private boolean isDone;
    private String description;

    public Task (boolean isDone, String description) {
        this.isDone = isDone;
        this.description = description;
    }

    public void mark() {
        this.isDone = true;
    }

    public void unmark() {
        this.isDone = false;
    }

    @Override
    public String toString() {
        if (isDone) {
            return "[X] " + this.description;
        }
        return "[ ] " + this.description;
    }
}
