package data_access;

import entity.Angela.Task.TaskInterf;
import entity.Angela.Task.TaskAvailableInterf;
import entity.Sophia.GoalInterface;
import use_case.Angela.task.delete.DeleteTaskDataAccessInterface;
import use_case.Angela.today_so_far.TodaySoFarDataAccessInterface;
import entity.Alex.Event.EventInterf;
import entity.Alex.WellnessLogEntry.WellnessLogEntryInterf;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Composite repository that combines AvailableTaskRepository and TodayTaskRepository
 * to implement interfaces that need access to both types of tasks.
 * Follows the Composite pattern and Dependency Injection principle.
 */
public class CompositeTaskRepository implements 
        DeleteTaskDataAccessInterface,
        TodaySoFarDataAccessInterface {
        
    private final AvailableTaskRepository availableTaskRepository;
    private final TodayTaskRepository todayTaskRepository;
    private GoalRepository goalRepository; // Optional dependency

    public CompositeTaskRepository(AvailableTaskRepository availableTaskRepository, 
                                 TodayTaskRepository todayTaskRepository) {
        this.availableTaskRepository = availableTaskRepository;
        this.todayTaskRepository = todayTaskRepository;
    }

    /**
     * Sets the goal repository for checking goal-task relationships.
     * This is optional and can be null if goal checking is not needed.
     */
    public void setGoalRepository(GoalRepository goalRepository) {
        this.goalRepository = goalRepository;
    }

    // DeleteTaskDataAccessInterface methods

    /**
     * Retrieves a task template by its ID.
     *
     * @param taskId The ID of the task template to find
     * @return The TaskAvailableInterf if found, null otherwise
     */
    @Override
    public TaskAvailableInterf getTaskAvailableById(String taskId) {
        return availableTaskRepository.getTaskAvailableById(taskId);
    }

    /**
     * Retrieves a today's task by its ID.
     *
     * @param taskId The ID of the today's task to find
     * @return The TaskInterf if found, null otherwise
     */
    @Override
    public TaskInterf getTodaysTaskById(String taskId) {
        return todayTaskRepository.getTodayTaskById(taskId);
    }

    /**
     * Checks whether the given task template exists in Available Tasks.
     *
     * @param taskAvailable The TaskAvailableInterf to check
     * @return true if present, false otherwise
     */
    @Override
    public boolean existsInAvailable(TaskAvailableInterf taskAvailable) {
        return availableTaskRepository.exists(taskAvailable);
    }

    /**
     * Checks whether the given task instance exists in Today's Tasks.
     *
     * @param task The TaskInterf to check
     * @return true if present, false otherwise
     */
    @Override
    public boolean existsInToday(TaskInterf task) {
        return todayTaskRepository.getTodayTaskById(task.getId()) != null;
    }

    /**
     * Checks if a task template exists in Today's Tasks by its template ID.
     *
     * @param templateTaskId The ID of the task template
     * @return true if any today's task references this template, false otherwise
     */
    @Override
    public boolean templateExistsInToday(String templateTaskId) {
        return todayTaskRepository.isTaskInTodaysList(templateTaskId);
    }

    /**
     * Removes the given task template from Available Tasks.
     *
     * @param taskAvailable The TaskAvailableInterf to remove
     * @return true if removed successfully, false otherwise
     */
    @Override
    public boolean deleteFromAvailable(TaskAvailableInterf taskAvailable) {
        if (taskAvailable == null) return false;
        return availableTaskRepository.removeTaskTemplate(taskAvailable.getId());
    }

    /**
     * Removes all today's task instances that reference the given template ID.
     *
     * @param templateTaskId The template task ID
     * @return true if all instances were removed successfully, false otherwise
     */
    @Override
    public boolean deleteAllTodaysTasksWithTemplate(String templateTaskId) {
        List<TaskInterf> tasksToRemove = todayTaskRepository.getTasksByTemplate(templateTaskId);
        boolean allRemoved = true;
        
        for (TaskInterf task : tasksToRemove) {
            boolean removed = todayTaskRepository.removeFromTodaysList(task.getId());
            if (!removed) {
                allRemoved = false;
            }
        }
        
        return allRemoved;
    }

    /**
     * Completely removes a task from both Available and Today's lists.
     *
     * @param templateTaskId The template task ID to remove completely
     * @return true if removed from both lists successfully, false otherwise
     */
    @Override
    public boolean deleteTaskCompletely(String templateTaskId) {
        // Remove from today's tasks first
        boolean todayRemoved = deleteAllTodaysTasksWithTemplate(templateTaskId);
        
        // Remove from available tasks
        boolean availableRemoved = availableTaskRepository.removeTaskTemplate(templateTaskId);
        
        return todayRemoved && availableRemoved;
    }

    /**
     * Gets all today's tasks that reference a specific template.
     *
     * @param templateTaskId The template task ID
     * @return List of TaskInterf instances that reference this template
     */
    @Override
    public List<TaskInterf> getTodaysTasksByTemplate(String templateTaskId) {
        return todayTaskRepository.getTasksByTemplate(templateTaskId);
    }

    /**
     * @return List of all available task templates
     */
    @Override
    public List<TaskAvailableInterf> getAllAvailableTaskTemplates() {
        return availableTaskRepository.getAllAvailableTaskTemplates();
    }

    /**
     * @return List of all today's tasks
     */
    @Override
    public List<TaskInterf> getAllTodaysTasks() {
        return todayTaskRepository.getAllTodaysTasks();
    }

    /**
     * Checks if the given task is a target task for any goal.
     *
     * @param taskId The ID of the task to check
     * @return List of goal names that reference this task as target, empty if none
     */
    @Override
    public List<String> getGoalNamesTargetingTask(String taskId) {
        if (goalRepository == null) {
            return new ArrayList<>(); // Return empty list if no goal repository
        }
        
        // Note: This method doesn't exist on GoalRepository yet
        // For now return empty list - this needs to be implemented in GoalRepository
        return new ArrayList<>();
    }

    // TodaySoFarDataAccessInterface methods

    /**
     * Gets all completed tasks for today.
     * @return List of completed tasks for today
     */
    @Override
    public List<TaskInterf> getCompletedTasksForToday() {
        return todayTaskRepository.getCompletedTasksForToday();
    }

    /**
     * Gets all completed events for today.
     * Note: This should delegate to an EventRepository in a complete implementation.
     * @return List of completed events for today
     */
    @Override
    public List<EventInterf> getCompletedEventsForToday() {
        // This would delegate to an EventRepository
        // For now, return empty list as this focuses on tasks
        return new ArrayList<>();
    }

    /**
     * Gets wellness log entries for today.
     * Note: This should delegate to a WellnessRepository in a complete implementation.
     * @return List of wellness entries for today
     */
    @Override
    public List<WellnessLogEntryInterf> getWellnessEntriesForToday() {
        // This would delegate to a WellnessRepository
        // For now, return empty list as this focuses on tasks
        return new ArrayList<>();
    }

    /**
     * Gets all active goals with their progress.
     * @return List of active goals
     */
    @Override
    public List<GoalInterface> getActiveGoals() {
        if (goalRepository == null) {
            return new ArrayList<>();
        }
        
        // Note: This method doesn't exist on GoalRepository yet and isActive() may not exist
        // For now return empty list - this needs to be implemented in GoalRepository
        return new ArrayList<>();
    }

    /**
     * Gets the task completion rate for today.
     * @return Completion rate as a percentage (0-100)
     */
    @Override
    public int getTodayTaskCompletionRate() {
        return todayTaskRepository.getTodayTaskCompletionRate();
    }

    /**
     * Gets the total number of tasks for today.
     * @return Total task count
     */
    @Override
    public int getTotalTasksForToday() {
        return todayTaskRepository.getAllTodaysTasks().size();
    }

    /**
     * Gets the total number of completed tasks for today.
     * @return Completed task count
     */
    @Override
    public int getCompletedTasksCountForToday() {
        return todayTaskRepository.getCompletedTasksForToday().size();
    }
}