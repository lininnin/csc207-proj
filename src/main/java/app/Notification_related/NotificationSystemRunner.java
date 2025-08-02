package app.Notification_related;

//import app.ReminderSchedulerThread;
import data_access.NotificationTimeDataAccessObject;
import interface_adapter.Alex.Notification_related.*;
import use_case.Alex.Notification_related.*;
import view.Alex.NotificationView;

/**
 * Initializes and runs the complete notification reminder system.
 * This includes setting up ViewModel, View, Presenter, Controller, Interactor, and Scheduler thread.
 */
public class NotificationSystemRunner {

    public void run() {
        // --- ViewModel ---
        NotificationViewModel viewModel = new NotificationViewModel();

        // --- View (registers listener) ---
        new NotificationView(viewModel);  // Auto-listens to ViewModel changes

        // --- Presenter ---
        NotificationOutputBoundary presenter = new NotificationPresenter(viewModel);

        // --- DAO (shared with Settings) ---
        NotificationDataAccessObjectInterf dao = new NotificationTimeDataAccessObject();

        // --- Interactor ---
        NotificationInputBoundary interactor = new NotificationInteractor(dao, presenter);

        // --- Controller ---
        NotificationController controller = new NotificationController(interactor);

        // --- Scheduler Thread ---
        ReminderSchedulerThread scheduler = new ReminderSchedulerThread(controller);
        scheduler.start();  // ⏰ 每分钟触发一次提醒检查
    }
}
