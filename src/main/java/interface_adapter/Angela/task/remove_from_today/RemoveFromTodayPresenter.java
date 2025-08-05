package interface_adapter.Angela.task.remove_from_today;

import interface_adapter.Angela.task.today.TodayTasksState;
import interface_adapter.Angela.task.today.TodayTasksViewModel;
import use_case.Angela.task.remove_from_today.RemoveFromTodayOutputBoundary;
import use_case.Angela.task.remove_from_today.RemoveFromTodayOutputData;

/**
 * Presenter for the remove from today use case.
 */
public class RemoveFromTodayPresenter implements RemoveFromTodayOutputBoundary {
    private final TodayTasksViewModel todayTasksViewModel;

    public RemoveFromTodayPresenter(TodayTasksViewModel todayTasksViewModel) {
        this.todayTasksViewModel = todayTasksViewModel;
    }

    @Override
    public void prepareSuccessView(RemoveFromTodayOutputData outputData) {
        TodayTasksState state = todayTasksViewModel.getState();
        state.setSuccessMessage(outputData.getMessage());
        state.setRefreshNeeded(true);
        todayTasksViewModel.setState(state);
        todayTasksViewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String error) {
        TodayTasksState state = todayTasksViewModel.getState();
        state.setError(error);
        todayTasksViewModel.setState(state);
        todayTasksViewModel.firePropertyChanged();
    }
}