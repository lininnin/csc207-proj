package interface_adapter.Angela.category.create;

import use_case.Angela.category.create.CreateCategoryInputBoundary;
import use_case.Angela.category.create.CreateCategoryInputData;

/**
 * Controller for create category use case.
 */
public class CreateCategoryController {
    private final CreateCategoryInputBoundary createCategoryInteractor;

    public CreateCategoryController(CreateCategoryInputBoundary createCategoryInteractor) {
        this.createCategoryInteractor = createCategoryInteractor;
    }

    public void execute(String categoryName) {
        CreateCategoryInputData inputData = new CreateCategoryInputData(categoryName);
        createCategoryInteractor.execute(inputData);
    }
}