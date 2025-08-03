package interface_adapter.Category_related.presenter;

import interface_adapter.Category_related.CategoryViewModel;
import use_case.Category_related.edit.EditCategoryOutputBoundary;
import use_case.Category_related.edit.EditCategoryOutputData;

/**
 * Presenter for the edit category use case.
 */
public class EditCategoryPresenter implements EditCategoryOutputBoundary {
    private final CategoryViewModel categoryViewModel;

    public EditCategoryPresenter(CategoryViewModel categoryViewModel) {
        this.categoryViewModel = categoryViewModel;
    }

    @Override
    public void prepareSuccessView(EditCategoryOutputData outputData) {
        categoryViewModel.updateCategoryName(outputData.getCategoryId(), outputData.getNewName());
        categoryViewModel.setSuccessMessage("Category renamed from '" +
                outputData.getOldName() + "' to '" + outputData.getNewName() + "'");
        categoryViewModel.clearError();
        categoryViewModel.clearEditingState();
        categoryViewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String error) {
        categoryViewModel.setError(error);
        categoryViewModel.firePropertyChanged();
    }
}