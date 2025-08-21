package data_access;

import entity.Angela.Task.Task;
import entity.Angela.Task.TaskAvailable;
import entity.Angela.Task.TaskInterf;
import entity.Angela.Task.TaskAvailableInterf;
import use_case.Angela.task.create.CreateTaskDataAccessInterface;
import use_case.Angela.task.edit_available.EditAvailableTaskDataAccessInterface;
import use_case.Angela.task.add_to_today.AddToTodayDataAccessInterface;
import use_case.Angela.task.edit_today.EditTodayTaskDataAccessInterface;
import use_case.Angela.task.mark_complete.MarkTaskCompleteDataAccessInterface;
import use_case.Angela.task.remove_from_today.RemoveFromTodayDataAccessInterface;
import use_case.Angela.task.overdue.OverdueTasksDataAccessInterface;
import use_case.Angela.task.delete.DeleteTaskDataAccessInterface;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Adapter for InMemoryTaskGateway that implements the new interface types.
 * This adapter converts between interface types and concrete types for the legacy gateway.
 * This is a temporary solution to avoid rewriting the massive InMemoryTaskGateway file.
 */
public class InMemoryTaskGatewayAdapter implements 
        CreateTaskDataAccessInterface,
        EditAvailableTaskDataAccessInterface,
        AddToTodayDataAccessInterface,
        EditTodayTaskDataAccessInterface,
        MarkTaskCompleteDataAccessInterface,
        RemoveFromTodayDataAccessInterface,
        OverdueTasksDataAccessInterface,
        DeleteTaskDataAccessInterface {
        
    private final InMemoryTaskGateway gateway;

    public InMemoryTaskGatewayAdapter(InMemoryTaskGateway gateway) {
        this.gateway = gateway;
    }

    // CreateTaskDataAccessInterface methods
    
    @Override
    public String saveTaskAvailable(TaskAvailableInterf taskAvailable) {
        // Convert interface to concrete type and call the correct gateway method
        TaskAvailable concrete = (TaskAvailable) taskAvailable;
        return gateway.saveTaskAvailable(concrete);
    }

    @Override
    public boolean taskExistsWithNameAndCategory(String name, String categoryId) {
        return gateway.taskExistsWithNameAndCategory(name, categoryId);
    }

    @Override
    public List<TaskAvailableInterf> getAllAvailableTaskTemplates() {
        return gateway.getAllAvailableTaskTemplates().stream()
                .map(task -> (TaskAvailableInterf) task)
                .collect(Collectors.toList());
    }

    @Override
    public TaskAvailableInterf getTaskAvailableById(String taskId) {
        return gateway.getTaskAvailableById(taskId);
    }
    
    // Method needed by AddToTodayDataAccessInterface (same implementation as above)
    public TaskAvailableInterf getAvailableTaskById(String taskId) {
        return gateway.getTaskAvailableById(taskId);
    }

    @Override
    public int getAvailableTaskCount() {
        return gateway.getAvailableTaskTemplates().size();
    }

    @Override
    public boolean exists(TaskAvailableInterf taskAvailable) {
        return gateway.existsInAvailable((TaskAvailable) taskAvailable);
    }

    // EditAvailableTaskDataAccessInterface methods
    
    @Override
    public boolean updateAvailableTask(String taskId, String newName, String newDescription, 
                                       String newCategoryId, boolean isOneTime) {
        return gateway.updateAvailableTask(taskId, newName, newDescription, newCategoryId, isOneTime);
    }

    @Override
    public boolean taskExistsWithNameAndCategoryExcluding(String name, String categoryId, String excludeTaskId) {
        return gateway.taskExistsWithNameAndCategoryExcluding(name, categoryId, excludeTaskId);
    }

    @Override
    public List<TaskAvailableInterf> getAllAvailableTasksWithDetails() {
        return getAllAvailableTaskTemplates();
    }

    // AddToTodayDataAccessInterface methods
    
    // This method is implemented both in CreateTaskDataAccessInterface and AddToTodayDataAccessInterface
    // The implementations are identical, so we just delegate to the same gateway method
    
    @Override
    public TaskInterf addTaskToToday(TaskAvailableInterf taskAvailable, Task.Priority priority, LocalDate dueDate) {
        TaskAvailable concrete = (TaskAvailable) taskAvailable;
        return gateway.addToToday(concrete.getId(), priority, dueDate);
    }

    @Override
    public boolean isTaskInTodaysList(String templateTaskId) {
        return gateway.templateExistsInToday(templateTaskId);
    }

    @Override
    public boolean isTaskInTodaysListAndNotOverdue(String templateTaskId) {
        // Find tasks with this template ID that are not overdue
        return gateway.getTodaysTasksByTemplate(templateTaskId).stream()
                .anyMatch(task -> !task.isOverDue());
    }

    @Override
    public boolean isExactDuplicateInTodaysList(String templateTaskId, Task.Priority priority, LocalDate dueDate) {
        return gateway.isExactDuplicateInTodaysList(templateTaskId, priority, dueDate);
    }

    // EditTodayTaskDataAccessInterface methods
    
    @Override
    public TaskInterf getTodayTaskById(String taskId) {
        return gateway.getTodayTaskById(taskId);
    }

    @Override
    public boolean updateTodayTaskPriorityAndDueDate(String taskId, Task.Priority priority, LocalDate dueDate) {
        return gateway.updateTodayTaskPriorityAndDueDate(taskId, priority, dueDate);
    }

    @Override
    public boolean isValidDueDate(LocalDate dueDate) {
        return gateway.isValidDueDate(dueDate);
    }

    // MarkTaskCompleteDataAccessInterface methods
    
    @Override
    public boolean updateTaskCompletionStatus(String taskId, boolean isCompleted) {
        return gateway.updateTaskCompletionStatus(taskId, isCompleted);
    }

    // RemoveFromTodayDataAccessInterface methods
    
    @Override
    public boolean removeFromTodaysList(String taskId) {
        return gateway.removeFromToday(taskId);
    }

    // OverdueTasksDataAccessInterface methods
    
    @Override
    public List<TaskInterf> getOverdueTasks(int daysBack) {
        return gateway.getOverdueTasks(daysBack).stream()
                .map(task -> (TaskInterf) task)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskInterf> getAllOverdueTasks() {
        return gateway.getAllOverdueTasks().stream()
                .map(task -> (TaskInterf) task)
                .collect(Collectors.toList());
    }

    // DeleteTaskDataAccessInterface methods
    
    @Override
    public TaskInterf getTodaysTaskById(String taskId) {
        return gateway.getTodayTaskById(taskId);
    }
    
    @Override
    public boolean existsInAvailable(TaskAvailableInterf taskAvailable) {
        return gateway.existsInAvailable((TaskAvailable) taskAvailable);
    }

    @Override
    public boolean existsInToday(TaskInterf task) {
        return gateway.existsInToday((Task) task);
    }

    @Override
    public boolean templateExistsInToday(String templateTaskId) {
        return gateway.templateExistsInToday(templateTaskId);
    }

    @Override
    public boolean deleteFromAvailable(TaskAvailableInterf taskAvailable) {
        return gateway.deleteFromAvailable((TaskAvailable) taskAvailable);
    }

    @Override
    public boolean deleteAllTodaysTasksWithTemplate(String templateTaskId) {
        return gateway.deleteAllTodaysTasksWithTemplate(templateTaskId);
    }

    @Override
    public boolean deleteTaskCompletely(String templateTaskId) {
        return gateway.deleteTaskCompletely(templateTaskId);
    }

    @Override
    public List<TaskInterf> getTodaysTasksByTemplate(String templateTaskId) {
        return gateway.getTodaysTasksByTemplate(templateTaskId).stream()
                .map(task -> (TaskInterf) task)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskInterf> getAllTodaysTasks() {
        return gateway.getTodaysTasks().stream()
                .map(task -> (TaskInterf) task)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getGoalNamesTargetingTask(String taskId) {
        return gateway.getGoalNamesTargetingTask(taskId);
    }
}