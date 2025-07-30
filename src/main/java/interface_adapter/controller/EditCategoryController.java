package interface_adapter.controller;

import use_case.Angela.category.edit.EditCategoryInputBoundary;
import use_case.Angela.category.edit.EditCategoryInputData;

/**
 * Controller for editing categories.
 */
public class EditCategoryController {
    private final EditCategoryInputBoundary editCategoryUseCase;

    public EditCategoryController(EditCategoryInputBoundary editCategoryUseCase) {
        this.editCategoryUseCase = editCategoryUseCase;
    }

    /**
     * Edits a category with a new name.
     *
     * @param categoryId The ID of the category to edit
     * @param newName The new name for the category
     */
    public void editCategory(String categoryId, String newName) {
        EditCategoryInputData inputData = new EditCategoryInputData(categoryId, newName);
        editCategoryUseCase.execute(inputData);
    }
}