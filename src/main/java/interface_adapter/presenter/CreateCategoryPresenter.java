package interface_adapter.presenter;

import interface_adapter.ViewManagerModel;
import interface_adapter.view_model.CategoryViewModel;
import use_case.Angela.category.create.CreateCategoryOutputBoundary;
import use_case.Angela.category.create.CreateCategoryOutputData;

/**
 * Presenter for the create category use case.
 * Updates the view model based on the use case output.
 */
public class CreateCategoryPresenter implements CreateCategoryOutputBoundary {
    private final CategoryViewModel categoryViewModel;
    private final ViewManagerModel viewManagerModel;

    public CreateCategoryPresenter(CategoryViewModel categoryViewModel,
                                   ViewManagerModel viewManagerModel) {
        this.categoryViewModel = categoryViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void prepareSuccessView(CreateCategoryOutputData outputData) {
        // Update the category view model
        categoryViewModel.addCategory(outputData.getCategoryId(),
                outputData.getCategoryName());
        categoryViewModel.setSuccessMessage("Category created successfully");
        categoryViewModel.clearError();
        categoryViewModel.firePropertyChanged();

        // Close the category dialog
        viewManagerModel.setActiveView("task");
        viewManagerModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String error) {
        categoryViewModel.setError(error);
        categoryViewModel.firePropertyChanged();
    }
}