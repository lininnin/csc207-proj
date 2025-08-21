package data_access;

import entity.Angela.Task.Task;
import entity.Angela.Task.TaskInterf;
import entity.Angela.Task.TaskAvailableInterf;
import entity.Angela.Task.TaskFactory;
import entity.BeginAndDueDates.BeginAndDueDates;
import entity.info.Info;
import use_case.Angela.task.add_to_today.AddToTodayDataAccessInterface;
import use_case.Angela.task.edit_today.EditTodayTaskDataAccessInterface;
import use_case.Angela.task.mark_complete.MarkTaskCompleteDataAccessInterface;
import use_case.Angela.task.remove_from_today.RemoveFromTodayDataAccessInterface;
import use_case.Angela.task.overdue.OverdueTasksDataAccessInterface;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Repository for managing Today's Task instances.
 * Follows SRP by focusing only on Today's Task operations.
 * Implements multiple DataAccessInterfaces for different use cases.
 */
public class TodayTaskRepository implements 
        AddToTodayDataAccessInterface,
        EditTodayTaskDataAccessInterface,
        MarkTaskCompleteDataAccessInterface,
        RemoveFromTodayDataAccessInterface,
        OverdueTasksDataAccessInterface {
        
    private final Map<String, Task> todaysTasks = Collections.synchronizedMap(new HashMap<>());
    private final TaskFactory taskFactory;

    public TodayTaskRepository(TaskFactory taskFactory) {
        this.taskFactory = taskFactory;
    }

    // AddToTodayDataAccessInterface methods

    /**
     * Gets an available task by its ID.
     * Note: This method delegates to AvailableTaskRepository in practice.
     * 
     * @param taskId the ID of the task to retrieve
     * @return the TaskAvailableInterf if found, null otherwise
     */
    @Override
    public TaskAvailableInterf getAvailableTaskById(String taskId) {
        // This method would typically delegate to AvailableTaskRepository
        // For now, returning null as this repository focuses on today's tasks
        throw new UnsupportedOperationException("This method should delegate to AvailableTaskRepository");
    }

    /**
     * Adds a task to today's list with the specified priority and due date.
     * 
     * @param taskAvailable the available task template to add
     * @param priority the priority for today's task (optional, can be null)
     * @param dueDate the due date for today's task (optional, can be null)
     * @return the created TaskInterf instance that was added to today's list
     */
    @Override
    public TaskInterf addTaskToToday(TaskAvailableInterf taskAvailable, Task.Priority priority, LocalDate dueDate) {
        if (taskAvailable == null) {
            throw new IllegalArgumentException("TaskAvailable cannot be null");
        }

        // Create dates with today as begin date
        BeginAndDueDates dates = new BeginAndDueDates(LocalDate.now(), dueDate);

        // Use factory to create the task
        TaskInterf task = taskFactory.create(taskAvailable.getId(), taskAvailable.getInfo(), dates, taskAvailable.isOneTime());
        
        // Set priority if provided using immutable update
        if (priority != null) {
            task = ((Task) task).withPriority(priority);
        }

        // Store the concrete task
        todaysTasks.put(task.getId(), (Task) task);
        return task;
    }

    /**
     * Checks if a task with the given template ID already exists in today's list.
     * 
     * @param templateTaskId the template task ID to check
     * @return true if a task with this template already exists in today's list, false otherwise
     */
    @Override
    public boolean isTaskInTodaysList(String templateTaskId) {
        return todaysTasks.values().stream()
                .anyMatch(task -> task.getTemplateTaskId().equals(templateTaskId));
    }

    /**
     * Checks if a task is in today's list and NOT overdue.
     * 
     * @param templateTaskId the template task ID to check
     * @return true if task is in today's list and not overdue, false otherwise
     */
    @Override
    public boolean isTaskInTodaysListAndNotOverdue(String templateTaskId) {
        return todaysTasks.values().stream()
                .filter(task -> task.getTemplateTaskId().equals(templateTaskId))
                .anyMatch(task -> !task.isOverDue());
    }

    /**
     * Gets all available tasks with their full details.
     * Note: This method delegates to AvailableTaskRepository in practice.
     * 
     * @return list of all available tasks
     */
    @Override
    public List<TaskAvailableInterf> getAllAvailableTasksWithDetails() {
        // This method would typically delegate to AvailableTaskRepository
        throw new UnsupportedOperationException("This method should delegate to AvailableTaskRepository");
    }

    /**
     * Checks if an exact duplicate task already exists in today's list.
     * 
     * @param templateTaskId the template task ID to check
     * @param priority the priority to check (can be null)
     * @param dueDate the due date to check (can be null)
     * @return true if an exact duplicate exists, false otherwise
     */
    @Override
    public boolean isExactDuplicateInTodaysList(String templateTaskId, Task.Priority priority, LocalDate dueDate) {
        return todaysTasks.values().stream()
                .filter(task -> task.getTemplateTaskId().equals(templateTaskId))
                .anyMatch(task -> {
                    boolean priorityMatch = Objects.equals(task.getPriority(), priority);
                    boolean dueDateMatch = Objects.equals(task.getBeginAndDueDates().getDueDate(), dueDate);
                    return priorityMatch && dueDateMatch;
                });
    }

    // EditTodayTaskDataAccessInterface methods

    /**
     * Gets a task from today's list by its ID.
     *
     * @param taskId The ID of the task
     * @return The task if found, null otherwise
     */
    @Override
    public TaskInterf getTodayTaskById(String taskId) {
        return todaysTasks.get(taskId);
    }

    /**
     * Updates the priority and due date of a task in today's list.
     *
     * @param taskId The ID of the task to update
     * @param priority The new priority (can be null to clear priority)
     * @param dueDate The new due date (can be null to clear due date)
     * @return true if the update was successful, false if task not found
     */
    @Override
    public boolean updateTodayTaskPriorityAndDueDate(String taskId, Task.Priority priority, LocalDate dueDate) {
        Task task = todaysTasks.get(taskId);
        if (task == null) {
            return false;
        }

        try {
            // Use immutable update methods
            Task updatedTask = task.withPriority(priority);
            
            // Update due date using immutable pattern
            BeginAndDueDates newDates = task.getDates().withDueDate(dueDate);
            
            // Create new task with updated dates
            Task finalTask = new Task(task.getId(), task.getTemplateTaskId(), (Info) task.getInfo(), 
                                    priority, newDates, task.isCompleted(), 
                                    task.getCompletedDateTime(), task.isOneTime());
            
            todaysTasks.put(taskId, finalTask);
            return true;
        } catch (Exception e) {
            System.err.println("Error updating today's task: " + e.getMessage());
            return false;
        }
    }

    /**
     * Validates that a due date is not before today.
     *
     * @param dueDate The date to validate
     * @return true if the date is valid (today or future), false otherwise
     */
    @Override
    public boolean isValidDueDate(LocalDate dueDate) {
        if (dueDate == null) return true; // null due dates are allowed
        return !dueDate.isBefore(LocalDate.now());
    }

    // MarkTaskCompleteDataAccessInterface methods

    /**
     * Updates the completion status of a task in today's list.
     *
     * @param taskId The ID of the task to update
     * @param isCompleted The new completion status
     * @return true if the update was successful, false otherwise
     */
    @Override
    public boolean updateTaskCompletionStatus(String taskId, boolean isCompleted) {
        Task task = todaysTasks.get(taskId);
        if (task == null) {
            return false;
        }

        try {
            Task updatedTask;
            if (isCompleted) {
                updatedTask = task.withCompletedStatus();
            } else {
                updatedTask = task.withUncompletedStatus();
            }
            
            todaysTasks.put(taskId, updatedTask);
            return true;
        } catch (Exception e) {
            System.err.println("Error updating task completion status: " + e.getMessage());
            return false;
        }
    }

    // RemoveFromTodayDataAccessInterface methods

    /**
     * Removes a task from today's list only.
     *
     * @param taskId The ID of the task to remove from today
     * @return true if the removal was successful, false otherwise
     */
    @Override
    public boolean removeFromTodaysList(String taskId) {
        return todaysTasks.remove(taskId) != null;
    }

    // OverdueTasksDataAccessInterface methods

    /**
     * Gets all overdue tasks from Today's list within the specified days back.
     * 
     * @param daysBack Number of days to look back for overdue tasks
     * @return List of overdue tasks sorted by most overdue first
     */
    @Override
    public List<TaskInterf> getOverdueTasks(int daysBack) {
        LocalDate cutoffDate = LocalDate.now().minusDays(daysBack);
        
        return todaysTasks.values().stream()
                .filter(task -> task.isOverDue())
                .filter(task -> {
                    LocalDate dueDate = task.getBeginAndDueDates().getDueDate();
                    return dueDate != null && !dueDate.isBefore(cutoffDate);
                })
                .sorted((t1, t2) -> {
                    LocalDate date1 = t1.getBeginAndDueDates().getDueDate();
                    LocalDate date2 = t2.getBeginAndDueDates().getDueDate();
                    return date1.compareTo(date2); // Most overdue (earliest date) first
                })
                .map(task -> (TaskInterf) task)
                .collect(Collectors.toList());
    }

    /**
     * Gets all overdue tasks from Today's list regardless of time range.
     * 
     * @return List of all overdue tasks sorted by most overdue first
     */
    @Override
    public List<TaskInterf> getAllOverdueTasks() {
        return todaysTasks.values().stream()
                .filter(task -> task.isOverDue())
                .sorted((t1, t2) -> {
                    LocalDate date1 = t1.getBeginAndDueDates().getDueDate();
                    LocalDate date2 = t2.getBeginAndDueDates().getDueDate();
                    return date1.compareTo(date2); // Most overdue (earliest date) first
                })
                .map(task -> (TaskInterf) task)
                .collect(Collectors.toList());
    }

    // Additional utility methods

    /**
     * Gets all tasks in today's list.
     * 
     * @return List of all today's tasks
     */
    public List<TaskInterf> getAllTodaysTasks() {
        return todaysTasks.values().stream()
                .map(task -> (TaskInterf) task)
                .collect(Collectors.toList());
    }

    /**
     * Gets completed tasks for today.
     * 
     * @return List of completed tasks
     */
    public List<TaskInterf> getCompletedTasksForToday() {
        return todaysTasks.values().stream()
                .filter(task -> task.getStatus())
                .map(task -> (TaskInterf) task)
                .collect(Collectors.toList());
    }

    /**
     * Gets today's task completion rate.
     * 
     * @return Completion rate as a percentage (0-100)
     */
    public int getTodayTaskCompletionRate() {
        if (todaysTasks.isEmpty()) {
            return 0;
        }
        
        long completedCount = todaysTasks.values().stream()
                .mapToLong(task -> task.getStatus() ? 1 : 0)
                .sum();
                
        return (int) Math.round((double) completedCount / todaysTasks.size() * 100);
    }

    /**
     * Gets tasks by template ID.
     * 
     * @param templateTaskId The template task ID
     * @return List of tasks that reference this template
     */
    public List<TaskInterf> getTasksByTemplate(String templateTaskId) {
        return todaysTasks.values().stream()
                .filter(task -> task.getTemplateTaskId().equals(templateTaskId))
                .map(task -> (TaskInterf) task)
                .collect(Collectors.toList());
    }
}