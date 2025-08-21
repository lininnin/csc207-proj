package interface_adapter.Angela.category.create;

import interface_adapter.Angela.category.CategoryManagementViewModel;
import interface_adapter.Angela.category.CategoryManagementState;
import use_case.Angela.category.create.CreateCategoryOutputBoundary;
import use_case.Angela.category.create.CreateCategoryOutputData;

/**
 * Presenter for the create category use case.
 * Follows Single Responsibility Principle.
 */
public class CreateCategoryPresenter implements CreateCategoryOutputBoundary {
    private final CategoryManagementViewModel viewModel;

    public CreateCategoryPresenter(CategoryManagementViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(CreateCategoryOutputData outputData) {
        CategoryManagementState currentState = viewModel.getState();
        currentState.setLastAction("CREATE_SUCCESS");
        currentState.setMessage("Category '" + outputData.getCategoryName() + "' created successfully");
        currentState.setRefreshNeeded(true);
        viewModel.setState(currentState);
        viewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String error) {
        CategoryManagementState currentState = viewModel.getState();
        currentState.setLastAction("CREATE_FAIL");
        currentState.setMessage(error);
        currentState.setRefreshNeeded(false);
        viewModel.setState(currentState);
        viewModel.firePropertyChanged();
    }
}