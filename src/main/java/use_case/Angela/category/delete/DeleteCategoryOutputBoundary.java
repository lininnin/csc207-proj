package use_case.Angela.category.delete;

/**
 * Output boundary for delete category use case.
 */
public interface DeleteCategoryOutputBoundary {
    /**
     * Prepares the success view after category deletion.
     *
     * @param outputData The output data
     */
    void prepareSuccessView(DeleteCategoryOutputData outputData);

    /**
     * Prepares the fail view with an error message.
     *
     * @param error The error message
     */
    void prepareFailView(String error);
}