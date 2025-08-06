package use_case.Angela.category.edit;

/**
 * Output boundary for edit category use case.
 */
public interface EditCategoryOutputBoundary {
    /**
     * Prepares the success view with the edited category data.
     *
     * @param outputData The output data containing the edited category
     */
    void prepareSuccessView(EditCategoryOutputData outputData);

    /**
     * Prepares the fail view with an error message.
     *
     * @param error The error message
     */
    void prepareFailView(String error);
}