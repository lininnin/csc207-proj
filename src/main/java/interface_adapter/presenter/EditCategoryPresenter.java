package interface_adapter.presenter;

import interface_adapter.view_model.CategoryViewModel;
import use_case.Angela.category.edit.EditCategoryOutputBoundary;
import use_case.Angela.category.edit.EditCategoryOutputData;

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