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

        // Check if already in today (by template ID)
        if (dataAccess.isTaskInTodaysList(taskId)) {
            outputBoundary.presentError("Task is already in Today's Tasks");
            return;
        }

        // Validate due date
        LocalDate dueDate = inputData.getDueDate();
        if (dueDate != null && dueDate.isBefore(LocalDate.now())) {
            outputBoundary.presentError("Due date cannot be a date earlier than today");
            return;
        }

        // Add to today
        Task task = dataAccess.addTaskToToday(taskAvailable, inputData.getPriority(), dueDate);
        String taskName = taskAvailable.getInfo().getName();

        AddTaskToTodayOutputData outputData = new AddTaskToTodayOutputData(task, taskName);
        outputBoundary.presentSuccess(outputData);
    }
}