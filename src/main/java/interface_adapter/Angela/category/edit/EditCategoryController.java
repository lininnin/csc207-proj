package interface_adapter.Angela.category.edit;

import use_case.Angela.category.edit.EditCategoryInputBoundary;
import use_case.Angela.category.edit.EditCategoryInputData;

/**
 * Controller for edit category use case.
 */
public class EditCategoryController {
    private final EditCategoryInputBoundary editCategoryInteractor;

    public EditCategoryController(EditCategoryInputBoundary editCategoryInteractor) {
        this.editCategoryInteractor = editCategoryInteractor;
    }

    public void execute(String categoryId, String newName) {
        EditCategoryInputData inputData = new EditCategoryInputData(categoryId, newName);
        editCategoryInteractor.execute(inputData);
    }
}