package interface_adapter.Angela.task.create;

import interface_adapter.Angela.task.available.AvailableTasksViewModel;
import interface_adapter.Angela.task.available.AvailableTasksState;
import interface_adapter.Angela.task.add_to_today.AddTaskToTodayViewModel;
import interface_adapter.Angela.task.add_to_today.AddTaskToTodayState;
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
    private AddTaskToTodayViewModel addTaskToTodayViewModel;

    public CreateTaskPresenter(CreateTaskViewModel createTaskViewModel,
                               AvailableTasksViewModel availableTasksViewModel,
                               ViewManagerModel viewManagerModel) {
        this.createTaskViewModel = createTaskViewModel;
        this.availableTasksViewModel = availableTasksViewModel;
        this.viewManagerModel = viewManagerModel;
    }
    
    public void setAddTaskToTodayViewModel(AddTaskToTodayViewModel addTaskToTodayViewModel) {
        this.addTaskToTodayViewModel = addTaskToTodayViewModel;
    }

    @Override
    public void presentSuccess(CreateTaskOutputData outputData) {

        // Update create task view model to show success and clear form
        CreateTaskState createState = createTaskViewModel.getState();
        createState.setTaskName("");
        createState.setDescription("");
        createState.setCategoryId("");
        createState.setOneTime(false);
        createState.setError(null);
        createState.setSuccessMessage(outputData.getMessage());
        createTaskViewModel.setState(createState);
        createTaskViewModel.firePropertyChanged(CreateTaskViewModel.CREATE_TASK_STATE_PROPERTY);

        // Update available tasks view model to trigger refresh
        AvailableTasksState availableState = availableTasksViewModel.getState();
        availableState.setRefreshNeeded(true);
        availableTasksViewModel.setState(availableState);
        availableTasksViewModel.firePropertyChanged(AvailableTasksViewModel.AVAILABLE_TASKS_STATE_PROPERTY);

        // Also notify AddTaskToToday view to refresh its dropdown
        if (addTaskToTodayViewModel != null) {
            AddTaskToTodayState addToTodayState = addTaskToTodayViewModel.getState();
            if (addToTodayState == null) {
                addToTodayState = new AddTaskToTodayState();
            }
            addToTodayState.setRefreshNeeded(true);
            addTaskToTodayViewModel.setState(addToTodayState);
            addTaskToTodayViewModel.firePropertyChanged();
        }

        // Note: Removed viewManagerModel.setActiveView() as we want to stay on the same page
    }

    @Override
    public void presentError(String error) {
        CreateTaskState state = createTaskViewModel.getState();
        state.setError(error);
        state.setSuccessMessage(null);
        createTaskViewModel.setState(state);
        createTaskViewModel.firePropertyChanged(CreateTaskViewModel.CREATE_TASK_STATE_PROPERTY);
    }
}