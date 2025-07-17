package view;

import entity.Task;
import java.util.ArrayList;
import java.util.List;

/**
 * View Model for task-related data.
 * Holds the state that the view needs to display.
 */
public class TaskViewModel {
    private List<Task> availableTasks = new ArrayList<>();
    private List<Task> todaysTasks = new ArrayList<>();
    private List<Task> completedTasks = new ArrayList<>();
    private List<Task> overdueTasks = new ArrayList<>();
    private double completionRate = 0.0;
    private String message = "";
    private final List<TaskViewModelUpdateListener> listeners = new ArrayList<>();

    // Methods to update state
    public void setAvailableTasks(List<Task> tasks) {
        this.availableTasks = new ArrayList<>(tasks);
        notifyListeners();
    }

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

    public void setCompletionRate(double rate) {
        this.completionRate = rate;
        notifyListeners();
    }

    public void setMessage(String message) {
        this.message = message;
        notifyListeners();
    }

    // Getters
    public List<Task> getAvailableTasks() {
        return new ArrayList<>(availableTasks);
    }

    public List<Task> getTodaysTasks() {
        return new ArrayList<>(todaysTasks);
    }

    public List<Task> getCompletedTasks() {
        return new ArrayList<>(completedTasks);
    }

    public List<Task> getOverdueTasks() {
        return new ArrayList<>(overdueTasks);
    }

    public double getCompletionRate() {
        return completionRate;
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
}

