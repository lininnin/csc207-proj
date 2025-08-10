package interface_adapter.Angela.category;

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
import use_case.Angela.category.create.*;
import use_case.Angela.category.delete.*;
import use_case.Angela.category.edit.*;

/**
 * Unified presenter for all category management operations.
 */
public class CategoryManagementPresenter implements
        CreateCategoryOutputBoundary,
        DeleteCategoryOutputBoundary,
        EditCategoryOutputBoundary {

    private final CategoryManagementViewModel viewModel;
    private AvailableTasksViewModel availableTasksViewModel;
    private TodayTasksViewModel todayTasksViewModel;
    private OverdueTasksController overdueTasksController;
    private TodaySoFarController todaySoFarController;
    private AvailableEventViewModel availableEventViewModel;
    private TodaysEventsViewModel todaysEventsViewModel;

    public CategoryManagementPresenter(CategoryManagementViewModel viewModel) {
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

    // Create Category
    @Override
    public void prepareSuccessView(CreateCategoryOutputData outputData) {
        CategoryManagementState state = viewModel.getState();
        state.setLastAction("CREATE_SUCCESS");
        state.setMessage("Category '" + outputData.getCategoryName() + "' created successfully");
        state.setRefreshNeeded(true);
        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }

    // Delete Category
    @Override
    public void prepareSuccessView(DeleteCategoryOutputData outputData) {
        CategoryManagementState state = viewModel.getState();
        state.setLastAction("DELETE_SUCCESS");
        state.setMessage(outputData.getMessage());
        state.setRefreshNeeded(true);
        viewModel.setState(state);
        viewModel.firePropertyChanged();
        
        // CRITICAL: Also trigger refresh of available tasks view since tasks were updated
        if (availableTasksViewModel != null) {
            AvailableTasksState tasksState = availableTasksViewModel.getState();
            tasksState.setRefreshNeeded(true);
            availableTasksViewModel.setState(tasksState);
            // Must use the correct property name that the view is listening for
            availableTasksViewModel.firePropertyChanged(AvailableTasksViewModel.AVAILABLE_TASKS_STATE_PROPERTY);
        }
        
        // CRITICAL: Also trigger refresh of today's tasks view since tasks were updated
        if (todayTasksViewModel != null) {
            TodayTasksState todayState = todayTasksViewModel.getState();
            if (todayState == null) {
                todayState = new TodayTasksState();
            }
            todayState.setRefreshNeeded(true);
            todayTasksViewModel.setState(todayState);
            todayTasksViewModel.firePropertyChanged();
            System.out.println("DEBUG: Triggered Today's Tasks refresh after category delete");
        }
        
        // Also refresh overdue tasks if controller is available
        if (overdueTasksController != null) {
            overdueTasksController.execute(7); // Refresh with 7 days
        }
        
        // CRITICAL: Refresh Today So Far panel when categories are deleted
        if (todaySoFarController != null) {
            todaySoFarController.refresh();
            System.out.println("DEBUG: Triggered Today So Far refresh after category delete");
        }
        
        // CRITICAL: Refresh event views when categories are deleted
        if (availableEventViewModel != null) {
            availableEventViewModel.firePropertyChanged("state");
        }
        
        if (todaysEventsViewModel != null) {
            todaysEventsViewModel.firePropertyChanged("state");
        }
    }

    // Edit Category
    @Override
    public void prepareSuccessView(EditCategoryOutputData outputData) {
        CategoryManagementState state = viewModel.getState();
        state.setLastAction("EDIT_SUCCESS");
        state.setMessage("Category renamed from '" + outputData.getOldName() +
                "' to '" + outputData.getNewName() + "'");
        state.setRefreshNeeded(true);
        viewModel.setState(state);
        viewModel.firePropertyChanged();
        
        // CRITICAL: Also trigger refresh of available tasks view since category names changed
        if (availableTasksViewModel != null) {
            AvailableTasksState tasksState = availableTasksViewModel.getState();
            tasksState.setRefreshNeeded(true);
            availableTasksViewModel.setState(tasksState);
            availableTasksViewModel.firePropertyChanged(AvailableTasksViewModel.AVAILABLE_TASKS_STATE_PROPERTY);
        }
        
        // CRITICAL: Also trigger refresh of today's tasks view since category names changed
        if (todayTasksViewModel != null) {
            TodayTasksState todayState = todayTasksViewModel.getState();
            if (todayState == null) {
                todayState = new TodayTasksState();
            }
            todayState.setRefreshNeeded(true);
            todayTasksViewModel.setState(todayState);
            todayTasksViewModel.firePropertyChanged();
            System.out.println("DEBUG: Triggered Today's Tasks refresh after category edit");
        }
        
        // Also refresh overdue tasks if controller is available  
        if (overdueTasksController != null) {
            overdueTasksController.execute(7); // Refresh with 7 days
        }
        
        // CRITICAL: Refresh Today So Far panel when categories are edited
        if (todaySoFarController != null) {
            todaySoFarController.refresh();
            System.out.println("DEBUG: Triggered Today So Far refresh after category edit");
        }
        
        // CRITICAL: Refresh event views when categories are edited
        if (availableEventViewModel != null) {
            availableEventViewModel.firePropertyChanged("state");
            System.out.println("DEBUG: Triggered Available Events refresh after category edit");
        }
        
        if (todaysEventsViewModel != null) {
            todaysEventsViewModel.firePropertyChanged("state");
            System.out.println("DEBUG: Triggered Today's Events refresh after category edit");
        }
    }

    @Override
    public void prepareFailView(String error) {
        CategoryManagementState state = viewModel.getState();
        state.setLastAction("ERROR");
        state.setError(error);
        state.setRefreshNeeded(false);
        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }
}