package midproject1;

import jakarta.persistence.*;  // JPA annotations
import java.time.LocalDate;

@Entity
@Table(name = "tasks")
public class Task {
    private static int idCounter = 0;  // Auto-increment counter (your manual logic)

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Primary key with auto-increment in DB
    private int id;   // Unique task ID
    
    @Column(name = "task_name", nullable = false, length = 100)  // Maps to task_name column in DB
    private String name;
    
    @Column(name = "due_date", nullable = false)  // Maps to due_date column in DB
    private LocalDate dueDate;
    
    @Column(name = "completed_on_time")  // Maps to completed_on_time column in DB
    private boolean completedOnTime;
    
    @Column(name = "assigned_to", nullable = false)  // Maps to assigned_to foreign key column in DB (user id)
    private int assignedTo;  // Changed to int to match DB schema
    
    @Transient  // Not stored in DB, used for runtime only
    private String assignedUsername;
    
    @Column(nullable = false, length = 50)  // Maps to status column in DB
    private String status;
    
    @Column(name = "book_title") // Not stored in DB, used only at runtime
    private String bookTitle;
    
    @Column(length = 255)  // Maps to comment column in DB, nullable
    private String comment;

    // Constructor (id is auto-assigned manually here; in JPA normally DB handles it)
    public Task(String name, String bookTitle, LocalDate dueDate, int assignedTo, String username) {
        this.name = name;
        this.bookTitle = bookTitle;
        this.dueDate = dueDate;
        this.completedOnTime = false;   // Default: not completed yet
        this.assignedTo = assignedTo;
        this.assignedUsername = String.valueOf(assignedTo);  // Example fallback for username
        this.status = "Pending";        // Default status
        this.comment = "";              // Default empty comment
    }

    // Default constructor needed for JPA
    public Task() {}

    // Getter for id
    public int getId() {
        return id;
    }

    // Setter for id (usually not needed because ID is auto-generated)
    public void setId(int id) {
        this.id = id;
    }

    // Returns the name of the task
    public String getName() {
        return name;
    }

    // Sets the name of the task
    public void setName(String name) {
        this.name = name;
    }

    // Returns the task's due date
    public LocalDate getDueDate() {
        return dueDate;
    }

    // Sets the task's due date
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    // Returns true if the task was completed on time
    public boolean isCompletedOnTime() {
        return completedOnTime;
    }

    // Sets whether the task was completed on time and updates status accordingly
    public void setCompletedOnTime(boolean completedOnTime) {
        this.completedOnTime = completedOnTime;
        updateStatus(); // Update status after completion change
    }

    // Returns the role (Author or Editor) the task is assigned to (stored as user id in DB)
    public int getAssignedTo() {
        return assignedTo;
    }

    // Sets the assigned user id (Author or Editor)
    public void setAssignedTo(int assignedTo) {
        this.assignedTo = assignedTo;
    }

    // Returns the username of the person assigned to the task (runtime only)
    public String getAssignedUsername() {
        return assignedUsername;
    }

    // Sets the username of the assigned user (runtime only)
    public void setAssignedUsername(String assignedUsername) {
        this.assignedUsername = assignedUsername;
    }

    // Returns the current status of the task
    public String getStatus() {
        return status;
    }

    // Sets the status and updates it accordingly
    public void setStatus(String newStatus) {
        this.status = newStatus;
        updateStatus();
    }

    // Returns the book title related to the task (runtime only)
    public String getBookTitle() {
        return bookTitle;
    }

    // Sets the book title (runtime only)
    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    // Returns the comment or review on the task
    public String getComment() {
        return comment;
    }

    // Sets a comment or review for the task
    public void setComment(String comment) {
        this.comment = comment;
    }

    // Updates the task status based on deadline and completion state
    private void updateStatus() {
        if (completedOnTime) {
            status = "Completed";
        } else if (LocalDate.now().isAfter(dueDate)) {
            status = "Late";
        } else {
            status = "Pending";
        }
    }

    // Returns an array representing a row for JTable display (Admin view)
    public Object[] toTableRow() {
        return new Object[]{name, assignedTo, bookTitle, dueDate.toString(), status};
    }

    // Sets a new deadline using a LocalDate object
    public void setDeadline(LocalDate newDeadline) {
        this.dueDate = newDeadline;
        updateStatus();
    }

    // Sets a new deadline using a String in ISO format yyyy-MM-dd
    public void setDeadline(String newDeadline) {
        this.dueDate = LocalDate.parse(newDeadline);
        updateStatus();
    }

    // Sets a new task name
    public void setTaskName(String newTaskName) {
        this.name = newTaskName;
    }

    // Returns the task name
    public String getTaskName() {
        return this.name;
    }

    // Returns the task deadline
    public LocalDate getDeadline() {
        return this.dueDate;
    }

    // Returns an array for JTable in editor view
    public Object[] toEditorTableRow() {
        return new Object[]{name, bookTitle, dueDate.toString(), status};
    }

    // Returns the username of the assigned author (same as getAssignedUsername)
    public String getAssignedAuthor() {
        return this.assignedUsername;
    }

   // Task içinde sadece ID ile set et

    Object getCompletedOnTime() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
