package use_case.Angela.category.delete;

import entity.Category;
import use_case.Angela.category.CategoryGateway;

/**
 * Interactor for the delete category use case.
 * Implements the business logic for deleting a category.
 */
public class DeleteCategoryInteractor implements DeleteCategoryInputBoundary {
    private final CategoryGateway categoryGateway;
    private final DeleteCategoryOutputBoundary outputBoundary;

    public DeleteCategoryInteractor(CategoryGateway categoryGateway,
                                    DeleteCategoryOutputBoundary outputBoundary) {
        this.categoryGateway = categoryGateway;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(DeleteCategoryInputData inputData) {
        String categoryId = inputData.getCategoryId();

        // Check if category exists
        Category category = categoryGateway.getCategoryById(categoryId);
        if (category == null) {
            outputBoundary.prepareFailView("Category not found");
            return;
        }

        // Delete the category
        // Note: The gateway implementation should handle clearing category from all entities
        boolean deleted = categoryGateway.deleteCategory(categoryId);

        if (deleted) {
            DeleteCategoryOutputData outputData = new DeleteCategoryOutputData(
                    categoryId,
                    "Category deleted successfully. All tasks, events, and goals with this category now have empty category."
            );
            outputBoundary.prepareSuccessView(outputData);
        } else {
            outputBoundary.prepareFailView("Failed to delete category");
        }
    }
}