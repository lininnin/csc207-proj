package data_access;

import entity.Angela.Task.Task;
import entity.Angela.Task.TaskAvailable;
import entity.Angela.Task.TaskAvailableInterf;
import entity.Angela.Task.TaskInterf;
import entity.info.Info;
import data_access.strategy.*;
import use_case.Angela.task.create.CreateTaskDataAccessInterface;
import use_case.Angela.task.delete.DeleteTaskDataAccessInterface;
import use_case.Angela.task.edit_available.EditAvailableTaskDataAccessInterface;
import use_case.Angela.task.add_to_today.AddToTodayDataAccessInterface;
import use_case.Angela.task.mark_complete.MarkTaskCompleteDataAccessInterface;
import use_case.Angela.task.edit_today.EditTodayTaskDataAccessInterface;
import use_case.Angela.category.edit.EditCategoryTaskDataAccessInterface;
import use_case.Angela.task.remove_from_today.RemoveFromTodayDataAccessInterface;
import use_case.Angela.task.overdue.OverdueTasksDataAccessInterface;
import use_case.Angela.category.delete.DeleteCategoryTaskDataAccessInterface;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Refactored InMemoryTaskDataAccessObject using Strategy Pattern.
 * Implements Single Responsibility Principle by delegating operations to specialized strategies.
 * Following template repository pattern: one concrete implementation implements multiple interfaces.
 */
public class InMemoryTaskDataAccessObject implements 
        CreateTaskDataAccessInterface,
        DeleteTaskDataAccessInterface,
        EditAvailableTaskDataAccessInterface,
        AddToTodayDataAccessInterface,
        MarkTaskCompleteDataAccessInterface,
        EditTodayTaskDataAccessInterface,
        EditCategoryTaskDataAccessInterface,
        RemoveFromTodayDataAccessInterface,
        OverdueTasksDataAccessInterface,
        DeleteCategoryTaskDataAccessInterface {
        
    // Shared data stores
    private final Map<String, Info> availableTasks = Collections.synchronizedMap(new HashMap<>()); // Legacy storage for backward compatibility
    private final Map<String, TaskAvailable> availableTaskTemplates = Collections.synchronizedMap(new HashMap<>()); // New storage for TaskAvailable
    private final Map<String, Task> todaysTasks = Collections.synchronizedMap(new HashMap<>());
    
    // Strategy instances - each handles one responsibility
    private final CreateTaskStrategy createTaskStrategy;
    private final EditAvailableTaskStrategy editAvailableTaskStrategy;
    private final AddToTodayStrategy addToTodayStrategy;
    private final EditTodayTaskStrategy editTodayTaskStrategy;
    private final MarkTaskCompleteStrategy markTaskCompleteStrategy;
    private final RemoveFromTodayStrategy removeFromTodayStrategy;
    private final OverdueTasksStrategy overdueTasksStrategy;
    private final DeleteTaskStrategy deleteTaskStrategy;
    private final EditCategoryTaskStrategy editCategoryTaskStrategy;
    private final DeleteCategoryTaskStrategy deleteCategoryTaskStrategy;
    
    public InMemoryTaskDataAccessObject() {
        // Initialize all strategies with shared data stores
        this.createTaskStrategy = new CreateTaskStrategy(availableTaskTemplates);
        this.editAvailableTaskStrategy = new EditAvailableTaskStrategy(availableTaskTemplates, todaysTasks, availableTasks);
        this.addToTodayStrategy = new AddToTodayStrategy(availableTaskTemplates, todaysTasks);
        this.editTodayTaskStrategy = new EditTodayTaskStrategy(todaysTasks);
        this.markTaskCompleteStrategy = new MarkTaskCompleteStrategy(todaysTasks);
        this.removeFromTodayStrategy = new RemoveFromTodayStrategy(todaysTasks);
        this.overdueTasksStrategy = new OverdueTasksStrategy(todaysTasks);
        this.deleteTaskStrategy = new DeleteTaskStrategy(availableTaskTemplates, todaysTasks, availableTasks);
        this.editCategoryTaskStrategy = new EditCategoryTaskStrategy(availableTaskTemplates, todaysTasks);
        this.deleteCategoryTaskStrategy = new DeleteCategoryTaskStrategy(availableTaskTemplates, todaysTasks);
    }
    
    /**
     * Sets the goal repository for checking goal-task relationships.
     * This is optional and can be null if goal checking is not needed.
     */
    public void setGoalRepository(GoalRepository goalRepository) {
        deleteTaskStrategy.setGoalRepository(goalRepository);
    }
    
    // ===== CreateTaskDataAccessInterface methods =====
    
    @Override
    public String saveTaskAvailable(TaskAvailableInterf taskAvailable) {
        return createTaskStrategy.saveTaskAvailable(taskAvailable);
    }
    
    @Override
    public boolean taskExistsWithNameAndCategory(String name, String categoryId) {
        return createTaskStrategy.taskExistsWithNameAndCategory(name, categoryId);
    }
    
    @Override
    public List<TaskAvailableInterf> getAllAvailableTaskTemplates() {
        return createTaskStrategy.getAllAvailableTaskTemplates();
    }
    
    @Override
    public TaskAvailableInterf getTaskAvailableById(String taskId) {
        return createTaskStrategy.getTaskAvailableById(taskId);
    }
    
    @Override
    public int getAvailableTaskCount() {
        return createTaskStrategy.getAvailableTaskCount();
    }
    
    @Override
    public boolean exists(TaskAvailableInterf taskAvailable) {
        return createTaskStrategy.exists(taskAvailable);
    }
    
    // ===== EditAvailableTaskDataAccessInterface methods =====
    
    @Override
    public boolean updateAvailableTask(String taskId, String newName, String newDescription, 
                                       String newCategoryId, boolean isOneTime) {
        return editAvailableTaskStrategy.updateAvailableTask(taskId, newName, newDescription, newCategoryId, isOneTime);
    }
    
    @Override
    public boolean taskExistsWithNameAndCategoryExcluding(String name, String categoryId, String excludeTaskId) {
        return editAvailableTaskStrategy.taskExistsWithNameAndCategoryExcluding(name, categoryId, excludeTaskId);
    }
    
    @Override
    public List<TaskAvailableInterf> getAllAvailableTasksWithDetails() {
        return editAvailableTaskStrategy.getAllAvailableTasksWithDetails();
    }
    
    // ===== AddToTodayDataAccessInterface methods =====
    
    @Override
    public TaskAvailableInterf getAvailableTaskById(String taskId) {
        return addToTodayStrategy.getAvailableTaskById(taskId);
    }
    
    @Override
    public TaskInterf addTaskToToday(TaskAvailableInterf taskAvailable, Task.Priority priority, LocalDate dueDate) {
        return addToTodayStrategy.addTaskToToday(taskAvailable, priority, dueDate);
    }
    
    @Override
    public boolean isTaskInTodaysList(String templateTaskId) {
        return addToTodayStrategy.isTaskInTodaysList(templateTaskId);
    }
    
    @Override
    public boolean isTaskInTodaysListAndNotOverdue(String templateTaskId) {
        return addToTodayStrategy.isTaskInTodaysListAndNotOverdue(templateTaskId);
    }
    
    @Override
    public boolean isExactDuplicateInTodaysList(String templateTaskId, Task.Priority priority, LocalDate dueDate) {
        return addToTodayStrategy.isExactDuplicateInTodaysList(templateTaskId, priority, dueDate);
    }
    
    // ===== EditTodayTaskDataAccessInterface methods =====
    
    @Override
    public TaskInterf getTodayTaskById(String taskId) {
        return editTodayTaskStrategy.getTodayTaskById(taskId);
    }
    
    @Override
    public boolean updateTodayTaskPriorityAndDueDate(String taskId, Task.Priority priority, LocalDate dueDate) {
        return editTodayTaskStrategy.updateTodayTaskPriorityAndDueDate(taskId, priority, dueDate);
    }
    
    @Override
    public boolean isValidDueDate(LocalDate dueDate) {
        return editTodayTaskStrategy.isValidDueDate(dueDate);
    }
    
    // ===== MarkTaskCompleteDataAccessInterface methods =====
    
    @Override
    public boolean updateTaskCompletionStatus(String taskId, boolean isCompleted) {
        return markTaskCompleteStrategy.updateTaskCompletionStatus(taskId, isCompleted);
    }
    
    // ===== RemoveFromTodayDataAccessInterface methods =====
    
    @Override
    public boolean removeFromTodaysList(String taskId) {
        return removeFromTodayStrategy.removeFromTodaysList(taskId);
    }
    
    // ===== OverdueTasksDataAccessInterface methods =====
    
    @Override
    public List<TaskInterf> getOverdueTasks(int daysBack) {
        return overdueTasksStrategy.getOverdueTasks(daysBack);
    }
    
    @Override
    public List<TaskInterf> getAllOverdueTasks() {
        return overdueTasksStrategy.getAllOverdueTasks();
    }
    
    // ===== DeleteTaskDataAccessInterface methods =====
    
    @Override
    public TaskInterf getTodaysTaskById(String taskId) {
        return deleteTaskStrategy.getTodaysTaskById(taskId);
    }
    
    @Override
    public boolean existsInAvailable(TaskAvailableInterf taskAvailable) {
        return deleteTaskStrategy.existsInAvailable(taskAvailable);
    }
    
    @Override
    public boolean existsInToday(TaskInterf task) {
        return deleteTaskStrategy.existsInToday(task);
    }
    
    @Override
    public boolean templateExistsInToday(String templateTaskId) {
        return deleteTaskStrategy.templateExistsInToday(templateTaskId);
    }
    
    @Override
    public boolean deleteFromAvailable(TaskAvailableInterf taskAvailable) {
        return deleteTaskStrategy.deleteFromAvailable(taskAvailable);
    }
    
    @Override
    public boolean deleteAllTodaysTasksWithTemplate(String templateTaskId) {
        return deleteTaskStrategy.deleteAllTodaysTasksWithTemplate(templateTaskId);
    }
    
    @Override
    public boolean deleteTaskCompletely(String templateTaskId) {
        return deleteTaskStrategy.deleteTaskCompletely(templateTaskId);
    }
    
    @Override
    public List<TaskInterf> getTodaysTasksByTemplate(String templateTaskId) {
        return deleteTaskStrategy.getTodaysTasksByTemplate(templateTaskId);
    }
    
    @Override
    public List<TaskInterf> getAllTodaysTasks() {
        return deleteTaskStrategy.getAllTodaysTasks();
    }
    
    @Override
    public List<String> getGoalNamesTargetingTask(String taskId) {
        return deleteTaskStrategy.getGoalNamesTargetingTask(taskId);
    }
    
    // ===== EditCategoryTaskDataAccessInterface methods =====
    
    @Override
    public List<TaskAvailable> findAvailableTasksByCategory(String categoryId) {
        return editCategoryTaskStrategy.findAvailableTasksByCategory(categoryId);
    }
    
    @Override
    public List<Task> findTodaysTasksByCategory(String categoryId) {
        return editCategoryTaskStrategy.findTodaysTasksByCategory(categoryId);
    }
    
    @Override
    public boolean updateAvailableTaskCategory(String taskId, String newCategoryId) {
        return editCategoryTaskStrategy.updateAvailableTaskCategory(taskId, newCategoryId);
    }
    
    @Override
    public boolean updateTodaysTaskCategory(String taskId, String newCategoryId) {
        return editCategoryTaskStrategy.updateTodaysTaskCategory(taskId, newCategoryId);
    }
    
    // ===== DeleteCategoryTaskDataAccessInterface methods =====
    
    @Override
    public List<TaskAvailable> findAvailableTasksWithEmptyCategory() {
        return deleteCategoryTaskStrategy.findAvailableTasksWithEmptyCategory();
    }
    
    @Override
    public List<Task> findTodaysTasksWithEmptyCategory() {
        return deleteCategoryTaskStrategy.findTodaysTasksWithEmptyCategory();
    }
    
    @Override
    public boolean updateTasksCategoryToNull(String categoryId) {
        return deleteCategoryTaskStrategy.updateTasksCategoryToNull(categoryId);
    }
    
    // ===== Legacy and utility methods for backward compatibility =====
    
    /**
     * Legacy method for backward compatibility.
     */
    public String saveAvailableTask(Info info) {
        String taskId = info.getId();
        availableTasks.put(taskId, info);
        return taskId;
    }
    
    /**
     * Legacy method for backward compatibility.
     */
    public List<Info> getAllAvailableTasks() {
        return new ArrayList<>(availableTasks.values());
    }
    
    /**
     * Legacy method for backward compatibility.
     */
    public boolean availableTaskNameExists(String name) {
        return availableTasks.values().stream()
                .anyMatch(info -> info.getName().equalsIgnoreCase(name));
    }
    
    /**
     * Legacy method for backward compatibility.
     */
    public boolean updateAvailableTask(Info info) {
        if (availableTasks.containsKey(info.getId())) {
            availableTasks.put(info.getId(), info);
            return true;
        }
        return false;
    }
    
    /**
     * Legacy method for backward compatibility.
     */
    public boolean deleteFromAvailable(String taskId) {
        return availableTasks.remove(taskId) != null;
    }
    
    /**
     * Legacy method for backward compatibility.
     */
    public Task addToToday(String taskId, Task.Priority priority, LocalDate dueDate) {
        TaskAvailableInterf taskAvailable = getTaskAvailableById(taskId);
        if (taskAvailable == null) {
            throw new IllegalArgumentException("Task not found: " + taskId);
        }
        return (Task) addTaskToToday(taskAvailable, priority, dueDate);
    }
    
    /**
     * Get today's tasks with sorting.
     */
    public List<Task> getTodaysTasks() {
        List<Task> tasks = new ArrayList<>(todaysTasks.values());
        
        // Sort tasks alphabetically by name (case-insensitive)
        tasks.sort((a, b) -> {
            String nameA = a.getInfo().getName();
            String nameB = b.getInfo().getName();
            if (nameA == null && nameB == null) return 0;
            if (nameA == null) return 1;
            if (nameB == null) return -1;
            return nameA.compareToIgnoreCase(nameB);
        });
        
        return tasks;
    }
    
    /**
     * Get available task templates as concrete type for backward compatibility.
     */
    public List<TaskAvailable> getAvailableTaskTemplates() {
        return new ArrayList<>(availableTaskTemplates.values());
    }
    
    /**
     * Clears all data from this data access object for testing purposes.
     * WARNING: This will delete all tasks and should only be used in tests!
     */
    public void clearAllData() {
        availableTasks.clear();
        availableTaskTemplates.clear();
        todaysTasks.clear();
    }
    
    /**
     * Removes a task from today's list.
     * Legacy method for backward compatibility.
     */
    public boolean removeFromToday(String taskId) {
        return removeFromTodayStrategy.removeFromTodaysList(taskId);
    }
}