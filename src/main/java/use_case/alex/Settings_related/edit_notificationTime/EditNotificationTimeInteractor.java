package use_case.alex.Settings_related.edit_notificationTime;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;

/**
 * Interactor for the EditNotificationTime use case.
 * Validates and updates daily notification reminder times.
 */
public class EditNotificationTimeInteractor implements EditNotificationTimeInputBoundary {

    private final EditNotificationTimeDataAccessInterf dataAccess;
    private final EditNotificationTimeOutputBoundary presenter;

    public EditNotificationTimeInteractor(EditNotificationTimeDataAccessInterf dataAccess,
                                          EditNotificationTimeOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(EditNotificationTimeInputData inputData) {
        String r1Str = inputData.getReminder1();
        String r2Str = inputData.getReminder2();
        String r3Str = inputData.getReminder3();

        try {
            // ✅ Step 1: parse strings to LocalTime
            LocalTime reminder1, reminder2, reminder3;
            try {
                reminder1 = LocalTime.parse(r1Str);
                reminder2 = LocalTime.parse(r2Str);
                reminder3 = LocalTime.parse(r3Str);

            } catch (DateTimeParseException e) {

                presenter.prepareFailView("Invalid time format. Please use HH:mm.");
                return;
            }

            // ✅ Step 2: update data access layer
            dataAccess.updateNotificationTimes(reminder1, reminder2, reminder3);

            // ✅ Step 3: prepare success output
            EditNotificationTimeOutputData outputData =
                    new EditNotificationTimeOutputData(r1Str, r2Str, r3Str, false);
            presenter.prepareSuccessView(outputData);

        } catch (Exception e) {
            e.printStackTrace();
            presenter.prepareFailView("Unexpected error occurred while updating notification times.");
        }
    }
}

