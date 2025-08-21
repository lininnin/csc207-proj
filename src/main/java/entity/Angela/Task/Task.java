package entity.Angela.Task;

import entity.BeginAndDueDates.BeginAndDueDates;
import entity.BeginAndDueDates.BeginAndDueDatesInterf;
import entity.info.Info;
import entity.info.InfoInterf;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a task instance in Today's list.
 * Created from an Available task template when added to Today.
 *
 * Business Rules:
 * - Links back to template via templateTaskId
 * - Priority is optional even for Today's tasks
 * - Begin date is auto-set to today when created
 * - Due date must be >= today if set
 * - Tracks completion status with timestamp
 * - Original position is session-only (transient)
 */
public class Task implements TaskInterf {
    private final String id;
    private final String templateTaskId;
    private Info info; // Made non-final for compatibility
    private Priority priority; // Optional, can be null
    private final BeginAndDueDates dates;
    private boolean isCompleted;
    private LocalDateTime completedDateTime;
    private boolean isOneTime; // Track here for Today's instance
    private transient Integer originalPosition; // Session-only, not persisted

    public void editStatus(boolean b) {
        if (isCompleted) {
            markComplete();
        } else {
            unmarkComplete();
        }
    }

    /**
     * Priority levels for tasks.
     */
    public enum Priority {
        LOW, MEDIUM, HIGH
    }

    /**
     * Creates a new Today's task from a template.
     *
     * @param templateTaskId ID of the source TaskAvailable
     * @param info Task information (copied from template)
     * @param dates Begin and due dates (begin should be today)
     * @param isOneTime Whether this is a one-time task (copied from template)
     * @throws IllegalArgumentException if required parameters are null
     */
    public Task(String templateTaskId, Info info, BeginAndDueDates dates, boolean isOneTime) {
        if (templateTaskId == null || templateTaskId.trim().isEmpty()) {
            throw new IllegalArgumentException("Template task ID cannot be null or empty");
        }
        if (info == null) {
            throw new IllegalArgumentException("Task info cannot be null");
        }
        if (dates == null) {
            throw new IllegalArgumentException("Dates cannot be null");
        }

        this.id = UUID.randomUUID().toString();
        this.templateTaskId = templateTaskId;
        this.info = info;
        this.dates = dates;
        this.priority = null; // Optional, set separately if needed
        this.isCompleted = false;
        this.completedDateTime = null;
        this.isOneTime = isOneTime;
        this.originalPosition = null;
    }

    /**
     * Creates a Today's task with all fields (for loading from storage).
     */
    public Task(String id, String templateTaskId, Info info, Priority priority,
                BeginAndDueDates dates, boolean isCompleted, LocalDateTime completedDateTime,
                boolean isOneTime) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Task ID cannot be null or empty");
        }
        if (templateTaskId == null || templateTaskId.trim().isEmpty()) {
            throw new IllegalArgumentException("Template task ID cannot be null or empty");
        }
        if (info == null) {
            throw new IllegalArgumentException("Task info cannot be null");
        }
        if (dates == null) {
            throw new IllegalArgumentException("Dates cannot be null");
        }

        this.id = id;
        this.templateTaskId = templateTaskId;
        this.info = info;
        this.priority = priority; // Can be null
        this.dates = dates;
        this.isCompleted = isCompleted;
        this.completedDateTime = completedDateTime;
        this.isOneTime = isOneTime;
        this.originalPosition = null;

        // Validate state consistency
        validateStateConsistency();
    }

    /**
     * Validates that the task's state is internally consistent.
     */
    private void validateStateConsistency() {
        if (isCompleted && completedDateTime == null) {
            throw new IllegalStateException("Completed task must have completion timestamp");
        }
        if (!isCompleted && completedDateTime != null) {
            throw new IllegalStateException("Incomplete task cannot have completion timestamp");
        }
    }

    /**
     * Gets the unique task instance ID.
     *
     * @return The task ID
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the template task ID this was created from.
     *
     * @return The template task ID
     */
    public String getTemplateTaskId() {
        return templateTaskId;
    }

    /**
     * Gets the task's information.
     *
     * @return The task info
     */
    public Info getInfo() {
        return info;
    }

    /**
     * Gets the task's priority.
     *
     * @return The priority, or null if not set
     */
    public Priority getPriority() {
        return priority;
    }

    /**
     * Sets the task's priority.
     * Priority is optional for Today's tasks.
     *
     * @param priority The priority level, or null to clear
     */
    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    /**
     * Gets the task's date range.
     *
     * @return The begin and due dates
     */
    public BeginAndDueDates getDates() {
        return dates;
    }
    
    /**
     * Gets the task's date range (interface method).
     * 
     * @return The BeginAndDueDatesInterf object
     */
    @Override
    public BeginAndDueDatesInterf getBeginAndDueDates() {
        return dates;
    }

    /**
     * Checks if the task is completed.
     *
     * @return true if completed
     */
    public boolean isCompleted() {
        return isCompleted;
    }
    
    /**
     * Gets the task's completion status (interface method).
     * 
     * @return true if completed, false otherwise
     */
    @Override
    public boolean getStatus() {
        return isCompleted;
    }

    /**
     * Gets the completion timestamp.
     *
     * @return The completion time, or null if not completed
     */
    public LocalDateTime getCompletedDateTime() {
        return completedDateTime;
    }

    /**
     * Marks the task as completed with the current timestamp.
     */
    public void markComplete() {
        markComplete(LocalDateTime.now());
    }

    /**
     * Marks the task as completed with a specific timestamp.
     *
     * @param completionTime The completion timestamp
     * @throws IllegalArgumentException if completionTime is null
     */
    public void markComplete(LocalDateTime completionTime) {
        if (completionTime == null) {
            throw new IllegalArgumentException("Completion time cannot be null");
        }
        this.isCompleted = true;
        this.completedDateTime = completionTime;
    }

    /**
     * Unmarks the task as complete.
     * Clears the completion timestamp.
     */
    public void unmarkComplete() {
        this.isCompleted = false;
        this.completedDateTime = null;
    }

    /**
     * Gets the original position for order restoration.
     * This is session-only and not persisted.
     *
     * @return The original position, or null if not set
     */
    public Integer getOriginalPosition() {
        return originalPosition;
    }

    /**
     * Sets the original position for order restoration.
     * This is session-only and not persisted.
     *
     * @param originalPosition The position in the list
     */
    public void setOriginalPosition(Integer originalPosition) {
        this.originalPosition = originalPosition;
    }

    /**
     * Checks if the task is overdue.
     * A task is overdue if it has a due date that's passed and is not completed.
     *
     * @return true if overdue
     */
    public boolean isOverdue() {
        if (isCompleted) {
            return false;
        }

        LocalDate dueDate = dates.getDueDate();
        if (dueDate == null) {
            return false;
        }

        return dueDate.isBefore(LocalDate.now());
    }
    
    /**
     * Checks if the task is overdue (interface method).
     * 
     * @return true if overdue, false otherwise
     */
    @Override
    public boolean isOverDue() {
        return isOverdue();
    }

    /**
     * Checks if this is a one-time task.
     *
     * @return true if one-time task
     */
    public boolean isOneTime() {
        return isOneTime;
    }

    /**
     * Sets whether this is a one-time task.
     *
     * @param oneTime true for one-time task
     */
    public void setOneTime(boolean oneTime) {
        this.isOneTime = oneTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Task{");
        sb.append("id='").append(id).append("'");
        sb.append(", templateTaskId='").append(templateTaskId).append("'");
        sb.append(", name='").append(info.getName()).append("'");
        sb.append(", category='").append(info.getCategory()).append("'");
        if (priority != null) {
            sb.append(", priority=").append(priority);
        }
        sb.append(", completed=").append(isCompleted);
        if (dates.getDueDate() != null) {
            sb.append(", dueDate=").append(dates.getDueDate());
        }
        sb.append(", oneTime=").append(isOneTime);
        sb.append(", overdue=").append(isOverdue());
        sb.append("}");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;
        return Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}