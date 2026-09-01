package linus.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Represents a type of Task with a deadline date.
 */
public class Deadline extends Task {
    /** The date the task is to be completed by. */
    private LocalDate deadline;

    /** Creates a new Deadline task.
     *
     * @param isDone Whether the task has been marked as completed.
     * @param description Description of the task.
     * @param deadline Date that the task is due by.
     */
    public Deadline(boolean isDone, String description, LocalDate deadline) {
        super(isDone, description);
        this.deadline = deadline;
    }

    /**
     * Returns a String representation of each Deadline task.
     * All deadline tasks are represented starting with [D].
     * Deadline is formatted as d MMM yyyy.
     *
     * @return String representation of Deadline task.
     */
    @Override
    public String toString() {
        String formattedDeadline = this.deadline.format(DateTimeFormatter.ofPattern("d MMM yyyy"));
        return "[D]" + super.toString() + " (by: " + formattedDeadline + ")";
    }

    /**
     * Returns a String representation of each Deadline task that
     * is formatted for ease of storing into the storage file.
     *
     * @return Formatted String representation of Deadline task.
     */
    @Override
    public String toFileFormat() {
        if (this.isDone()) {
            return "D | X | " + this.getDescription() + " | " + this.deadline + "\n";
        }
        return "D | | " + this.getDescription() + " | " + this.deadline + "\n";
    }
}
