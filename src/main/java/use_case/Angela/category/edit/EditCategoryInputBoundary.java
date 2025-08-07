package use_case.Angela.category.edit;

/**
 * Input boundary for edit category use case.
 */
public interface EditCategoryInputBoundary {
    /**
     * Edits a category with the given data.
     *
     * @param inputData The category edit data
     */
    void execute(EditCategoryInputData inputData);
}