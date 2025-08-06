package interface_adapter.Angela.task.add_to_today;

import interface_adapter.Angela.task.today.TodayTasksViewModel;
import interface_adapter.Angela.task.today.TodayTasksState;
import interface_adapter.Angela.task.overdue.OverdueTasksController;
import use_case.Angela.task.add_to_today.AddTaskToTodayOutputBoundary;
import use_case.Angela.task.add_to_today.AddTaskToTodayOutputData;

/**
 * Presenter for the add to today use case.
 * Converts use case output to view model updates.
 */
public class AddTaskToTodayPresenter implements AddTaskToTodayOutputBoundary {
    private final AddTaskToTodayViewModel addTaskToTodayViewModel;
    private final TodayTasksViewModel todayTasksViewModel;
    private OverdueTasksController overdueTasksController;

    public AddTaskToTodayPresenter(AddTaskToTodayViewModel addTaskToTodayViewModel,
                                   TodayTasksViewModel todayTasksViewModel) {
        this.addTaskToTodayViewModel = addTaskToTodayViewModel;
        this.todayTasksViewModel = todayTasksViewModel;
    }

    @Override
    public void presentSuccess(AddTaskToTodayOutputData outputData) {
        // Update add to today view model with success message
        AddTaskToTodayState state = new AddTaskToTodayState();
        state.setSuccessMessage("Task '" + outputData.getTaskName() + "' added to Today's Tasks");
        state.setError(null);
        addTaskToTodayViewModel.setState(state);
        addTaskToTodayViewModel.firePropertyChanged();

        // Trigger refresh of today's tasks view
        TodayTasksState todayState = todayTasksViewModel.getState();
        if (todayState == null) {
            todayState = new TodayTasksState();
        }
        todayState.setRefreshNeeded(true);
        todayTasksViewModel.setState(todayState);
        todayTasksViewModel.firePropertyChanged();
        
        // Also refresh overdue tasks if controller is available
        if (overdueTasksController != null) {
            overdueTasksController.execute(7); // Refresh with 7 days
        }
    }

    @Override
    public void presentError(String error) {
        // Update add to today view model with error
        AddTaskToTodayState state = new AddTaskToTodayState();
        state.setError(error);
        state.setSuccessMessage(null);
        addTaskToTodayViewModel.setState(state);
        addTaskToTodayViewModel.firePropertyChanged();
    }
    
    public void setOverdueTasksController(OverdueTasksController controller) {
        this.overdueTasksController = controller;
    }
}