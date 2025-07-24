package app;

import interface_adapter.Alex.add_event.AddEventController;
import interface_adapter.Alex.add_event.AddEventPresenter;
import interface_adapter.Alex.add_event.AddedEventState;
import interface_adapter.Alex.add_event.AddedEventViewModel;
import interface_adapter.Alex.add_event.AddedEventState;
import interface_adapter.Alex.available_event.AvailableEventViewModel;
import interface_adapter.Alex.create_event.*;
import interface_adapter.Alex.delete_event.*;
import interface_adapter.Alex.edit_event.*;

import use_case.Alex.add_event.*;
import use_case.Alex.create_event.*;
import use_case.Alex.delete_event.*;
import use_case.Alex.edit_event.*;

import data_access.EventAvailableDataAccessObject;
import data_access.TodaysEventDataAccessObject;

import entity.Info.InfoFactory;
import view.Alex.AddEventView;
import view.Alex.CreateEventView;
import view.Alex.AvailableEventView;
import view.CollapsibleSidebarView;

import javax.swing.*;
import java.awt.*;
import java.util.List;

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
            EditedEventViewModel editedEventViewModel = new EditedEventViewModel();
            AddedEventViewModel addEventViewModel = new AddedEventViewModel();

            // --- Data Access & Factory ---
            EventAvailableDataAccessObject commonDao = new EventAvailableDataAccessObject(); // for available events
            TodaysEventDataAccessObject todaysEventDAO = new TodaysEventDataAccessObject();  // for added events
            CreateEventDataAccessInterface createAccess = commonDao;
            DeleteEventDataAccessInterf deleteAccess = commonDao;
            EditEventDataAccessInterf editAccess = commonDao;
            AddEventDataAccessInterf addEventAccess = todaysEventDAO;
            ReadAvailableEventDataAccessInterf availableInfoAccess = commonDao;
            InfoFactory infoFactory = new InfoFactory();

            // --- Create Event Use Case ---
            CreateEventOutputBoundary createEventPresenter = new CreateEventPresenter(
                    createdEventViewModel, availableEventViewModel, createAccess);
            CreateEventInputBoundary createEventInteractor = new CreateEventInteractor(
                    createAccess, createEventPresenter, infoFactory);
            CreateEventController createEventController = new CreateEventController(createEventInteractor);

            // --- Delete Event Use Case ---
            DeleteEventOutputBoundary deleteEventPresenter = new DeleteEventPresenter(
                    deletedEventViewModel, availableEventViewModel);
            DeleteEventInputBoundary deleteEventInteractor = new DeleteEventInteractor(
                    deleteAccess, deleteEventPresenter);
            DeleteEventController deleteEventController = new DeleteEventController(deleteEventInteractor);

            // --- Edit Event Use Case ---
            EditEventOutputBoundary editEventPresenter = new EditEventPresenter(
                    editedEventViewModel, availableEventViewModel);
            EditEventInputBoundary editEventInteractor = new EditEventInteractor(editAccess, editEventPresenter);
            EditEventController editEventController = new EditEventController(editEventInteractor);

            // --- Add Event Use Case ---
            AddEventOutputBoundary addEventPresenter = new AddEventPresenter(addEventViewModel);
            AddEventInputBoundary addEventInteractor = new AddEventInteractor(
                    addEventAccess,         // ✅ today’s events
                    availableInfoAccess,   // ✅ available info
                    addEventPresenter
            );
            AddEventController addEventController = new AddEventController(addEventInteractor);

            // --- AddEventView ---
            AddEventView addEventView = new AddEventView(addEventViewModel, addEventController);

            // 初始化 availableNames 到 AddEventState
            List<String> names = commonDao.getAllEvents().stream()
                    .map(info -> info.getName())
                    .toList();
            AddedEventState state = addEventViewModel.getState();
            state.setAvailableNames(names);
            addEventViewModel.setState(state);

            // --- CreateEventView ---
            CreateEventView createEventView = new CreateEventView(
                    createdEventViewModel,
                    addEventViewModel,
                    commonDao  // EventAvailableDataAccessObject
            );createEventView.setCreateEventController(createEventController);

            // --- AvailableEventView ---
            AvailableEventView availableEventView = new AvailableEventView(
                    availableEventViewModel,
                    deleteEventController,
                    deletedEventViewModel,
                    createdEventViewModel,
                    editEventController,
                    editedEventViewModel
            );

            // --- Upper Panel Layout (left top + left bottom) ---
            JSplitPane verticalSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, createEventView, addEventView);
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
            bottomBox.setPreferredSize(new Dimension(800, 300));
            bottomBox.setBackground(Color.GRAY);

            JPanel centerPanel = new JPanel(new BorderLayout());
            centerPanel.add(topCenterRow, BorderLayout.CENTER);
            centerPanel.add(bottomBox, BorderLayout.SOUTH);

            // --- Sidebar Panel ---
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

            // --- Wrap sidebar + centerPanel ---
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




