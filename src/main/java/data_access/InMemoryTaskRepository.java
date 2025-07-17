package data_access;

import entity.Task;
import use_case.TaskRepository;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * In-memory implementation of TaskRepository for demo purposes.
 * In production, this would be replaced with file or database storage.
 */
public class InMemoryTaskRepository implements TaskRepository {
    private final Map<String, Task> tasks = new HashMap<>();
    private final Set<String> todaysTaskIds = new HashSet<>();

    @Override
    public void save(Task task) {
        tasks.put(task.getInfo().getId(), task);
    }

    @Override
    public void update(Task task) {
        tasks.put(task.getInfo().getId(), task);
    }

    @Override
    public Task findById(String taskId) {
        return tasks.get(taskId);
    }

    @Override
    public List<Task> findAll() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Task> findTodaysTasks() {
        LocalDate today = LocalDate.now();
        return tasks.values().stream()
                .filter(task -> {
                    LocalDate beginDate = task.getBeginAndDueDates().getBeginDate();
                    return beginDate.equals(today) || todaysTaskIds.contains(task.getInfo().getId());
                })
                .filter(task -> !task.isComplete())
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> findOverdueTasks() {
        return tasks.values().stream()
                .filter(Task::isOverdue)
                .collect(Collectors.toList());
    }

    @Override
    public void addToTodaysTasks(Task task) {
        todaysTaskIds.add(task.getInfo().getId());
    }

    @Override
    public List<Task> findByCategory(String category) {
        return tasks.values().stream()
                .filter(task -> category.equals(task.getInfo().getCategory()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> findByPriority(Task.Priority priority) {
        return tasks.values().stream()
                .filter(task -> task.getTaskPriority() == priority)
                .collect(Collectors.toList());
    }

    /**
     * Additional method to get completed tasks for today.
     */
    public List<Task> findTodaysCompletedTasks() {
        LocalDate today = LocalDate.now();
        return tasks.values().stream()
                .filter(Task::isComplete)
                .filter(task -> {
                    if (task.getCompletedDateTime() != null) {
                        return task.getCompletedDateTime().toLocalDate().equals(today);
                    }
                    return false;
                })
                .collect(Collectors.toList());
    }
}
