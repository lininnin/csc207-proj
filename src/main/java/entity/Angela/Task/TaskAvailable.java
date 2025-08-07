package entity.Angela.Task;

import entity.info.Info;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents an available task template.
 * These are the master templates that users create and can add to Today's tasks.
 *
 * Business Rules:
 * - Task names must be unique in Available Tasks (case-insensitive)
 * - Names limited to 20 characters (validated in Info)
 * - Descriptions limited to 100 characters (validated in Info)
 * - Can have a planned due date that persists user planning
 * - No priority or completion status (those belong to Today's instances)
 */
public class TaskAvailable {
    private final String id;
    private Info info; // Made non-final for compatibility
    private String plannedDueDate; // ISO date string or null
    private boolean isOneTime; // Store here, not in Info

    /**
     * Creates a new available task template.
     *
     * @param info Basic task information (uses existing Info)
     * @throws IllegalArgumentException if info is null
     */
    public TaskAvailable(Info info) {
        if (info == null) {
            throw new IllegalArgumentException("Task info cannot be null");
        }

        // CRITICAL: Use the existing Info's ID, don't generate a new one!
        this.id = info.getId();
        this.info = info;
        this.plannedDueDate = null;
        this.isOneTime = false; // Default to regular task
    }

    /**
     * Creates an available task with a specific ID (for loading from storage).
     *
     * @param id The task ID
     * @param info Basic task information
     * @param plannedDueDate Optional planned due date (ISO format)
     * @param isOneTime Whether this is a one-time task
     * @throws IllegalArgumentException if required parameters are null
     */
    public TaskAvailable(String id, Info info, String plannedDueDate, boolean isOneTime) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Task ID cannot be null or empty");
        }
        if (info == null) {
            throw new IllegalArgumentException("Task info cannot be null");
        }

        this.id = id;
        this.info = info;
        this.plannedDueDate = plannedDueDate;
        this.isOneTime = isOneTime;
    }

    /**
     * Gets the unique task ID.
     *
     * @return The task ID
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the task's basic information.
     *
     * @return The immutable task info
     */
    public Info getInfo() {
        return info;
    }

    /**
     * Gets the planned due date.
     *
     * @return The planned due date in ISO format, or null if not set
     */
    public String getPlannedDueDate() {
        return plannedDueDate;
    }

    /**
     * Sets the planned due date.
     * This is updated when user sets a due date in Today's task.
     *
     * @param plannedDueDate The due date in ISO format, or null to clear
     */
    public void setPlannedDueDate(String plannedDueDate) {
        this.plannedDueDate = plannedDueDate;
    }

    /**
     * Checks if this task has a planned due date.
     *
     * @return true if a due date is set
     */
    public boolean hasPlannedDueDate() {
        return plannedDueDate != null && !plannedDueDate.isEmpty();
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

    /**
     * Checks if this task is a duplicate of another based on name AND category.
     * Same name with different categories is allowed.
     * Comparison is case-insensitive per business rules.
     *
     * @param other The other task to compare
     * @return true if names AND categories match (case-insensitive)
     */
    public boolean isDuplicateOf(TaskAvailable other) {
        if (other == null) {
            return false;
        }

        // Check if names match (case-insensitive)
        boolean namesMatch = this.info.getName().equalsIgnoreCase(other.info.getName());

        // Check if categories match (including null handling)
        String thisCategory = this.info.getCategory();
        String otherCategory = other.info.getCategory();

        boolean categoriesMatch;
        if (thisCategory == null && otherCategory == null) {
            categoriesMatch = true; // Both null
        } else if (thisCategory == null || otherCategory == null) {
            categoriesMatch = false; // One null, one not
        } else {
            categoriesMatch = thisCategory.equalsIgnoreCase(otherCategory);
        }

        return namesMatch && categoriesMatch;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TaskAvailable{");
        sb.append("id='").append(id).append("'");
        sb.append(", name='").append(info.getName()).append("'");
        sb.append(", category='").append(info.getCategory()).append("'");
        sb.append(", oneTime=").append(isOneTime);
        if (plannedDueDate != null) {
            sb.append(", plannedDueDate='").append(plannedDueDate).append("'");
        }
        sb.append("}");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TaskAvailable that = (TaskAvailable) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}