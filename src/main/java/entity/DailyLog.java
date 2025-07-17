package entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a simplified daily log for the team user story.
 * Contains only timeLog (chronological task record) and DailyTaskSummary.
 */
public class DailyLog {
    private final String id;
    private final LocalDate date;
    private final List<Object> timeLog; // Chronological log of all tasks (both completed and uncompleted)
    private final DailyTaskSummary dailyTaskSummary;

    /**
     * Constructs a new DailyLog for the specified date.
     *
     * @param date The date for this daily log
     */
    public DailyLog(LocalDate date) {
        this.id = UUID.randomUUID().toString();
        this.date = date;
        this.timeLog = new ArrayList<>();
        this.dailyTaskSummary = new DailyTaskSummary(date);
    }

    /**
     * Adds a task to the daily log.
     * The task is added to timeLog and to the DailyTaskSummary.
     *
     * @param task The task to add
     */
    public void addTask(Task task) {
        // Add to timeLog to maintain chronological record
        timeLog.add(task);
        // Add to summary for statistics
        dailyTaskSummary.addScheduledTask(task);
    }

    /**
     * Marks a task as completed.
     * Updates the task's completion status and the daily summary.
     *
     * @param task The task to mark as completed
     * @param completionTime The time the task was completed
     */
    public void markTaskCompleted(Task task, LocalDateTime completionTime) {
        // Update the task itself
        task.completeTask(completionTime);
        // Update the summary statistics
        dailyTaskSummary.markTaskCompleted(task);
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
     * Returns the chronological log of all tasks for this day.
     *
     * @return List of all tasks in chronological order
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
}