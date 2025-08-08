package app.Notification_related;

import data_access.NotificationTimeDataAccessObject;
import entity.Alex.NotificationTime.NotificationTimeFactory;
import entity.Alex.NotificationTime.NotificationTimeFactoryInterf;
import interface_adapter.alex.Notification_related.*;
import use_case.alex.Notification_related.*;
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
        NotificationTimeFactoryInterf factory = new NotificationTimeFactory();
        NotificationDataAccessObjectInterf dao = new NotificationTimeDataAccessObject(factory);

        // --- Interactor ---
        NotificationInputBoundary interactor = new NotificationInteractor(dao, presenter);

        // --- Controller ---
        NotificationController controller = new NotificationController(interactor);

        // --- Scheduler Thread ---
        ReminderSchedulerThread scheduler = new ReminderSchedulerThread(controller);
        scheduler.start();  // ⏰ 每分钟触发一次提醒检查
    }
}
