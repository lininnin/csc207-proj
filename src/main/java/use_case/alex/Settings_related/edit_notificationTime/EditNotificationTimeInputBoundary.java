package use_case.alex.Settings_related.edit_notificationTime;

/**
 * Input boundary interface for the EditNotificationTime use case.
 * Defines the method to execute the update operation.
 */
public interface EditNotificationTimeInputBoundary {

    /**
     * Executes the use case with the provided input data.
     *
     * @param inputData The input data containing the updated reminder times
     */
    void execute(EditNotificationTimeInputData inputData);
}

