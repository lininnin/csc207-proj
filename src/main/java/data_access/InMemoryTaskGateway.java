package data_access;

import entity.Angela.Task.Task;
import entity.info.Info;
import use_case.Angela.task.TaskGateway;
import java.time.LocalDate;
import java.util.*;

/**
 * In-memory implementation of TaskGateway for quick demos.
 * Replace with TaskRepository (JSON-based) for production.
 */
public class InMemoryTaskGateway implements TaskGateway {
    private final Map<String, Info> availableTasks = new HashMap<>();
    private final Map<String, Task> todaysTasks = new HashMap<>();

    @Override
    public String saveAvailableTask(Info info) {
        String taskId = info.getId(); // Info already has an ID
        availableTasks.put(taskId, info);
        return taskId;
    }

    @Override
    public List<Info> getAllAvailableTasks() {
        return new ArrayList<>(availableTasks.values());
    }

    @Override
    public boolean availableTaskNameExists(String name) {
        return availableTasks.values().stream()
                .anyMatch(info -> info.getName().equalsIgnoreCase(name));
    }

    @Override
    public boolean taskExistsWithNameAndCategory(String name, String category) {
        return availableTasks.values().stream()
                .anyMatch(info -> {
                    boolean nameMatches = info.getName().equalsIgnoreCase(name);
                    String taskCategory = info.getCategory() != null ? info.getCategory() : "";
                    boolean categoryMatches = taskCategory.equalsIgnoreCase(category);
                    return nameMatches && categoryMatches;
                });
    }

    @Override
    public boolean updateAvailableTask(Info info) {
        if (availableTasks.containsKey(info.getId())) {
            availableTasks.put(info.getId(), info);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteFromAvailable(String taskId) {
        return availableTasks.remove(taskId) != null;
    }

    @Override
    public Task addToToday(String taskId, Task.Priority priority, LocalDate dueDate) {
        // Implementation for demo - you'll implement this in add_task_to_today use case
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<Task> getTodaysTasks() {
        return new ArrayList<>(todaysTasks.values());
    }

    @Override
    public boolean updateTodaysTask(Task task) {
        // Implementation for demo
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public boolean removeFromToday(String taskId) {
        return todaysTasks.remove(taskId) != null;
    }

    @Override
    public boolean markTaskComplete(String taskId) {
        // Implementation for demo
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public boolean unmarkTaskComplete(String taskId) {
        // Implementation for demo
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public boolean existsInAvailable(String taskId) {
        return availableTasks.containsKey(taskId);
    }

    @Override
    public boolean existsInToday(String taskId) {
        return todaysTasks.containsKey(taskId);
    }

    @Override
    public String getTaskName(String taskId) {
        Info info = availableTasks.get(taskId);
        if (info != null) {
            return info.getName();
        }
        Task task = todaysTasks.get(taskId);
        if (task != null) {
            return task.getInfo().getName();
        }
        return null;
    }

    @Override
    public boolean deleteTaskCompletely(String taskId) {
        boolean deletedFromAvailable = availableTasks.remove(taskId) != null;
        boolean deletedFromToday = todaysTasks.remove(taskId) != null;
        return deletedFromAvailable || deletedFromToday;
    }

    @Override
    public List<Task> getTasksWithDueDates() {
        // For demo purposes, return empty list
        return new ArrayList<>();
    }

    @Override
    public List<Task> getOverdueTasks() {
        // For demo purposes, return empty list
        return new ArrayList<>();
    }

    @Override
    public List<Task> getCompletedTasksToday() {
        return todaysTasks.values().stream()
                .filter(Task::isCompleted)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public double getTodaysCompletionRate() {
        if (todaysTasks.isEmpty()) {
            return 0.0;
        }
        long completed = todaysTasks.values().stream()
                .filter(Task::isCompleted)
                .count();
        return (double) completed / todaysTasks.size() * 100;
    }
}