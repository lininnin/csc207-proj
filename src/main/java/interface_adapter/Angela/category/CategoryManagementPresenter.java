package interface_adapter.Angela.category;

import interface_adapter.Angela.category.CategoryManagementViewModel;
import interface_adapter.Angela.category.CategoryManagementState;
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

    public CategoryManagementPresenter(CategoryManagementViewModel viewModel) {
        this.viewModel = viewModel;
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