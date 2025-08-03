package interface_adapter.Category_related.controller;

import use_case.Category_related.delete.DeleteCategoryInputBoundary;
import use_case.Category_related.delete.DeleteCategoryInputData;

/**
 * Controller for deleting categories.
 */
public class DeleteCategoryController {
    private final DeleteCategoryInputBoundary deleteCategoryUseCase;

    public DeleteCategoryController(DeleteCategoryInputBoundary deleteCategoryUseCase) {
        this.deleteCategoryUseCase = deleteCategoryUseCase;
    }

    /**
     * Deletes a category by its ID.
     *
     * @param categoryId The ID of the category to delete
     */
    public void deleteCategory(String categoryId) {
        DeleteCategoryInputData inputData = new DeleteCategoryInputData(categoryId);
        deleteCategoryUseCase.execute(inputData);
    }
}