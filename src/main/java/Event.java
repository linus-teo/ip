public class Event extends Task {
    private String start;
    private String end;

    public Event(boolean isDone, String description, String start, String end) {
        super(isDone, description);
        this.start = start;
        this.end = end;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + this.start + " to: " + this.end + ")";
    }

    @Override
    public String toFileFormat() {
        if (this.isDone()) {
            return "E | X | " + this.getDescription() + " | " + this.start + " | " + this.end + "\n";
        }
        return "E | | " + this.getDescription() + " | " + this.start + " | " + this.end + "\n";
    }
}
