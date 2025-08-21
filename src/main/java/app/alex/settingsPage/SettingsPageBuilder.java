package app.alex.settingsPage;

import data_access.alex.NotificationTimeDataAccessObject;

import entity.alex.NotificationTime.NotificationTimeFactory;
import entity.alex.NotificationTime.NotificationTimeFactoryInterf;
import interface_adapter.alex.Settings_related.notificationTime_module.NotificationTimeViewModel;
import interface_adapter.alex.Settings_related.edit_notificationTime.EditNotificationTimeController;
import interface_adapter.alex.Settings_related.edit_notificationTime.EditNotificationTimePresenter;
import interface_adapter.alex.Settings_related.edit_notificationTime.EditNotificationTimeViewModel;

import use_case.alex.Settings_related.edit_notificationTime.EditNotificationTimeInputBoundary;
import use_case.alex.Settings_related.edit_notificationTime.EditNotificationTimeDataAccessInterf;
import use_case.alex.Settings_related.edit_notificationTime.EditNotificationTimeInteractor;
import use_case.alex.Settings_related.edit_notificationTime.EditNotificationTimeOutputBoundary;

import view.alex.Settings.NotificationTimeView;

import javax.swing.*;
import java.awt.*;

public class SettingsPageBuilder {

    public JPanel build() {
        // -------------------- ViewModel --------------------
        NotificationTimeViewModel notificationTimeViewModel = new NotificationTimeViewModel();
        EditNotificationTimeViewModel editNotificationTimeViewModel = new EditNotificationTimeViewModel();

        // -------------------- DAO + Use Case --------------------
        NotificationTimeFactoryInterf notificationTimeFactory = new NotificationTimeFactory();
        EditNotificationTimeDataAccessInterf dao = new NotificationTimeDataAccessObject(notificationTimeFactory);
        EditNotificationTimeOutputBoundary presenter = new EditNotificationTimePresenter(editNotificationTimeViewModel);
        EditNotificationTimeInputBoundary interactor = new EditNotificationTimeInteractor(dao, presenter);
        EditNotificationTimeController controller = new EditNotificationTimeController(interactor);

        // -------------------- Center View --------------------
        NotificationTimeView notificationTimeView = new NotificationTimeView(
                notificationTimeViewModel,
                editNotificationTimeViewModel,
                controller
        );

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createTitledBorder("Notification Time Settings"));
        centerPanel.add(notificationTimeView, BorderLayout.CENTER);

        // -------------------- Right Panel (Details) --------------------
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setPreferredSize(new Dimension(500, 0));
        rightPanel.setBackground(Color.WHITE);

        // -------------------- Main Container --------------------
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(rightPanel, BorderLayout.EAST);

        return mainPanel;
    }
}



