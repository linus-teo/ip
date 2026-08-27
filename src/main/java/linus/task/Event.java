package linus.task;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Event extends Task {
    private LocalDate start;
    private LocalDate end;

    public Event(boolean isDone, String description, LocalDate start, LocalDate end) {
        super(isDone, description);
        this.start = start;
        this.end = end;
    }

    @Override
    public String toString() {
        String formattedStart = this.start.format(DateTimeFormatter.ofPattern("d MMM yyyy"));
        String formattedEnd = this.end.format(DateTimeFormatter.ofPattern("d MMM yyyy"));
        return "[E]" + super.toString() + " (from: " + formattedStart + " to: " + formattedEnd + ")";
    }

    @Override
    public String toFileFormat() {
        if (this.isDone()) {
            return "E | X | " + this.getDescription() + " | " + this.start + " | " + this.end + "\n";
        }
        return "E | | " + this.getDescription() + " | " + this.start + " | " + this.end + "\n";
    }
}
