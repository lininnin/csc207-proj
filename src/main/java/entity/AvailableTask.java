package entity;

import java.time.LocalDate;

/**
 * Represents a task template in the Available Tasks repository.
 * Contains only basic information - no active properties like priority or completion status.
 * Follows Single Responsibility Principle - only responsible for task template information.
 */
public class AvailableTask implements OneTimeTrackable {
    private final Info info;
    private final boolean oneTime;

    /**
     * Constructs a new AvailableTask.
     *
     * @param info Basic task information
     * @param oneTime Whether this task should be removed from Available after use
     * @throws IllegalArgumentException if info is null
     */
    public AvailableTask(Info info, boolean oneTime) {
        if (info == null) {
            throw new IllegalArgumentException("Info cannot be null");
        }
        this.info = info;
        this.oneTime = oneTime;
    }

    /**
     * Factory method to create a regular (reusable) task.
     *
     * @param id Unique identifier
     * @param name Task name (max 20 chars)
     * @param description Optional description (max 100 chars)
     * @param category Optional category
     * @return New AvailableTask instance
     */
    public static AvailableTask createRegular(String id, String name, String description, String category) {
        Info info = new Info(id, name, description, category, LocalDate.now());
        return new AvailableTask(info, false);
    }

    /**
     * Factory method to create a one-time task.
     *
     * @param id Unique identifier
     * @param name Task name (max 20 chars)
     * @param description Optional description (max 100 chars)
     * @param category Optional category
     * @return New AvailableTask instance marked as one-time
     */
    public static AvailableTask createOneTime(String id, String name, String description, String category) {
        Info info = new Info(id, name, description, category, LocalDate.now());
        return new AvailableTask(info, true);
    }

    public Info getInfo() {
        return info;
    }

    @Override
    public boolean isOneTime() {
        return oneTime;
    }

    /**
     * Creates a TodaysTask instance from this template.
     *
     * @param priority Task priority (optional, defaults to MEDIUM)
     * @param dueDate Optional due date
     * @return New TodaysTask instance
     */
    public TodaysTask addToToday(TodaysTask.Priority priority, LocalDate dueDate) {
        BeginAndDueDates dates = BeginAndDueDates.startingToday(dueDate);
        return new TodaysTask(this, priority, dates);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AvailableTask that = (AvailableTask) o;
        return info.equals(that.info);
    }

    @Override
    public int hashCode() {
        return info.hashCode();
    }

    @Override
    public String toString() {
        return String.format("AvailableTask[%s, oneTime=%s]", info.getName(), oneTime);
    }
}