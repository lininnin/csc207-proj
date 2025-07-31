package interface_adapter.Angela.category.delete;

import use_case.Angela.category.delete.DeleteCategoryInputBoundary;
import use_case.Angela.category.delete.DeleteCategoryInputData;

/**
 * Controller for delete category use case.
 */
public class DeleteCategoryController {
    private final DeleteCategoryInputBoundary deleteCategoryInteractor;

    public DeleteCategoryController(DeleteCategoryInputBoundary deleteCategoryInteractor) {
        this.deleteCategoryInteractor = deleteCategoryInteractor;
    }

    public void execute(String categoryId) {
        DeleteCategoryInputData inputData = new DeleteCategoryInputData(categoryId);
        deleteCategoryInteractor.execute(inputData);
    }
}