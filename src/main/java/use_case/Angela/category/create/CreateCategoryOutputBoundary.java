package use_case.Angela.category.create;

/**
 * Output boundary for the create category use case.
 */
public interface CreateCategoryOutputBoundary {
    /**
     * Prepares the success view with the created category data.
     *
     * @param outputData The output data containing the created category
     */
    void prepareSuccessView(CreateCategoryOutputData outputData);

    /**
     * Prepares the fail view with an error message.
     *
     * @param error The error message
     */
    void prepareFailView(String error);
}