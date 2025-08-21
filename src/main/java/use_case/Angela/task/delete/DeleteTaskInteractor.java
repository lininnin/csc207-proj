package use_case.Angela.task.delete;

import entity.Angela.Task.TaskAvailableInterf;
import entity.Angela.Task.TaskInterf;
import java.util.List;

/**
 * Interactor for the delete task use case.
 * Implements the business logic for deleting a task.
 */
public class DeleteTaskInteractor implements DeleteTaskInputBoundary {
    private final DeleteTaskDataAccessInterface dataAccess;
    private final DeleteTaskOutputBoundary outputBoundary;

    public DeleteTaskInteractor(DeleteTaskDataAccessInterface dataAccess,
                                DeleteTaskOutputBoundary outputBoundary) {
        this.dataAccess = dataAccess;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(DeleteTaskInputData inputData) {
        String taskId = inputData.getTaskId();
        
        // DEBUG: Log the task ID we're trying to delete
        System.out.println("DEBUG: DeleteTaskInteractor - Attempting to delete task with ID: " + taskId);

        // Get the TaskAvailable (template) to check if it exists
        TaskAvailableInterf taskTemplate = dataAccess.getTaskAvailableById(taskId);
        
        // DEBUG: Log whether we found the task
        System.out.println("DEBUG: DeleteTaskInteractor - Task template found: " + (taskTemplate != null));
        
        if (taskTemplate == null) {
            // DEBUG: Log all available tasks
            System.out.println("DEBUG: DeleteTaskInteractor - Available tasks count: " + 
                dataAccess.getAllAvailableTaskTemplates().size());
            for (TaskAvailableInterf task : dataAccess.getAllAvailableTaskTemplates()) {
                System.out.println("DEBUG: Available task ID: " + task.getId() + ", Name: " + task.getInfo().getName());
            }
            
            outputBoundary.prepareFailView("Task not found in Available Tasks");
            return;
        }

        // Check if this task is a target of any goals
        List<String> goalNames = dataAccess.getGoalNamesTargetingTask(taskId);
        if (!goalNames.isEmpty()) {
            String goalsList = String.join(", ", goalNames);
            String warningMessage = String.format(
                "Warning: This task '%s' is linked to goal(s): %s. " +
                "If deleted, the goal(s) will also be deleted. Are you sure?",
                taskTemplate.getInfo().getName(), goalsList
            );
            outputBoundary.prepareFailView(warningMessage);
            return;
        }

        // Check if this template has instances in Today's Tasks
        boolean existsInToday = dataAccess.templateExistsInToday(taskId);

        if (existsInToday && inputData.isFromAvailable()) {
            // Show warning dialog - task exists in both lists
            String taskName = taskTemplate.getInfo().getName();
            outputBoundary.showDeleteFromBothWarning(taskId, taskName);
            return;
        }

        // Delete the task completely
        boolean deleted = dataAccess.deleteTaskCompletely(taskId);
        
        // DEBUG: Log deletion result
        System.out.println("DEBUG: DeleteTaskInteractor - Deletion result: " + deleted);

        if (deleted) {
            String message = existsInToday ?
                    "Task deleted from both Available and Today's lists" :
                    "Task deleted successfully";

            DeleteTaskOutputData outputData = new DeleteTaskOutputData(
                    taskId, message, existsInToday
            );
            outputBoundary.prepareSuccessView(outputData);
        } else {
            outputBoundary.prepareFailView("Failed to delete task");
        }
    }
}