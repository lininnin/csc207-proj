package use_case.Angela.category.create;

import entity.Category;
import entity.CategoryFactory;

/**
 * Interactor for the create category use case.
 * Implements the business logic for creating a new category.
 */
public class CreateCategoryInteractor implements CreateCategoryInputBoundary {
    private final CreateCategoryDataAccessInterface categoryDataAccess;
    private final CreateCategoryOutputBoundary outputBoundary;
    private final CategoryFactory categoryFactory;

    public CreateCategoryInteractor(CreateCategoryDataAccessInterface categoryDataAccess,
                                    CreateCategoryOutputBoundary outputBoundary,
                                    CategoryFactory categoryFactory) {
        this.categoryDataAccess = categoryDataAccess;
        this.outputBoundary = outputBoundary;
        this.categoryFactory = categoryFactory;
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
        if (categoryDataAccess.existsByName(categoryName)) {
            outputBoundary.prepareFailView("The category name already exists");
            return;
        }

        // Generate unique ID based on category count
        String categoryId = "category" + (categoryDataAccess.getCategoryCount() + 1);
        
        // Create new category using factory
        Category category = categoryFactory.create(categoryId, categoryName);

        try {
            categoryDataAccess.save(category);
            CreateCategoryOutputData outputData = new CreateCategoryOutputData(
                    categoryId, categoryName, false
            );
            outputBoundary.prepareSuccessView(outputData);
        } catch (Exception e) {
            outputBoundary.prepareFailView("Failed to create category: " + e.getMessage());
        }
    }
}