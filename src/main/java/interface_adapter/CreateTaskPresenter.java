package interface_adapter;

import use_case.CreateTaskOutputBoundary;
import use_case.CreateTaskOutputData;
import view.TaskViewModel;
import view.TaskViewModelUpdateListener;

/**
 * Presenter for task creation.
 * Implements the output boundary and updates the view model.
 */
public class CreateTaskPresenter implements CreateTaskOutputBoundary {
    private final TaskViewModel taskViewModel;

    public CreateTaskPresenter(TaskViewModel taskViewModel) {
        this.taskViewModel = taskViewModel;
    }

    @Override
    public void presentSuccess(CreateTaskOutputData outputData) {
        // Update view model with success message
        taskViewModel.setMessage("Success: " + outputData.getMessage());
        taskViewModel.setLastCreatedTaskId(outputData.getTaskId());
        taskViewModel.refreshTaskLists();
    }

    @Override
    public void presentError(String error) {
        // Update view model with error message
        taskViewModel.setMessage("Error: " + error);
    }
}