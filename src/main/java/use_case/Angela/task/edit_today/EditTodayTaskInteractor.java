package use_case.Angela.task.edit_today;

import entity.Angela.Task.TaskInterf;

/**
 * Interactor for the Edit Today Task use case.
 * Implements the business logic for editing priority and due date of today's tasks.
 */
public class EditTodayTaskInteractor implements EditTodayTaskInputBoundary {
    private final EditTodayTaskDataAccessInterface taskDataAccess;
    private final EditTodayTaskOutputBoundary outputBoundary;

    /**
     * Constructs the interactor with its dependencies.
     * 
     * @param taskDataAccess The data access interface for task operations
     * @param outputBoundary The output boundary for presenting results
     */
    public EditTodayTaskInteractor(EditTodayTaskDataAccessInterface taskDataAccess,
                                   EditTodayTaskOutputBoundary outputBoundary) {
        this.taskDataAccess = taskDataAccess;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(EditTodayTaskInputData inputData) {
        System.out.println("DEBUG: EditTodayTaskInteractor.execute - taskId: " + inputData.getTaskId());
        
        // Validate input
        if (inputData.getTaskId() == null || inputData.getTaskId().trim().isEmpty()) {
            outputBoundary.prepareFailView("Task ID is required");
            return;
        }

        // Get the task to edit
        TaskInterf task = taskDataAccess.getTodayTaskById(inputData.getTaskId());
        if (task == null) {
            outputBoundary.prepareFailView("Task not found in today's list");
            return;
        }

        // Validate due date if provided
        if (inputData.getDueDate() != null && !taskDataAccess.isValidDueDate(inputData.getDueDate())) {
            outputBoundary.prepareFailView("Due date cannot be before today");
            return;
        }

        try {
            // Update the task
            boolean success = taskDataAccess.updateTodayTaskPriorityAndDueDate(
                inputData.getTaskId(),
                inputData.getPriority(),
                inputData.getDueDate()
            );

            if (success) {
                // Get the task name for the success message
                String taskName = task.getInfo().getName();
                
                // Create success message
                String message = "Successfully updated task: " + taskName;
                
                // Create output data
                EditTodayTaskOutputData outputData = new EditTodayTaskOutputData(
                    inputData.getTaskId(),
                    taskName,
                    inputData.getPriority(),
                    inputData.getDueDate(),
                    message
                );
                
                outputBoundary.prepareSuccessView(outputData);
                
                System.out.println("DEBUG: Task updated successfully - " + message);
            } else {
                outputBoundary.prepareFailView("Failed to update task");
            }
        } catch (Exception e) {
            System.err.println("ERROR: EditTodayTaskInteractor - " + e.getMessage());
            outputBoundary.prepareFailView("Error updating task: " + e.getMessage());
        }
    }
}