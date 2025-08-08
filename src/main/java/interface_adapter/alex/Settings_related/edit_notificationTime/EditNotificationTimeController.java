package interface_adapter.alex.Settings_related.edit_notificationTime;

import use_case.alex.Settings_related.edit_notificationTime.EditNotificationTimeInputBoundary;
import use_case.alex.Settings_related.edit_notificationTime.EditNotificationTimeInputData;

/**
 * Controller for the EditNotificationTime use case.
 * Receives user input and invokes the interactor.
 */
public class EditNotificationTimeController {

    private final EditNotificationTimeInputBoundary interactor;

    /**
     * Constructs the controller with the given interactor.
     *
     * @param interactor The input boundary (use case interactor)
     */
    public EditNotificationTimeController(EditNotificationTimeInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Executes the use case with user-provided reminder time strings.
     *
     * @param reminder1 First reminder time (e.g., "08:00")
     * @param reminder2 Second reminder time (e.g., "12:00")
     * @param reminder3 Third reminder time (e.g., "20:00")
     */
    public void execute(String reminder1, String reminder2, String reminder3) {
        EditNotificationTimeInputData inputData =
                new EditNotificationTimeInputData(reminder1, reminder2, reminder3);
        interactor.execute(inputData);
    }
}

