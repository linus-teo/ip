package linus.task;

public class ToDo extends Task{
    public ToDo(boolean isDone, String description) {
        super(isDone, description);
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }

    @Override
    public String toFileFormat() {
        if (this.isDone()) {
            return "T | X | " + this.getDescription() + "\n";
        }
        return "T | | " + this.getDescription() + "\n";
    }
}
