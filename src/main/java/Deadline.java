import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Deadline extends Task {
    private LocalDate deadline;

    public Deadline(boolean isDone, String description, LocalDate deadline) {
        super(isDone, description);
        this.deadline = deadline;
    }

    @Override
    public String toString() {
        String formattedDeadline = this.deadline.format(DateTimeFormatter.ofPattern("d MMM yyyy"));
        return "[D]" + super.toString() + " (by: " + formattedDeadline + ")";
    }

    @Override
    public String toFileFormat() {
        if (this.isDone()) {
            return "D | X | " + this.getDescription() + " | " + this.deadline + "\n";
        }
        return "D | | " + this.getDescription() + " | " + this.deadline + "\n";
    }
}
