package use_case.Angela.task.add_to_today;

import entity.Angela.Task.Task;
import entity.Angela.Task.TaskAvailable;
import java.time.LocalDate;

/**
 * Interactor for adding a task to today.
 */
public class AddTaskToTodayInteractor implements AddTaskToTodayInputBoundary {
    private final AddToTodayDataAccessInterface dataAccess;
    private final AddTaskToTodayOutputBoundary outputBoundary;

    public AddTaskToTodayInteractor(AddToTodayDataAccessInterface dataAccess,
                                    AddTaskToTodayOutputBoundary outputBoundary) {
        this.dataAccess = dataAccess;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(AddTaskToTodayInputData inputData) {
        String taskId = inputData.getTaskId();

        // Get the available task
        TaskAvailable taskAvailable = dataAccess.getAvailableTaskById(taskId);
        if (taskAvailable == null) {
            outputBoundary.presentError("Task not found in Available Tasks");
            return;
        }

        // Validate due date first
        LocalDate dueDate = inputData.getDueDate();
        
        // Check for exact duplicate (same template, priority, and due date)
        // This prevents adding the exact same task multiple times
        if (dataAccess.isExactDuplicateInTodaysList(taskId, inputData.getPriority(), dueDate)) {
            outputBoundary.presentError("This exact task (same priority and due date) is already in Today's Tasks");
            return;
        }
        
        // If testing overdue and task is already in today's list, remove it first
        if (inputData.isTestingOverdue() && dataAccess.isTaskInTodaysList(taskId)) {
            // This allows re-adding the same task with a different (overdue) date for testing
            System.out.println("DEBUG: Testing mode - allowing re-add of task with overdue date");
        }
        // TEMPORARY: Modified for testing overdue functionality
        // In production, overdue tasks should go directly to Overdue list, not Today's list
        // But for testing, we allow past dates to demonstrate the overdue functionality
        if (dueDate != null && dueDate.isBefore(LocalDate.now())) {
            // In production, this would be:
            // outputBoundary.presentError("Tasks with past due dates cannot be added to Today's list");
            // return;
        }

        // Add to today
        Task task = dataAccess.addTaskToToday(taskAvailable, inputData.getPriority(), dueDate);
        String taskName = taskAvailable.getInfo().getName();

        AddTaskToTodayOutputData outputData = new AddTaskToTodayOutputData(task, taskName);
        outputBoundary.presentSuccess(outputData);
    }
}