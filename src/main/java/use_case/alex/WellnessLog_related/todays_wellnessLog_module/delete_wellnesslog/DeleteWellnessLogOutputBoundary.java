package use_case.alex.WellnessLog_related.todays_wellnessLog_module.delete_wellnesslog;

/**
 * Output boundary for the DeleteWellnessLog use case.
 * Defines how the output data should be passed to the presenter.
 */
public interface DeleteWellnessLogOutputBoundary {

    /**
     * Prepares the view for a successful deletion.
     *
     * @param outputData the result of the successful deletion
     */
    void prepareSuccessView(DeleteWellnessLogOutputData outputData);

    /**
     * Prepares the view for a failed deletion.
     *
     * @param errorMessage the error message to present
     */
    void prepareFailView(String errorMessage);
}

