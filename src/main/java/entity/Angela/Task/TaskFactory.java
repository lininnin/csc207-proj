package entity.Angela.Task;

import entity.BeginAndDueDates.BeginAndDueDates;
import entity.BeginAndDueDates.BeginAndDueDatesInterf;
import entity.BeginAndDueDates.ImmutableBeginAndDueDates;
import entity.info.Info;
import entity.info.InfoInterf;
import entity.info.ImmutableInfo;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Factory class for creating Task instances (Today's tasks).
 * Implements the Factory pattern following Clean Architecture principles.
 * 
 * Provides methods to create both mutable and immutable Task entities.
 * Uses interface types for dependency inversion compliance.
 */
public class TaskFactory {

    /**
     * Creates a new Today's task from a template.
     * Begin date is set to today automatically.
     *
     * @param template The source TaskAvailableInterf
     * @return A new Task instance for Today
     * @throws IllegalArgumentException if template is null
     */
    public TaskInterf createFromTemplate(TaskAvailableInterf template) {
        if (template == null) {
            throw new IllegalArgumentException("Template cannot be null");
        }

        // Create dates with today as begin date
        BeginAndDueDates dates = new BeginAndDueDates(LocalDate.now(), null);

        return new Task(template.getId(), convertToConcreteInfo(template.getInfo()), dates, template.isOneTime());
    }

    /**
     * Creates a new Today's task from a template with specific dates.
     *
     * @param template The source TaskAvailableInterf
     * @param dueDate Optional due date (must be >= today if set)
     * @return A new Task instance for Today
     * @throws IllegalArgumentException if parameters are invalid
     */
    public TaskInterf createFromTemplate(TaskAvailableInterf template, LocalDate dueDate) {
        if (template == null) {
            throw new IllegalArgumentException("Template cannot be null");
        }

        // Validate due date if provided
        if (dueDate != null && dueDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Due date cannot be in the past");
        }

        // Create dates with today as begin date
        BeginAndDueDates dates = new BeginAndDueDates(LocalDate.now(), dueDate);

        return new Task(template.getId(), convertToConcreteInfo(template.getInfo()), dates, template.isOneTime());
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
    public TaskInterf create(String templateTaskId, InfoInterf info, BeginAndDueDatesInterf dates, boolean isOneTime) {
        // Convert interfaces to concrete types for Task constructor
        Info concreteInfo = convertToConcreteInfo(info);
        BeginAndDueDates concreteDates = convertToConcreteBeginAndDueDates(dates);
        
        return new Task(templateTaskId, concreteInfo, concreteDates, isOneTime);
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
    public TaskInterf create(String id, String templateTaskId, InfoInterf info, Task.Priority priority,
                       BeginAndDueDatesInterf dates, boolean isCompleted, LocalDateTime completedDateTime,
                       boolean isOneTime) {
        // Convert interfaces to concrete types for Task constructor
        Info concreteInfo = convertToConcreteInfo(info);
        BeginAndDueDates concreteDates = convertToConcreteBeginAndDueDates(dates);
        
        return new Task(id, templateTaskId, concreteInfo, priority, concreteDates, isCompleted, completedDateTime, isOneTime);
    }
    
    // Factory methods for immutable entity creation
    
    /**
     * Creates a new Today's task from a template using immutable entities.
     * Begin date is set to today automatically.
     *
     * @param template The source TaskAvailableInterf
     * @return A new Task instance with immutable components
     * @throws IllegalArgumentException if template is null
     */
    public TaskInterf createImmutableFromTemplate(TaskAvailableInterf template) {
        if (template == null) {
            throw new IllegalArgumentException("Template cannot be null");
        }

        // Create immutable dates with today as begin date
        ImmutableBeginAndDueDates dates = new ImmutableBeginAndDueDates(LocalDate.now(), null);
        ImmutableInfo immutableInfo = new ImmutableInfo((Info) template.getInfo());

        return new Task(template.getId(), immutableInfo.toMutableInfo(), dates.toMutableBeginAndDueDates(), template.isOneTime());
    }
    
    /**
     * Creates a new Today's task using immutable entities with specific fields.
     *
     * @param templateTaskId ID of the source template
     * @param info Immutable task information
     * @param dates Immutable begin and due dates
     * @param isOneTime Whether this is a one-time task
     * @return A new Task instance with immutable components
     * @throws IllegalArgumentException if parameters are invalid
     */
    public TaskInterf createImmutable(String templateTaskId, ImmutableInfo info, ImmutableBeginAndDueDates dates, boolean isOneTime) {
        if (info == null || dates == null) {
            throw new IllegalArgumentException("Info and dates cannot be null");
        }
        
        return new Task(templateTaskId, info.toMutableInfo(), dates.toMutableBeginAndDueDates(), isOneTime);
    }
    
    // Helper methods for type conversion
    
    /**
     * Converts InfoInterf to concrete Info type.
     */
    private Info convertToConcreteInfo(InfoInterf info) {
        if (info instanceof Info) {
            return (Info) info;
        } else if (info instanceof ImmutableInfo) {
            return ((ImmutableInfo) info).toMutableInfo();
        } else {
            throw new IllegalArgumentException("Unsupported InfoInterf implementation: " + info.getClass());
        }
    }
    
    /**
     * Converts BeginAndDueDatesInterf to concrete BeginAndDueDates type.
     */
    private BeginAndDueDates convertToConcreteBeginAndDueDates(BeginAndDueDatesInterf dates) {
        if (dates instanceof BeginAndDueDates) {
            return (BeginAndDueDates) dates;
        } else if (dates instanceof ImmutableBeginAndDueDates) {
            return ((ImmutableBeginAndDueDates) dates).toMutableBeginAndDueDates();
        } else {
            throw new IllegalArgumentException("Unsupported BeginAndDueDatesInterf implementation: " + dates.getClass());
        }
    }
}