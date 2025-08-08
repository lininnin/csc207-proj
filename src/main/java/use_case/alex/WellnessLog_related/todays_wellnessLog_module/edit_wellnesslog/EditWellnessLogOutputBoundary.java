package use_case.alex.WellnessLog_related.todays_wellnessLog_module.edit_wellnesslog;

/**
 * Output boundary interface for the EditWellnessLog use case.
 * Implemented by the Presenter to convert output data to a format suitable for the ViewModel.
 */
public interface EditWellnessLogOutputBoundary {

    /**
     * Prepares the success view using the output data.
     *
     * @param outputData The output data from the interactor.
     */
    void prepareSuccessView(EditWellnessLogOutputData outputData);

    /**
     * Prepares the failure view with an error message.
     *
     * @param errorMessage The error message describing what went wrong.
     */
    void prepareFailView(String errorMessage);
}

