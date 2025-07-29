package use_case.Angela.task.edit_todays;

import entity.Task;
import entity.BeginAndDueDates.BeginAndDueDates;
import use_case.Angela.task.TaskGateway;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Interactor for the edit today's task use case.
 * Implements the business logic for editing a task in Today's Tasks.
 */
public class EditTodaysTaskInteractor implements EditTodaysTaskInputBoundary {
    private final TaskGateway taskGateway;
    private final EditTodaysTaskOutputBoundary outputBoundary;

    public EditTodaysTaskInteractor(TaskGateway taskGateway,
                                    EditTodaysTaskOutputBoundary outputBoundary) {
        this.taskGateway = taskGateway;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(EditTodaysTaskInputData inputData) {
        String taskId = inputData.getTaskId();

        // Check if task exists in Today's Tasks
        if (!taskGateway.existsInToday(taskId)) {
            outputBoundary.prepareFailView("Task not found in Today's Tasks");
            return;
        }

        // Validate due date if provided
        LocalDate dueDate = inputData.getDueDate();
        if (dueDate != null && dueDate.isBefore(LocalDate.now())) {
            outputBoundary.prepareFailView("Due date cannot be a date earlier than today");
            return;
        }

        // Get current task
        Task currentTask = null;
        for (Task task : taskGateway.getTodaysTasks()) {
            if (task.getInfo().getId().equals(taskId)) {
                currentTask = task;
                break;
            }
        }

        if (currentTask == null) {
            outputBoundary.prepareFailView("Task not found");
            return;
        }

        // Check if status is being changed
        boolean statusChanged = false;
        boolean wasComplete = currentTask.isComplete();
        Boolean newCompleteStatus = inputData.getIsComplete();

        if (newCompleteStatus != null && newCompleteStatus != wasComplete) {
            statusChanged = true;
            if (newCompleteStatus) {
                currentTask.completeTask(LocalDateTime.now());
            } else {
                // Create new task instance to "uncomplete" it
                BeginAndDueDates dates = currentTask.getBeginAndDueDates();
                dates.setDueDate(dueDate != null ? dueDate : dates.getDueDate());

                currentTask = new Task(
                        currentTask.getInfo(),
                        dates,
                        inputData.getPriority() != null ? inputData.getPriority() : currentTask.getTaskPriority()
                );
            }
        } else {
            // Update priority and due date if changed
            if (inputData.getPriority() != null || dueDate != null) {
                BeginAndDueDates dates = currentTask.getBeginAndDueDates();
                dates.setDueDate(dueDate != null ? dueDate : dates.getDueDate());

                currentTask = new Task(
                        currentTask.getInfo(),
                        dates,
                        inputData.getPriority() != null ? inputData.getPriority() : currentTask.getTaskPriority()
                );

                // Restore completion status if it was complete
                if (wasComplete) {
                    currentTask.completeTask(LocalDateTime.now());
                }
            }
        }

        // Update the task
        boolean updated = taskGateway.updateTodaysTask(currentTask);

        if (updated) {
            EditTodaysTaskOutputData outputData = new EditTodaysTaskOutputData(
                    taskId,
                    currentTask.getInfo().getName(),
                    statusChanged,
                    currentTask.isComplete()
            );
            outputBoundary.prepareSuccessView(outputData);
        } else {
            outputBoundary.prepareFailView("Failed to update task");
        }
    }
}