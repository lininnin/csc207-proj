package entity.Angela;

import entity.alex.DailyEventLog.DailyEventLog;
import entity.alex.DailyWellnessLog.DailyWellnessLog;
import entity.Angela.Task.Task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a simplified daily log for the team user story.
 * Contains only timeLog (chronological task record), DailyWellnessLog, DailyEventLog, and DailyTaskSummary.
 */
public class DailyLog {
    private final String id;
    private final LocalDate date;
    private final List<Object> timeLog; // Chronological log of all completed tasks/events/wellnesslog
    private final DailyWellnessLog dailyWellnessLog;
    private final DailyEventLog dailyEventLog;
    private final DailyTaskSummary dailyTaskSummary;

    /**
     * Constructs a new DailyLog for the specified date.
     *
     * @param date The date for this daily log; must not be null
     * @throws IllegalArgumentException if date is null
     */
    public DailyLog(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null for DailyLog.");
        }

        this.id = UUID.randomUUID().toString();
        this.date = date;
        this.timeLog = new ArrayList<>();
        this.dailyTaskSummary = new DailyTaskSummary(date);
        this.dailyWellnessLog = new DailyWellnessLog(date);
        this.dailyEventLog = new DailyEventLog(date);
    }

    /**
     * Adds a task to the daily log.
     * The task is added to timeLog and to the DailyTaskSummary.
     *
     * TODO: Will be used when implementing daily planning feature
     * @param task The task to add; must not be null
     * @throws IllegalArgumentException if task is null
     */
    public void addTask(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Cannot add null task to DailyLog.");
        }
        timeLog.add(task); // Add to timeLog to maintain chronological record
        dailyTaskSummary.addScheduledTask(task); // Add to summary for statistics
    }

    /**
     * Marks a task as completed.
     * Updates the task's completion status and the daily summary.
     *
     * @param task The task to mark as completed; must not be null
     * @param completionTime The time the task was completed; must not be null
     * @throws IllegalArgumentException if task or completionTime is null
     */
    public void markTaskCompleted(Task task, LocalDateTime completionTime) {
        if (task == null) {
            throw new IllegalArgumentException("Cannot mark null task as completed.");
        }
        if (completionTime == null) {
            throw new IllegalArgumentException("Completion time cannot be null.");
        }

        task.editStatus(true);        // Update the task itself
        dailyTaskSummary.markTaskCompleted(task);  // Update the summary statistics
    }

    /**
     * Returns the unique identifier for this daily log.
     *
     * @return The UUID string
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the date of this daily log.
     *
     * @return The LocalDate
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Returns the chronological log of all tasks/events/wellness logs for this day.
     *
     * TODO: Will be used in Alex's story #2 for showing task/event correlations
     * @return List of all entries in chronological order
     */
    public List<Object> getTimeLog() {
        return new ArrayList<>(timeLog);
    }

    /**
     * Returns the daily task summary with statistics.
     *
     * @return The DailyTaskSummary object
     */
    public DailyTaskSummary getDailyTaskSummary() {
        return dailyTaskSummary;
    }

    /**
     * Returns the daily wellness log.
     *
     * @return The DailyWellnessLog object
     */
    public DailyWellnessLog getDailyWellnessLog() {
        return dailyWellnessLog;
    }

    /**
     * Returns the daily event log.
     *
     * @return The DailyEventLog object
     */
    public DailyEventLog getDailyEventLog() {
        return dailyEventLog;
    }
}

