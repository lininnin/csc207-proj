package app;

import interface_adapter.Alex.available_event.AvailableEventViewModel;
import interface_adapter.Alex.create_event.*;
import interface_adapter.Alex.delete_event.*;
import use_case.Alex.create_event.*;
import use_case.Alex.delete_event.*;
import data_access.EventAvailableDataAccessObject;
import entity.Info.InfoFactory;
import view.Alex.CreateEventView;
import view.Alex.AvailableEventView;
import view.CollapsibleSidebarView;

import javax.swing.*;
import java.awt.*;

class CreateEventTestApp {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("MindTrack");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 700);

            // --- ViewModels ---
            CreatedEventViewModel createdEventViewModel = new CreatedEventViewModel();
            AvailableEventViewModel availableEventViewModel = new AvailableEventViewModel();
            DeletedEventViewModel deletedEventViewModel = new DeletedEventViewModel();

            // --- Data Access & Factory ---
            EventAvailableDataAccessObject commonDao = new EventAvailableDataAccessObject();
            CreateEventDataAccessInterface createAccess = commonDao;
            DeleteEventDataAccessInterf deleteAccess = commonDao;
            InfoFactory infoFactory = new InfoFactory();

            // --- Create Event Use Case ---
            CreateEventOutputBoundary createEventPresenter = new CreateEventPresenter(
                    createdEventViewModel, availableEventViewModel, createAccess);
            CreateEventInputBoundary createEventInteractor = new CreateEventInteractor(createAccess, createEventPresenter, infoFactory);
            CreateEventController createEventController = new CreateEventController(createEventInteractor);

            // --- Delete Event Use Case ---
            DeleteEventOutputBoundary deleteEventPresenter = new DeleteEventPresenter(
                    deletedEventViewModel, availableEventViewModel);
            DeleteEventInputBoundary deleteEventInteractor = new DeleteEventInteractor(deleteAccess, deleteEventPresenter);
            DeleteEventController deleteEventController = new DeleteEventController(deleteEventInteractor);

            // --- CreateEventView ---
            CreateEventView createEventView = new CreateEventView(createdEventViewModel);
            createEventView.setCreateEventController(createEventController);

            // --- AvailableEventView ---
            AvailableEventView availableEventView = new AvailableEventView(
                    availableEventViewModel, deleteEventController, deletedEventViewModel, createdEventViewModel);

            // --- Upper Panel Layout (left top + left bottom) ---
            JSplitPane verticalSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, createEventView, new JPanel());
            verticalSplit.setResizeWeight(0.5);
            verticalSplit.setDividerSize(2);
            verticalSplit.setEnabled(false);

            // --- Top Center Row ---
            JPanel upperRightPanel = new JPanel();
            upperRightPanel.setBackground(new Color(240, 240, 255));
            JPanel topCenterRow = new JPanel(new GridLayout(1, 2));
            topCenterRow.add(verticalSplit);
            topCenterRow.add(upperRightPanel);

            // --- Bottom Content Area ---
            JPanel bottomBox = new JPanel(new BorderLayout());
            bottomBox.add(availableEventView, BorderLayout.NORTH);
            bottomBox.setPreferredSize(new Dimension(800, 350));
            bottomBox.setBackground(Color.GRAY);

            JPanel centerPanel = new JPanel(new BorderLayout());
            centerPanel.add(topCenterRow, BorderLayout.CENTER);
            centerPanel.add(bottomBox, BorderLayout.SOUTH);

            // --- Sidebar Panel (buttons only) ---
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

            // --- Wrap sidebar + centerPanel into a collapsible container ---
            CollapsibleSidebarView collapsibleCenter = new CollapsibleSidebarView(sidebarPanel, centerPanel);

            // --- Right Panel (Details) ---
            JPanel rightPanel = new JPanel();
            rightPanel.setPreferredSize(new Dimension(300, 0));
            rightPanel.setBackground(Color.WHITE);

            // --- Final Frame Layout ---
            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.add(collapsibleCenter, BorderLayout.CENTER);
            mainPanel.add(rightPanel, BorderLayout.EAST);

            frame.setContentPane(mainPanel);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}


