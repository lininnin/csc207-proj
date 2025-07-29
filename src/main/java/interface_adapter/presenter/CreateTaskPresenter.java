package interface_adapter.presenter;

import use_case.Angela.task.create_task.CreateTaskOutputBoundary;
import use_case.Angela.task.create_task.CreateTaskOutputData;
import view.Task.TaskViewModel;

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
    }

    @Override
    public void presentError(String error) {
        // Update view model with error message
        taskViewModel.setMessage("Error: " + error);
    }
}