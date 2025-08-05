package interface_adapter.Angela.category;

import interface_adapter.Angela.category.CategoryManagementViewModel;
import interface_adapter.Angela.category.CategoryManagementState;
import interface_adapter.Angela.task.available.AvailableTasksViewModel;
import interface_adapter.Angela.task.available.AvailableTasksState;
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

    public CategoryManagementPresenter(CategoryManagementViewModel viewModel) {
        this.viewModel = viewModel;
    }
    
    public void setAvailableTasksViewModel(AvailableTasksViewModel availableTasksViewModel) {
        this.availableTasksViewModel = availableTasksViewModel;
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