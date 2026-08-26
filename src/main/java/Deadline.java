public class Deadline extends Task {
    private String deadline;

    public Deadline(boolean isDone, String description, String deadline) {
        super(isDone, description);
        this.deadline = deadline;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + this.deadline + ")";
    }

    @Override
    public String toFileFormat() {
        if (this.isDone()) {
            return "D | X | " + this.getDescription() + " | " + this.deadline + "\n";
        }
        return "D | | " + this.getDescription() + " | " + this.deadline + "\n";
    }
}
