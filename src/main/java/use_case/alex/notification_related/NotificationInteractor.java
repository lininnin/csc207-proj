package use_case.alex.notification_related;

import java.time.LocalTime;

/**
 * Interactor for the Notification reminder use case.
 * Checks whether current time matches any configured reminder time.
 */
public class NotificationInteractor implements NotificationInputBoundary {

    private final NotificationDataAccessObjectInterf dataAccess;
    private final NotificationOutputBoundary presenter;

    public NotificationInteractor(NotificationDataAccessObjectInterf dataAccess,
                                  NotificationOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(NotificationInputData inputData) {
        LocalTime now = inputData.getCurrentTime().withSecond(0).withNano(0);  // ignore seconds

        if (now.equals(dataAccess.getReminder1()) ||
                now.equals(dataAccess.getReminder2()) ||
                now.equals(dataAccess.getReminder3())) {

            NotificationOutputData outputData = new NotificationOutputData(
                    "🧠 It's time to fill your wellness log!"
            );
            presenter.prepareReminderView(outputData);
        }

        // 如果当前时间不等于提醒时间，不做任何事（也可以扩展为记录日志等）
    }
}

