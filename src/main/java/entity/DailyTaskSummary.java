package entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Represents a daily summary of tasks with statistics for a specific date.
 * Tracks scheduled tasks, completed tasks, completion rate, and category breakdown.
 */
public class DailyTaskSummary {
    private final String id;
    private final LocalDate date;
    private final List<Task> scheduledTasks;
    private final List<Task> completedTasks;
    private double completionRate;
    private final Map<String, Integer> categoryBreakdown;

    /**
     * Constructs a new DailyTaskSummary for the specified date.
     *
     * @param date The date this summary represents
     */
    public DailyTaskSummary(LocalDate date) {
        this.id = UUID.randomUUID().toString();
        this.date = date;
        this.scheduledTasks = new ArrayList<>();
        this.completedTasks = new ArrayList<>();
        this.completionRate = 0.0;
        this.categoryBreakdown = new HashMap<>();
    }
    /**
     * Adds a task to the scheduled tasks list.
     *
     * @param task The task to add to scheduled tasks
     */
    public void addScheduledTask(Task task) {
        if (task != null && !scheduledTasks.contains(task)) {
            scheduledTasks.add(task);
            updateCompletionRate();
        }
    }

    /**
     * Marks a task as completed and updates statistics.
     *
     * @param task The task to mark as completed
     */
    public void markTaskCompleted(Task task) {
        if (task != null && scheduledTasks.contains(task) && !completedTasks.contains(task)) {
            completedTasks.add(task);
            updateCategoryBreakdown(task);
            updateCompletionRate();
        }
    }

    /**
     * Updates the category breakdown when a task is completed.
     *
     * @param task The completed task
     */
    private void updateCategoryBreakdown(Task task) {
        String category = task.getInfo().getCategory();
        if (category != null) {
            categoryBreakdown.merge(category, 1, Integer::sum);
        }
    }

    /**
     * Recalculates the completion rate based on scheduled and completed tasks.
     */
    private void updateCompletionRate() {
        if (scheduledTasks.isEmpty()) {
            this.completionRate = 0.0;
        } else {
            this.completionRate = (double) completedTasks.size() / scheduledTasks.size();
        }
    }

    // Getters
    // TODO: These getters will be used in Angela's analytics story for data visualization
    public String getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public List<Task> getScheduledTasks() {
        return new ArrayList<>(scheduledTasks);
    }

    public List<Task> getCompletedTasks() {
        return new ArrayList<>(completedTasks);
    }

    public double getCompletionRate() {
        return completionRate;
    }

    public Map<String, Integer> getCategoryBreakdown() {
        return new HashMap<>(categoryBreakdown);
    }
}

