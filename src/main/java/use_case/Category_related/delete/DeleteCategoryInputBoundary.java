package use_case.Category_related.delete;

/**
 * Input boundary for the delete category use case.
 */
public interface DeleteCategoryInputBoundary {
    /**
     * Deletes a category with the given data.
     *
     * @param inputData The category deletion data
     */
    void execute(DeleteCategoryInputData inputData);
}