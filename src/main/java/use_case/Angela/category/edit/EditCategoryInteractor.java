package use_case.Angela.category.edit;

import entity.Category;
import use_case.Angela.category.CategoryGateway;

/**
 * Interactor for the edit category use case.
 * Implements the business logic for editing a category.
 */
public class EditCategoryInteractor implements EditCategoryInputBoundary {
    private final CategoryGateway categoryGateway;
    private final EditCategoryOutputBoundary outputBoundary;

    public EditCategoryInteractor(CategoryGateway categoryGateway,
                                  EditCategoryOutputBoundary outputBoundary) {
        this.categoryGateway = categoryGateway;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(EditCategoryInputData inputData) {
        String categoryId = inputData.getCategoryId();
        String newName = inputData.getNewCategoryName();

        // Validate input
        if (newName == null || newName.trim().isEmpty()) {
            outputBoundary.prepareFailView("Category name cannot be empty");
            return;
        }

        if (newName.length() > 20) {
            outputBoundary.prepareFailView("The name of category cannot exceed 20 letters");
            return;
        }

        // Check if category exists
        Category category = categoryGateway.getCategoryById(categoryId);
        if (category == null) {
            outputBoundary.prepareFailView("Category not found");
            return;
        }

        String oldName = category.getName();

        // Check if new name already exists (unless it's the same as current name)
        if (!oldName.equalsIgnoreCase(newName) && categoryGateway.categoryNameExists(newName)) {
            outputBoundary.prepareFailView("The category name already exists");
            return;
        }

        // Update category
        category.setName(newName);
        boolean updated = categoryGateway.updateCategory(category);

        if (updated) {
            EditCategoryOutputData outputData = new EditCategoryOutputData(
                    categoryId, oldName, newName
            );
            outputBoundary.prepareSuccessView(outputData);
        } else {
            outputBoundary.prepareFailView("Failed to update category");
        }
    }
}