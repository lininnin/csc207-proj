package interface_adapter.Angela.task.edit_today;

import interface_adapter.Angela.task.today.TodayTasksViewModel;
import interface_adapter.Angela.task.today.TodayTasksState;
import use_case.Angela.task.edit_today.EditTodayTaskOutputBoundary;
import use_case.Angela.task.edit_today.EditTodayTaskOutputData;

/**
 * Presenter for the edit today task use case.
 * Converts use case output to view model updates.
 */
public class EditTodayTaskPresenter implements EditTodayTaskOutputBoundary {
    private final EditTodayTaskViewModel editTodayTaskViewModel;
    private final TodayTasksViewModel todayTasksViewModel;

    /**
     * Constructs the presenter with its view models.
     * 
     * @param editTodayTaskViewModel The view model for edit today task state
     * @param todayTasksViewModel The view model for today's tasks list
     */
    public EditTodayTaskPresenter(EditTodayTaskViewModel editTodayTaskViewModel,
                                  TodayTasksViewModel todayTasksViewModel) {
        this.editTodayTaskViewModel = editTodayTaskViewModel;
        this.todayTasksViewModel = todayTasksViewModel;
    }

    @Override
    public void prepareSuccessView(EditTodayTaskOutputData outputData) {
        System.out.println("DEBUG: EditTodayTaskPresenter.prepareSuccessView - message: " + outputData.getMessage());
        
        // Create success state
        EditTodayTaskState editState = new EditTodayTaskState();
        editState.setSuccessMessage(outputData.getMessage());
        editState.setError(null);
        editState.setEditingTaskId(null); // Clear editing state
        editState.setLastEditedTaskId(outputData.getTaskId());
        editTodayTaskViewModel.setState(editState);
        editTodayTaskViewModel.firePropertyChanged();

        // Trigger refresh of today's tasks view
        TodayTasksState todayState = todayTasksViewModel.getState();
        if (todayState == null) {
            todayState = new TodayTasksState();
        }
        todayState.setRefreshNeeded(true);
        todayTasksViewModel.setState(todayState);
        todayTasksViewModel.firePropertyChanged();
        
        System.out.println("DEBUG: Triggered Today's Tasks refresh after edit");
    }

    @Override
    public void prepareFailView(String error) {
        System.out.println("DEBUG: EditTodayTaskPresenter.prepareFailView - error: " + error);
        
        // Create error state
        EditTodayTaskState state = new EditTodayTaskState();
        state.setError(error);
        state.setSuccessMessage(null);
        editTodayTaskViewModel.setState(state);
        editTodayTaskViewModel.firePropertyChanged();
        
        // Don't trigger refresh on error - keep edit dialog open
    }
}