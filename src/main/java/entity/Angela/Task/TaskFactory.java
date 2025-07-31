package entity.Angela.Task;

import entity.BeginAndDueDates.BeginAndDueDates;
import entity.info.Info;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Factory class for creating Task instances (Today's tasks).
 * Implements the Factory pattern following Clean Architecture principles.
 */
public class TaskFactory {

    /**
     * Creates a new Today's task from a template.
     * Begin date is set to today automatically.
     *
     * @param template The source TaskAvailable
     * @return A new Task instance for Today
     * @throws IllegalArgumentException if template is null
     */
    public Task createFromTemplate(TaskAvailable template) {
        if (template == null) {
            throw new IllegalArgumentException("Template cannot be null");
        }

        // Create dates with today as begin date
        BeginAndDueDates dates = new BeginAndDueDates(LocalDate.now(), null);

        return new Task(template.getId(), template.getInfo(), dates, template.isOneTime());
    }

    /**
     * Creates a new Today's task from a template with specific dates.
     *
     * @param template The source TaskAvailable
     * @param dueDate Optional due date (must be >= today if set)
     * @return A new Task instance for Today
     * @throws IllegalArgumentException if parameters are invalid
     */
    public Task createFromTemplate(TaskAvailable template, LocalDate dueDate) {
        if (template == null) {
            throw new IllegalArgumentException("Template cannot be null");
        }

        // Validate due date if provided
        if (dueDate != null && dueDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Due date cannot be in the past");
        }

        // Create dates with today as begin date
        BeginAndDueDates dates = new BeginAndDueDates(LocalDate.now(), dueDate);

        return new Task(template.getId(), template.getInfo(), dates, template.isOneTime());
    }

    /**
     * Creates a new Today's task with all basic fields.
     *
     * @param templateTaskId ID of the source template
     * @param info Task information
     * @param dates Begin and due dates
     * @param isOneTime Whether this is a one-time task
     * @return A new Task instance
     * @throws IllegalArgumentException if parameters are invalid
     */
    public Task create(String templateTaskId, Info info, BeginAndDueDates dates, boolean isOneTime) {
        return new Task(templateTaskId, info, dates, isOneTime);
    }

    /**
     * Creates a Today's task with all fields (for loading from storage).
     *
     * @param id The task instance ID
     * @param templateTaskId ID of the source template
     * @param info Task information
     * @param priority Optional priority
     * @param dates Begin and due dates
     * @param isCompleted Completion status
     * @param completedDateTime Optional completion timestamp
     * @param isOneTime Whether this is a one-time task
     * @return A new Task instance
     * @throws IllegalArgumentException if parameters are invalid
     */
    public Task create(String id, String templateTaskId, Info info, Task.Priority priority,
                       BeginAndDueDates dates, boolean isCompleted, LocalDateTime completedDateTime,
                       boolean isOneTime) {
        return new Task(id, templateTaskId, info, priority, dates, isCompleted, completedDateTime, isOneTime);
    }
}