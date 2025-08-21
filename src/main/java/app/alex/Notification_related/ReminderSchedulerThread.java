package app.alex.Notification_related;

import interface_adapter.alex.Notification_related.NotificationController;

/**
 * A background thread that checks every minute whether a reminder should be triggered.
 * It delegates the reminder logic to NotificationController.
 */
public class ReminderSchedulerThread extends Thread {

    private final NotificationController controller;

    public ReminderSchedulerThread(final NotificationController controller) {
        this.controller = controller;
        this.setDaemon(true);  // So it doesn't prevent app shutdown
        this.setName("ReminderSchedulerThread");
    }

    @Override
    public void run() {
        while (true) {
            try {
                controller.execute();  // Trigger the notification use case
                Thread.sleep(60 * 1000);  // Wait 1 minute
            } catch (InterruptedException e) {
                System.err.println("ReminderSchedulerThread interrupted.");
                break;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
