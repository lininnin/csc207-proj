package view;

import entity.Task;
import java.util.ArrayList;
import java.util.List;

/**
 * View Model for task-related data.
 * Holds the state that the view needs to display.
 */
public class TaskViewModel {
    private List<Task> todaysTasks = new ArrayList<>();
    private List<Task> completedTasks = new ArrayList<>();
    private List<Task> overdueTasks = new ArrayList<>();
    private String message = "";
    private String lastCreatedTaskId = "";
    private final List<TaskViewModelUpdateListener> listeners = new ArrayList<>();

    // Methods to update state
    public void setTodaysTasks(List<Task> tasks) {
        this.todaysTasks = new ArrayList<>(tasks);
        notifyListeners();
    }

    public void setCompletedTasks(List<Task> tasks) {
        this.completedTasks = new ArrayList<>(tasks);
        notifyListeners();
    }

    public void setOverdueTasks(List<Task> tasks) {
        this.overdueTasks = new ArrayList<>(tasks);
        notifyListeners();
    }

    public void setMessage(String message) {
        this.message = message;
        notifyListeners();
    }

    public void setLastCreatedTaskId(String taskId) {
        this.lastCreatedTaskId = taskId;
    }

    // Getters
    public List<Task> getTodaysTasks() {
        return new ArrayList<>(todaysTasks);
    }

    public List<Task> getCompletedTasks() {
        return new ArrayList<>(completedTasks);
    }

    public List<Task> getOverdueTasks() {
        return new ArrayList<>(overdueTasks);
    }

    public String getMessage() {
        return message;
    }

    // Observer pattern for view updates
    public void addUpdateListener(TaskViewModelUpdateListener listener) {
        listeners.add(listener);
    }

    private void notifyListeners() {
        for (TaskViewModelUpdateListener listener : listeners) {
            listener.onViewModelUpdated();
        }
    }

    public void refreshTaskLists() {
        // This will be called by presenters to trigger a refresh
        notifyListeners();
    }
}

