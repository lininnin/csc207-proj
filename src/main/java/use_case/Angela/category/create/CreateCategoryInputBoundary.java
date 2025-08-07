package use_case.Angela.category.create;

/**
 * Input boundary for create category use case.
 */
public interface CreateCategoryInputBoundary {
    /**
     * Creates a new category with the given data.
     *
     * @param inputData The category creation data
     */
    void execute(CreateCategoryInputData inputData);
}