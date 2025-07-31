package interface_adapter.Angela.task.create;

import interface_adapter.Angela.task.available.AvailableTasksViewModel;
import interface_adapter.Angela.task.available.AvailableTasksState;
import interface_adapter.ViewManagerModel;
import use_case.Angela.task.create.CreateTaskOutputBoundary;
import use_case.Angela.task.create.CreateTaskOutputData;

/**
 * Presenter for the create task use case.
 * Converts output data into view model updates.
 */
public class CreateTaskPresenter implements CreateTaskOutputBoundary {
    private final CreateTaskViewModel createTaskViewModel;
    private final AvailableTasksViewModel availableTasksViewModel;
    private final ViewManagerModel viewManagerModel;

    public CreateTaskPresenter(CreateTaskViewModel createTaskViewModel,
                               AvailableTasksViewModel availableTasksViewModel,
                               ViewManagerModel viewManagerModel) {
        this.createTaskViewModel = createTaskViewModel;
        this.availableTasksViewModel = availableTasksViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void presentSuccess(CreateTaskOutputData outputData) {
        // Update create task view model to clear form
        CreateTaskState createState = createTaskViewModel.getState();
        createState.setTaskName("");
        createState.setDescription("");
        createState.setCategoryId("");
        createState.setOneTime(false);
        createState.setError(null);
        createState.setSuccessMessage(outputData.getMessage());
        createTaskViewModel.setState(createState);
        createTaskViewModel.firePropertyChanged();

        // Update available tasks view model to trigger refresh
        AvailableTasksState availableState = availableTasksViewModel.getState();
        availableState.setRefreshNeeded(true);
        availableTasksViewModel.setState(availableState);
        availableTasksViewModel.firePropertyChanged();

        // Switch back to available tasks view
        viewManagerModel.setActiveView(availableTasksViewModel.getViewName());
        viewManagerModel.firePropertyChanged();
    }

    @Override
    public void presentError(String error) {
        CreateTaskState state = createTaskViewModel.getState();
        state.setError(error);
        state.setSuccessMessage(null);
        createTaskViewModel.setState(state);
        createTaskViewModel.firePropertyChanged();
    }
}