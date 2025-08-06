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
        System.out.println("DEBUG [AddTaskToTodayInteractor]: execute() called");
        String taskId = inputData.getTaskId();
        System.out.println("DEBUG [AddTaskToTodayInteractor]: Task ID: " + taskId + 
                          ", Priority: " + inputData.getPriority() + 
                          ", Due Date: " + inputData.getDueDate());

        // Get the available task
        TaskAvailable taskAvailable = dataAccess.getAvailableTaskById(taskId);
        if (taskAvailable == null) {
            outputBoundary.presentError("Task not found in Available Tasks");
            return;
        }

        // Check if already in today (by template ID)
        if (dataAccess.isTaskInTodaysList(taskId)) {
            outputBoundary.presentError("Task is already in Today's Tasks");
            return;
        }

        // Validate due date
        LocalDate dueDate = inputData.getDueDate();
        // TEMPORARY: Modified for testing overdue functionality
        // In production, overdue tasks should go directly to Overdue list, not Today's list
        // But for testing, we allow past dates to demonstrate the overdue functionality
        if (dueDate != null && dueDate.isBefore(LocalDate.now())) {
            System.out.println("DEBUG [AddTaskToTodayInteractor]: WARNING - Adding task with past due date for testing");
            // In production, this would be:
            // outputBoundary.presentError("Tasks with past due dates cannot be added to Today's list");
            // return;
        }
        System.out.println("DEBUG [AddTaskToTodayInteractor]: Due date validation skipped for testing");

        // Add to today
        System.out.println("DEBUG [AddTaskToTodayInteractor]: Adding task to today's list");
        Task task = dataAccess.addTaskToToday(taskAvailable, inputData.getPriority(), dueDate);
        String taskName = taskAvailable.getInfo().getName();
        System.out.println("DEBUG [AddTaskToTodayInteractor]: Task added successfully - Name: " + taskName);

        AddTaskToTodayOutputData outputData = new AddTaskToTodayOutputData(task, taskName);
        outputBoundary.presentSuccess(outputData);
    }
}