package view.Task;

import entity.Angela.Task.Task;
import java.util.ArrayList;
import java.util.List;

/**
 * View model for the task view.
 * Holds the state and notifies listeners of updates.
 */
public class TaskViewModel {
    private List<Task> availableTasks;
    private List<Task> todaysTasks;
    private List<Task> completedTasks;
    private List<Task> overdueTasks;
    private double completionRate;
    private String message;

    private final List<TaskViewModelUpdateListener> updateListeners;

    public TaskViewModel() {
        this.availableTasks = new ArrayList<>();
        this.todaysTasks = new ArrayList<>();
        this.completedTasks = new ArrayList<>();
        this.overdueTasks = new ArrayList<>();
        this.completionRate = 0.0;
        this.message = "";
        this.updateListeners = new ArrayList<>();
    }

    /**
     * Adds a listener for view model updates.
     *
     * @param listener The listener to add
     */
    public void addUpdateListener(TaskViewModelUpdateListener listener) {
        if (listener != null && !updateListeners.contains(listener)) {
            updateListeners.add(listener);
        }
    }

    /**
     * Removes a listener.
     *
     * @param listener The listener to remove
     */
    public void removeUpdateListener(TaskViewModelUpdateListener listener) {
        updateListeners.remove(listener);
    }

    /**
     * Notifies all listeners of an update.
     */
    private void notifyListeners() {
        for (TaskViewModelUpdateListener listener : updateListeners) {
            listener.onViewModelUpdated();
        }
    }

    // Getters and setters with notification

    public List<Task> getAvailableTasks() {
        return new ArrayList<>(availableTasks);
    }

    public void setAvailableTasks(List<Task> availableTasks) {
        this.availableTasks = new ArrayList<>(availableTasks);
        notifyListeners();
    }

    public List<Task> getTodaysTasks() {
        return new ArrayList<>(todaysTasks);
    }

    public void setTodaysTasks(List<Task> todaysTasks) {
        this.todaysTasks = new ArrayList<>(todaysTasks);
        // Separate completed from incomplete
        updateCompletedTasks();
        notifyListeners();
    }

    public List<Task> getCompletedTasks() {
        return new ArrayList<>(completedTasks);
    }

    public List<Task> getOverdueTasks() {
        return new ArrayList<>(overdueTasks);
    }

    public void setOverdueTasks(List<Task> overdueTasks) {
        this.overdueTasks = new ArrayList<>(overdueTasks);
        notifyListeners();
    }

    public double getCompletionRate() {
        return completionRate;
    }

    public void setCompletionRate(double completionRate) {
        this.completionRate = completionRate;
        notifyListeners();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message != null ? message : "";
        notifyListeners();
    }

    /**
     * Updates the completed tasks list from today's tasks.
     */
    private void updateCompletedTasks() {
        completedTasks.clear();
        for (Task task : todaysTasks) {
            if (task.isComplete()) {
                completedTasks.add(task);
            }
        }
    }

    /**
     * Updates all task lists from repositories.
     * Called by the presenter when data changes.
     *
     * @param available Available tasks
     * @param todays Today's tasks
     * @param overdue Overdue tasks
     * @param rate Completion rate
     */
    public void updateAllTasks(List<Task> available, List<Task> todays,
                               List<Task> overdue, double rate) {
        this.availableTasks = new ArrayList<>(available);
        this.todaysTasks = new ArrayList<>(todays);
        this.overdueTasks = new ArrayList<>(overdue);
        this.completionRate = rate;
        updateCompletedTasks();
        notifyListeners();
    }

    /**
     * Clears the message.
     */
    public void clearMessage() {
        this.message = "";
        notifyListeners();
    }

    /**
     * Gets the count of incomplete tasks in today.
     *
     * @return Number of incomplete tasks
     */
    public int getIncompleteTodayCount() {
        return (int) todaysTasks.stream()
                .filter(task -> !task.isComplete())
                .count();
    }

    /**
     * Gets the count of completed tasks today.
     *
     * @return Number of completed tasks
     */
    public int getCompletedTodayCount() {
        return completedTasks.size();
    }
}