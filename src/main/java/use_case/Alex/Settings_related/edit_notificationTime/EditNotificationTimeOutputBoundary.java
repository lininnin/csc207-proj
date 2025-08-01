package use_case.Alex.Settings_related.edit_notificationTime;

/**
 * Output boundary interface for the EditNotificationTime use case.
 * Defines methods to present success or failure outcomes to the UI layer.
 */
public interface EditNotificationTimeOutputBoundary {

    /**
     * Prepares the success view with the updated reminder times.
     *
     * @param outputData Output data containing updated times
     */
    void prepareSuccessView(EditNotificationTimeOutputData outputData);

    /**
     * Prepares the failure view with an appropriate error message.
     *
     * @param errorMessage Description of the failure
     */
    void prepareFailView(String errorMessage);
}

