package use_case.Category_related.create;

/**
 * Input boundary for the create category use case.
 */
public interface CreateCategoryInputBoundary {
    /**
     * Creates a new category with the given data.
     *
     * @param inputData The category creation data
     */
    void execute(CreateCategoryInputData inputData);
}