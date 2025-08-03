package app;

import app.Notification_related.NotificationSystemRunner;

class MainApp {
    public static void main(String[] args) {
        // 切换运行不同模块的页面：
        new EventPageRunner().run();
        //new WellnessLogPageRunner().run();
        //new SettingsPageRunner().run();
        NotificationSystemRunner runner = new NotificationSystemRunner();
        runner.run();
    }
}

//class MainApp {
//    public static void main(String[] args) {
//        EventPageRunner eventPage = new EventPageRunner();
//        eventPage.run();
//    }
//}

//package app;
//
//import interface_adapter.Alex.Event_related.available_event_module.delete_event.DeleteEventController;
//import interface_adapter.Alex.Event_related.available_event_module.delete_event.DeleteEventPresenter;
//import interface_adapter.Alex.Event_related.available_event_module.delete_event.DeletedEventViewModel;
//import interface_adapter.Alex.Event_related.available_event_module.edit_event.EditEventController;
//import interface_adapter.Alex.Event_related.available_event_module.edit_event.EditEventPresenter;
//import interface_adapter.Alex.Event_related.available_event_module.edit_event.EditedEventViewModel;
//import interface_adapter.Alex.Event_related.add_event.AddEventController;
//import interface_adapter.Alex.Event_related.add_event.AddEventPresenter;
//import interface_adapter.Alex.Event_related.add_event.AddedEventState;
//import interface_adapter.Alex.Event_related.add_event.AddedEventViewModel;
//import interface_adapter.Alex.Event_related.available_event_module.available_event.AvailableEventViewModel;
//import interface_adapter.Alex.Event_related.create_event.CreateEventController;
//import interface_adapter.Alex.Event_related.create_event.CreateEventPresenter;
//import interface_adapter.Alex.Event_related.create_event.CreatedEventViewModel;
//import interface_adapter.Alex.Event_related.todays_events_module.delete_todays_event.DeleteTodaysEventController;
//import interface_adapter.Alex.Event_related.todays_events_module.delete_todays_event.DeleteTodaysEventPresenter;
//import interface_adapter.Alex.Event_related.todays_events_module.delete_todays_event.DeleteTodaysEventViewModel;
//
//import interface_adapter.Alex.Event_related.todays_events_module.edit_todays_event.EditTodaysEventController;
//import interface_adapter.Alex.Event_related.todays_events_module.edit_todays_event.EditTodaysEventPresenter;
//import interface_adapter.Alex.Event_related.todays_events_module.edit_todays_event.EditTodaysEventViewModel;
//import interface_adapter.Alex.Event_related.todays_events_module.todays_events.TodaysEventsViewModel;
//import use_case.Alex.Event_related.add_event.*;
//import use_case.Alex.Event_related.create_event.CreateEventDataAccessInterface;
//import use_case.Alex.Event_related.create_event.CreateEventInputBoundary;
//import use_case.Alex.Event_related.create_event.CreateEventInteractor;
//import use_case.Alex.Event_related.create_event.CreateEventOutputBoundary;
//import use_case.Alex.Event_related.avaliable_events_module.delete_event.DeleteEventDataAccessInterf;
//import use_case.Alex.Event_related.avaliable_events_module.delete_event.DeleteEventInputBoundary;
//import use_case.Alex.Event_related.avaliable_events_module.delete_event.DeleteEventInteractor;
//import use_case.Alex.Event_related.avaliable_events_module.delete_event.DeleteEventOutputBoundary;
//import use_case.Alex.Event_related.avaliable_events_module.edit_event.EditEventDataAccessInterf;
//import use_case.Alex.Event_related.avaliable_events_module.edit_event.EditEventInputBoundary;
//import use_case.Alex.Event_related.avaliable_events_module.edit_event.EditEventInteractor;
//import use_case.Alex.Event_related.avaliable_events_module.edit_event.EditEventOutputBoundary;
//
//import data_access.EventAvailableDataAccessObject;
//import data_access.TodaysEventDataAccessObject;
//
//import entity.Info.InfoFactory;
//import use_case.Alex.Event_related.todays_events_module.delete_todays_event.DeleteTodaysEventDataAccessInterf;
//import use_case.Alex.Event_related.todays_events_module.delete_todays_event.DeleteTodaysEventInputBoundary;
//import use_case.Alex.Event_related.todays_events_module.delete_todays_event.DeleteTodaysEventInteractor;
//import use_case.Alex.Event_related.todays_events_module.delete_todays_event.DeleteTodaysEventOutputBoundary;
//import use_case.Alex.Event_related.todays_events_module.edit_todays_event.EditTodaysEventDataAccessInterf;
//import use_case.Alex.Event_related.todays_events_module.edit_todays_event.EditTodaysEventInputBoundary;
//import use_case.Alex.Event_related.todays_events_module.edit_todays_event.EditTodaysEventInteractor;
//import use_case.Alex.Event_related.todays_events_module.edit_todays_event.EditTodaysEventOutputBoundary;
//import view.Alex.Event.AddEventView;
//import view.Alex.Event.CreateEventView;
//import view.Alex.Event.AvailableEventView;
//import view.Alex.Event.TodaysEventsView;
//import view.CollapsibleSidebarView;
//
//import javax.swing.*;
//import java.awt.*;
//import java.util.List;
//
//class CreateEventTestApp {
//
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            JFrame frame = new JFrame("MindTrack");
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            frame.setSize(1200, 700);
//
//            // --- ViewModels ---
//            CreatedEventViewModel createdEventViewModel = new CreatedEventViewModel();
//            AvailableEventViewModel availableEventViewModel = new AvailableEventViewModel();
//            DeletedEventViewModel deletedEventViewModel = new DeletedEventViewModel();
//            EditedEventViewModel editedEventViewModel = new EditedEventViewModel();
//            AddedEventViewModel addEventViewModel = new AddedEventViewModel();
//            TodaysEventsViewModel todaysEventsViewModel = new TodaysEventsViewModel();
//            DeleteTodaysEventViewModel deleteTodaysEventViewModel = new DeleteTodaysEventViewModel();
//            EditTodaysEventViewModel editTodaysEventViewModel = new EditTodaysEventViewModel();
//
//
//            // --- Data Access & Factory ---
//            EventAvailableDataAccessObject commonDao = new EventAvailableDataAccessObject(); // for available events
//            TodaysEventDataAccessObject todaysEventDAO = new TodaysEventDataAccessObject();  // for added events
//            CreateEventDataAccessInterface createAccess = commonDao;
//            DeleteEventDataAccessInterf deleteAccess = commonDao;
//            EditEventDataAccessInterf editAccess = commonDao;
//            AddEventDataAccessInterf addEventAccess = todaysEventDAO;
//            DeleteTodaysEventDataAccessInterf deleteTodaysEventAccess = todaysEventDAO;
//            ReadAvailableEventDataAccessInterf availableInfoAccess = commonDao;
//            EditTodaysEventDataAccessInterf editTodaysEventDataAccess = todaysEventDAO;
//            InfoFactory infoFactory = new InfoFactory();
//
//            // --- Create Event Use Case ---
//            CreateEventOutputBoundary createEventPresenter = new CreateEventPresenter(
//                    createdEventViewModel, availableEventViewModel, createAccess);
//            CreateEventInputBoundary createEventInteractor = new CreateEventInteractor(
//                    createAccess, createEventPresenter, infoFactory);
//            CreateEventController createEventController = new CreateEventController(createEventInteractor);
//
//            // --- Delete Event Use Case ---
//            DeleteEventOutputBoundary deleteEventPresenter = new DeleteEventPresenter(
//                    deletedEventViewModel,
//                    availableEventViewModel,
//                    addEventViewModel  // ✅ 新增参数
//            );
//            DeleteEventInputBoundary deleteEventInteractor = new DeleteEventInteractor(
//                    deleteAccess, deleteEventPresenter);
//            DeleteEventController deleteEventController = new DeleteEventController(deleteEventInteractor);
//
//            // --- Edit Event Use Case ---
//            EditEventOutputBoundary editEventPresenter = new EditEventPresenter(
//                    editedEventViewModel, availableEventViewModel);
//            EditEventInputBoundary editEventInteractor = new EditEventInteractor(editAccess, editEventPresenter);
//            EditEventController editEventController = new EditEventController(editEventInteractor);
//
//            // --- Add Event Use Case ---
//            AddEventOutputBoundary addEventPresenter = new AddEventPresenter(
//                    addEventViewModel,
//                    todaysEventsViewModel,
//                    addEventAccess  // 实现了 AddEventDataAccessInterf 的 TodaysEventDataAccessObject
//            );
//            AddEventInputBoundary addEventInteractor = new AddEventInteractor(
//                    addEventAccess,         // ✅ today’s events
//                    availableInfoAccess,   // ✅ available info
//                    addEventPresenter
//            );
//            AddEventController addEventController = new AddEventController(addEventInteractor);
//
//            // --- Delete Today's Event Use Case ---
//            DeleteTodaysEventOutputBoundary presenter = new DeleteTodaysEventPresenter(
//                    deleteTodaysEventViewModel,
//                    todaysEventsViewModel,
//                    addEventViewModel
//            );
//
//            DeleteTodaysEventInputBoundary interactor = new DeleteTodaysEventInteractor(
//                    deleteTodaysEventAccess,
//                    presenter
//            );
//
//            DeleteTodaysEventController deleteTodaysEventController = new DeleteTodaysEventController(interactor);
//
//            // --- Edit Today's Event Use Case ---
//            EditTodaysEventOutputBoundary editPresenter = new EditTodaysEventPresenter(
//                    editTodaysEventViewModel,
//                    todaysEventsViewModel
//            );
//
//            EditTodaysEventInputBoundary editInteractor = new EditTodaysEventInteractor(
//                    editTodaysEventDataAccess, // ✅ 你需要提前构造这个 DAO（实现 EditTodaysEventDataAccessInterf）
//                    editPresenter
//            );
//
//            EditTodaysEventController editTodaysEventController = new EditTodaysEventController(editInteractor);
//
//
//            // --- AddEventView ---
//            AddEventView addEventView = new AddEventView(addEventViewModel, addEventController);
//            TodaysEventsView todaysEventsView = new TodaysEventsView(
//                    todaysEventsViewModel,
//                    addEventController,
//                    addEventViewModel,
//                    deleteTodaysEventController,
//                    editTodaysEventController,            // ✅ 新增 edit controller
//                    editTodaysEventViewModel              // ✅ 新增 edit view model
//            );
//
//
//            // 初始化 availableNames 到 AddEventState
//            List<String> names = commonDao.getAllEvents().stream()
//                    .map(info -> info.getName())
//                    .toList();
//            AddedEventState state = addEventViewModel.getState();
//            state.setAvailableNames(names);
//            addEventViewModel.setState(state);
//
//            // --- CreateEventView ---
//            CreateEventView createEventView = new CreateEventView(
//                    createdEventViewModel,
//                    addEventViewModel,
//                    commonDao  // EventAvailableDataAccessObject
//            );createEventView.setCreateEventController(createEventController);
//
//            // --- AvailableEventView ---
//            AvailableEventView availableEventView = new AvailableEventView(
//                    availableEventViewModel,
//                    deleteEventController,
//                    deletedEventViewModel,
//                    createdEventViewModel,
//                    editEventController,
//                    editedEventViewModel
//            );
//
//
//            // --- Upper Panel Layout (left top + left bottom) ---
//            JSplitPane verticalSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, createEventView, addEventView);
//            verticalSplit.setResizeWeight(0.5);
//            verticalSplit.setDividerSize(2);
//            verticalSplit.setEnabled(false);
//
//            // --- Top Center Row ---
//            JPanel upperRightPanel = new JPanel(new BorderLayout()); // 或任意你想要的布局
//            upperRightPanel.add(todaysEventsView, BorderLayout.CENTER);
//            ;
//            upperRightPanel.setBackground(new Color(240, 240, 255));
//            JPanel topCenterRow = new JPanel(new GridLayout(1, 2));
//            topCenterRow.add(verticalSplit);
//            topCenterRow.add(upperRightPanel);
//
//            // --- Bottom Content Area ---
//            JPanel bottomBox = new JPanel(new BorderLayout());
//            bottomBox.add(availableEventView, BorderLayout.NORTH);
//            bottomBox.setPreferredSize(new Dimension(800, 300));
//            bottomBox.setBackground(Color.GRAY);
//
//            JPanel centerPanel = new JPanel(new BorderLayout());
//            centerPanel.add(topCenterRow, BorderLayout.CENTER);
//            centerPanel.add(bottomBox, BorderLayout.SOUTH);
//
//            // --- Sidebar Panel ---
//            JPanel sidebarPanel = new JPanel();
//            sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
//            sidebarPanel.setBackground(new Color(60, 63, 65));
//            sidebarPanel.setPreferredSize(new Dimension(200, 700));
//            sidebarPanel.add(new JButton("📋 Tasks"));
//            sidebarPanel.add(new JButton("📆 Events"));
//            sidebarPanel.add(new JButton("🎯 Goals"));
//            sidebarPanel.add(new JButton("🧠 Wellness Log"));
//            sidebarPanel.add(new JButton("📊 Charts"));
//            sidebarPanel.add(new JButton("🤖 AI-Feedback & Analysis"));
//            sidebarPanel.add(new JButton("⚙️ Settings"));
//
//            // --- Wrap sidebar + centerPanel ---
//            CollapsibleSidebarView collapsibleCenter = new CollapsibleSidebarView(sidebarPanel, centerPanel);
//
//            // --- Right Panel (Details) ---
//            JPanel rightPanel = new JPanel(new BorderLayout());
//            rightPanel.setPreferredSize(new Dimension(300, 0));
//            rightPanel.setBackground(Color.WHITE);
//
//            // --- Final Frame Layout ---
//            JPanel mainPanel = new JPanel(new BorderLayout());
//            mainPanel.add(collapsibleCenter, BorderLayout.CENTER);
//            mainPanel.add(rightPanel, BorderLayout.EAST);
//
//            frame.setContentPane(mainPanel);
//            frame.setLocationRelativeTo(null);
//            frame.setVisible(true);
//        });
//    }
//}
