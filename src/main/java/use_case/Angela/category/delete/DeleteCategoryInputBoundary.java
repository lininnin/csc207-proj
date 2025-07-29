package use_case.Angela.category.delete;

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
