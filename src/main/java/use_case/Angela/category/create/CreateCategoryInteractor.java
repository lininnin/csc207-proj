package use_case.Angela.category.create;

import entity.Category;
import use_case.Angela.category.CategoryGateway;

/**
 * Interactor for the create category use case.
 * Implements the business logic for creating a new category.
 */
public class CreateCategoryInteractor implements CreateCategoryInputBoundary {
    private final CategoryGateway categoryGateway;
    private final CreateCategoryOutputBoundary outputBoundary;

    public CreateCategoryInteractor(CategoryGateway categoryGateway,
                                    CreateCategoryOutputBoundary outputBoundary) {
        this.categoryGateway = categoryGateway;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(CreateCategoryInputData inputData) {
        String categoryName = inputData.getCategoryName();

        // Validate input
        if (categoryName == null || categoryName.trim().isEmpty()) {
            outputBoundary.prepareFailView("Category name cannot be empty");
            return;
        }

        if (categoryName.length() > 20) {
            outputBoundary.prepareFailView("The name of category cannot exceed 20 letters");
            return;
        }

        // Check if category name already exists
        if (categoryGateway.categoryNameExists(categoryName)) {
            outputBoundary.prepareFailView("The category name already exists");
            return;
        }

        // Create and save new category
        String categoryId = categoryGateway.getNextCategoryId();
        Category category = new Category(categoryId, categoryName, null);

        try {
            categoryGateway.saveCategory(category);
            CreateCategoryOutputData outputData = new CreateCategoryOutputData(
                    categoryId, categoryName, false
            );
            outputBoundary.prepareSuccessView(outputData);
        } catch (Exception e) {
            outputBoundary.prepareFailView("Failed to create category: " + e.getMessage());
        }
    }
}