package interface_adapter.Angela.category.edit;

import interface_adapter.Angela.category.CategoryManagementViewModel;
import interface_adapter.Angela.category.CategoryManagementState;
import interface_adapter.Angela.task.available.AvailableTasksViewModel;
import interface_adapter.Angela.task.available.AvailableTasksState;
import interface_adapter.Angela.task.today.TodayTasksViewModel;
import interface_adapter.Angela.task.today.TodayTasksState;
import interface_adapter.Angela.task.overdue.OverdueTasksController;
import interface_adapter.Angela.today_so_far.TodaySoFarController;
import interface_adapter.alex.event_related.available_event_module.available_event.AvailableEventViewModel;
import interface_adapter.alex.event_related.todays_events_module.todays_events.TodaysEventsViewModel;
import use_case.Angela.category.edit.EditCategoryOutputBoundary;
import use_case.Angela.category.edit.EditCategoryOutputData;

/**
 * Presenter for the edit category use case.
 * Follows Single Responsibility Principle.
 */
public class EditCategoryPresenter implements EditCategoryOutputBoundary {
    private final CategoryManagementViewModel viewModel;
    private AvailableTasksViewModel availableTasksViewModel;
    private TodayTasksViewModel todayTasksViewModel;
    private OverdueTasksController overdueTasksController;
    private TodaySoFarController todaySoFarController;
    private AvailableEventViewModel availableEventViewModel;
    private TodaysEventsViewModel todaysEventsViewModel;

    public EditCategoryPresenter(CategoryManagementViewModel viewModel) {
        this.viewModel = viewModel;
    }
    
    public void setAvailableTasksViewModel(AvailableTasksViewModel availableTasksViewModel) {
        this.availableTasksViewModel = availableTasksViewModel;
    }
    
    public void setTodayTasksViewModel(TodayTasksViewModel todayTasksViewModel) {
        this.todayTasksViewModel = todayTasksViewModel;
    }
    
    public void setOverdueTasksController(OverdueTasksController controller) {
        this.overdueTasksController = controller;
    }
    
    public void setTodaySoFarController(TodaySoFarController controller) {
        this.todaySoFarController = controller;
    }
    
    public void setAvailableEventViewModel(AvailableEventViewModel availableEventViewModel) {
        this.availableEventViewModel = availableEventViewModel;
    }
    
    public void setTodaysEventsViewModel(TodaysEventsViewModel todaysEventsViewModel) {
        this.todaysEventsViewModel = todaysEventsViewModel;
    }

    @Override
    public void prepareSuccessView(EditCategoryOutputData outputData) {
        CategoryManagementState currentState = viewModel.getState();
        currentState.setLastAction("EDIT_SUCCESS");
        currentState.setMessage("Category '" + outputData.getNewName() + "' updated successfully");
        currentState.setRefreshNeeded(true);
        viewModel.setState(currentState);
        viewModel.firePropertyChanged();
        
        // Trigger refresh of related views
        if (availableTasksViewModel != null) {
            AvailableTasksState tasksState = availableTasksViewModel.getState();
            tasksState.setRefreshNeeded(true);
            availableTasksViewModel.setState(tasksState);
            availableTasksViewModel.firePropertyChanged(AvailableTasksViewModel.AVAILABLE_TASKS_STATE_PROPERTY);
        }
        if (todayTasksViewModel != null) {
            TodayTasksState todayState = todayTasksViewModel.getState();
            if (todayState == null) {
                todayState = new TodayTasksState();
            }
            todayState.setRefreshNeeded(true);
            todayTasksViewModel.setState(todayState);
            todayTasksViewModel.firePropertyChanged(TodayTasksViewModel.TODAY_TASKS_STATE_PROPERTY);
        }
        if (overdueTasksController != null) {
            overdueTasksController.execute(7);
        }
        if (todaySoFarController != null) {
            todaySoFarController.refresh();
        }
        if (availableEventViewModel != null) {
            availableEventViewModel.firePropertyChanged();
        }
        if (todaysEventsViewModel != null) {
            todaysEventsViewModel.firePropertyChanged();
        }
    }

    @Override
    public void prepareFailView(String error) {
        CategoryManagementState currentState = viewModel.getState();
        currentState.setLastAction("EDIT_FAIL");
        currentState.setMessage(error);
        currentState.setRefreshNeeded(false);
        viewModel.setState(currentState);
        viewModel.firePropertyChanged();
    }
}