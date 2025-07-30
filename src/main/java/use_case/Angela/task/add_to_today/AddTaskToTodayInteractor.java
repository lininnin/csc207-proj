package use_case.Angela.task.add_to_today;

import entity.Angela.Task.Task;
import use_case.Angela.task.TaskGateway;
import java.time.LocalDate;

/**
 * Interactor for adding a task to today.
 */
public class AddTaskToTodayInteractor implements AddTaskToTodayInputBoundary {
    private final TaskGateway taskGateway;
    private final AddTaskToTodayOutputBoundary outputBoundary;

    public AddTaskToTodayInteractor(TaskGateway taskGateway,
                                    AddTaskToTodayOutputBoundary outputBoundary) {
        this.taskGateway = taskGateway;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(AddTaskToTodayInputData inputData) {
        String taskId = inputData.getTaskId();

        // Check if task exists in available
        if (!taskGateway.existsInAvailable(taskId)) {
            outputBoundary.presentError("Task not found in Available Tasks");
            return;
        }

        // Check if already in today
        if (taskGateway.existsInToday(taskId)) {
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
        Task task = taskGateway.addToToday(taskId, inputData.getPriority(), dueDate);
        String taskName = taskGateway.getTaskName(taskId);

        AddTaskToTodayOutputData outputData = new AddTaskToTodayOutputData(task, taskName);
        outputBoundary.presentSuccess(outputData);
    }
}