package interface_adapter.presenter;

import interface_adapter.view_model.CategoryViewModel;
import use_case.Angela.category.delete.DeleteCategoryOutputBoundary;
import use_case.Angela.category.delete.DeleteCategoryOutputData;

/**
 * Presenter for the delete category use case.
 */
public class DeleteCategoryPresenter implements DeleteCategoryOutputBoundary {
    private final CategoryViewModel categoryViewModel;

    public DeleteCategoryPresenter(CategoryViewModel categoryViewModel) {
        this.categoryViewModel = categoryViewModel;
    }

    @Override
    public void prepareSuccessView(DeleteCategoryOutputData outputData) {
        categoryViewModel.removeCategory(outputData.getDeletedCategoryId());
        categoryViewModel.setSuccessMessage(outputData.getMessage());
        categoryViewModel.clearError();
        categoryViewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String error) {
        categoryViewModel.setError(error);
        categoryViewModel.firePropertyChanged();
    }
}