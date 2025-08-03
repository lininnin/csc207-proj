package use_case.Category_related.edit;

/**
 * Input boundary for the edit category use case.
 */
public interface EditCategoryInputBoundary {
    /**
     * Edits a category with the given data.
     *
     * @param inputData The category edit data
     */
    void execute(EditCategoryInputData inputData);
}