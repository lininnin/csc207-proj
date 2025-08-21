package data_access.strategy;

import entity.Angela.Task.Task;
import entity.Angela.Task.TaskAvailable;
import entity.Angela.Task.TaskAvailableInterf;
import entity.Angela.Task.TaskInterf;
import entity.BeginAndDueDates.BeginAndDueDates;
import entity.info.Info;
import entity.info.InfoFactory;
import use_case.Angela.task.add_to_today.AddToTodayDataAccessInterface;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Strategy for handling add to today operations.
 * Implements Single Responsibility Principle by focusing only on adding tasks to today.
 */
public class AddToTodayStrategy implements AddToTodayDataAccessInterface {
    
    private final Map<String, TaskAvailable> availableTaskTemplates;
    private final Map<String, Task> todaysTasks;
    
    public AddToTodayStrategy(Map<String, TaskAvailable> availableTaskTemplates,
                             Map<String, Task> todaysTasks) {
        this.availableTaskTemplates = availableTaskTemplates;
        this.todaysTasks = todaysTasks;
    }
    
    @Override
    public TaskAvailableInterf getAvailableTaskById(String taskId) {
        return availableTaskTemplates.get(taskId);
    }
    
    @Override
    public TaskInterf addTaskToToday(TaskAvailableInterf taskAvailable, Task.Priority priority, LocalDate dueDate) {
        TaskAvailable template = (TaskAvailable) taskAvailable;
        
        // Create a new Info object as a deep copy using the builder pattern
        InfoFactory infoFactory = new InfoFactory();
        Info newInfo = (Info) infoFactory.create(
            template.getInfo().getName(),
            template.getInfo().getDescription(),
            template.getInfo().getCategory()
        );
        
        // Create dates object - for overdue testing, set begin date to due date if due date is in the past
        LocalDate beginDate = (dueDate != null && dueDate.isBefore(LocalDate.now())) ? dueDate : LocalDate.now();
        BeginAndDueDates dates = new BeginAndDueDates(beginDate, dueDate);
        
        // Create new task instance
        Task newTask = new Task(
            template.getId(), // template task ID
            newInfo,
            dates,
            template.isOneTime()
        );
        
        // Set priority (ID is set in constructor)
        newTask.setPriority(priority);
        
        // Add to today's tasks
        todaysTasks.put(newTask.getId(), newTask);
        
        return newTask;
    }
    
    @Override
    public boolean isTaskInTodaysList(String templateTaskId) {
        return todaysTasks.values().stream()
                .anyMatch(task -> templateTaskId.equals(task.getTemplateTaskId()));
    }
    
    @Override
    public boolean isTaskInTodaysListAndNotOverdue(String templateTaskId) {
        return todaysTasks.values().stream()
                .filter(task -> templateTaskId.equals(task.getTemplateTaskId()))
                .anyMatch(task -> !task.isOverDue());
    }
    
    @Override
    public List<TaskAvailableInterf> getAllAvailableTasksWithDetails() {
        return availableTaskTemplates.values().stream()
                .map(task -> (TaskAvailableInterf) task)
                .collect(java.util.stream.Collectors.toList());
    }
    
    @Override
    public boolean isExactDuplicateInTodaysList(String templateTaskId, Task.Priority priority, LocalDate dueDate) {
        return todaysTasks.values().stream()
                .anyMatch(task -> {
                    boolean templateMatches = templateTaskId.equals(task.getTemplateTaskId());
                    boolean priorityMatches = (priority == null && task.getPriority() == null) ||
                                            (priority != null && priority.equals(task.getPriority()));
                    boolean dueDateMatches = (dueDate == null && task.getDates().getDueDate() == null) ||
                                           (dueDate != null && dueDate.equals(task.getDates().getDueDate()));
                    
                    return templateMatches && priorityMatches && dueDateMatches;
                });
    }
}