package app;

import data_access.NotificationTimeDataAccessObject;

import interface_adapter.Alex.Settings_related.notificationTime_module.NotificationTimeViewModel;
import interface_adapter.Alex.Settings_related.edit_notificationTime.EditNotificationTimeController;
import interface_adapter.Alex.Settings_related.edit_notificationTime.EditNotificationTimePresenter;
import interface_adapter.Alex.Settings_related.edit_notificationTime.EditNotificationTimeViewModel;

import use_case.Alex.Settings_related.edit_notificationTime.EditNotificationTimeInputBoundary;
import use_case.Alex.Settings_related.edit_notificationTime.EditNotificationTimeDataAccessInterf;
import use_case.Alex.Settings_related.edit_notificationTime.EditNotificationTimeInteractor;
import use_case.Alex.Settings_related.edit_notificationTime.EditNotificationTimeOutputBoundary;

import view.Alex.Settings.NotificationTimeView;
import view.CollapsibleSidebarView;

import javax.swing.*;
import java.awt.*;

public class SettingsPageBuilder {

    public JPanel build() {
        // -------------------- ViewModel --------------------
        NotificationTimeViewModel notificationTimeViewModel = new NotificationTimeViewModel();
        EditNotificationTimeViewModel editNotificationTimeViewModel = new EditNotificationTimeViewModel();

        // -------------------- DAO + Use Case --------------------
        EditNotificationTimeDataAccessInterf dao = new NotificationTimeDataAccessObject();
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

        // -------------------- Sidebar --------------------
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBackground(new Color(60, 63, 65));
        sidebarPanel.setPreferredSize(new Dimension(200, 700));
        sidebarPanel.add(new JButton("📋 Tasks"));
        sidebarPanel.add(new JButton("📆 Events"));
        sidebarPanel.add(new JButton("🎯 Goals"));
        sidebarPanel.add(new JButton("🧠 Wellness Log"));
        sidebarPanel.add(new JButton("📊 Charts"));
        sidebarPanel.add(new JButton("🤖 AI-Feedback & Analysis"));
        sidebarPanel.add(new JButton("⚙️ Settings"));

        // -------------------- Collapsible Layout --------------------
        CollapsibleSidebarView collapsibleCenter = new CollapsibleSidebarView(sidebarPanel, centerPanel);

        // -------------------- Right Panel (Details) --------------------
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setPreferredSize(new Dimension(500, 0));
        rightPanel.setBackground(Color.WHITE);

        // -------------------- Main Container --------------------
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(collapsibleCenter, BorderLayout.CENTER);
        mainPanel.add(rightPanel, BorderLayout.EAST);

        return mainPanel;
    }
}



