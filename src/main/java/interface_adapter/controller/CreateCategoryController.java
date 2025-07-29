package interface_adapter.controller;

import use_case.Angela.category.create.CreateCategoryInputBoundary;
import use_case.Angela.category.create.CreateCategoryInputData;

/**
 * Controller for creating categories.
 * Handles user input from the category dialog.
 */
public class CreateCategoryController {
    private final CreateCategoryInputBoundary createCategoryUseCase;

    public CreateCategoryController(CreateCategoryInputBoundary createCategoryUseCase) {
        this.createCategoryUseCase = createCategoryUseCase;
    }

    /**
     * Creates a new category with the given name.
     *
     * @param categoryName The name for the new category
     */
    public void createCategory(String categoryName) {
        CreateCategoryInputData inputData = new CreateCategoryInputData(categoryName);
        createCategoryUseCase.execute(inputData);
    }
}