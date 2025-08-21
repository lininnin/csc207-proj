package interface_adapter.Angela.task.overdue;

import interface_adapter.Angela.task.overdue.OverdueTasksViewModel;
import interface_adapter.Angela.task.overdue.OverdueTasksState;
import use_case.Angela.task.overdue.OverdueTasksOutputBoundary;
import use_case.Angela.task.overdue.OverdueTasksOutputData;

/**
 * Presenter for the overdue tasks feature.
 */
public class OverdueTasksPresenter implements OverdueTasksOutputBoundary {
    private final OverdueTasksViewModel viewModel;
    
    public OverdueTasksPresenter(OverdueTasksViewModel viewModel) {
        this.viewModel = viewModel;
    }
    
    @Override
    public void presentOverdueTasks(OverdueTasksOutputData outputData) {
        System.out.println("DEBUG: OverdueTasksPresenter.presentOverdueTasks() called with " + 
                          outputData.getOverdueTasks().size() + " tasks");
        OverdueTasksState state = viewModel.getState();
        state.setOverdueTasks(outputData.getOverdueTasks());
        state.setTotalOverdueTasks(outputData.getTotalOverdueTasks());
        state.setError(null);
        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }
    
    @Override
    public void prepareFailView(String error) {
        OverdueTasksState state = viewModel.getState();
        state.setError(error);
        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }
}