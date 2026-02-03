
package midproject1;

import java.time.LocalDate;  // Import LocalDate to work with dates

public class DeadlineChecker {
    private LocalDate deadline;  // The deadline for the task

    // Constructor: This sets the deadline
    public DeadlineChecker(LocalDate deadline) {
        this.deadline = deadline;  // Set the deadline value
    }

    // Check if the deadline has passed
    public boolean isDeadlinePassed() {
        LocalDate today = LocalDate.now();  // Get today's date
        return today.isAfter(deadline);  // If today is after the deadline, return true
    }

    // Check if the deadline has passed with a given date
    public boolean isDeadlinePassed(LocalDate checkDate) {
        return checkDate.isAfter(deadline);  // Compare the given date with the deadline
    }

    // Get the deadline
    public LocalDate getDeadline() {
        return deadline;  // Return the deadline value
    }

    // Change the deadline
    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;  // Set a new deadline
    }
}
