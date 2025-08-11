package app.eventPage;

import entity.Alex.DailyEventLog.DailyEventLogFactory;
import entity.Alex.DailyEventLog.DailyEventLogFactoryInterf;
import entity.Alex.Event.EventFactory;
import entity.Alex.Event.EventFactoryInterf;
import entity.Alex.EventAvailable.EventAvailableFactory;
import entity.Alex.EventAvailable.EventAvailableFactoryInterf;
import entity.Alex.EventAvailable.EventAvailableInterf;
import entity.BeginAndDueDates.BeginAndDueDatesFactory;
import entity.BeginAndDueDates.BeginAndDueDatesFactoryInterf;
import interface_adapter.alex.event_related.available_event_module.delete_event.DeleteEventController;
import interface_adapter.alex.event_related.available_event_module.delete_event.DeleteEventPresenter;
import interface_adapter.alex.event_related.available_event_module.delete_event.DeletedEventViewModel;
import interface_adapter.alex.event_related.available_event_module.edit_event.EditEventController;
import interface_adapter.alex.event_related.available_event_module.edit_event.EditEventPresenter;
import interface_adapter.alex.event_related.available_event_module.edit_event.EditedEventViewModel;
import interface_adapter.alex.event_related.add_event.AddEventController;
import interface_adapter.alex.event_related.add_event.AddEventPresenter;
import interface_adapter.alex.event_related.add_event.AddedEventState;
import interface_adapter.alex.event_related.add_event.AddedEventViewModel;
import interface_adapter.alex.event_related.available_event_module.available_event.AvailableEventViewModel;
import interface_adapter.alex.event_related.create_event.CreateEventController;
import interface_adapter.alex.event_related.create_event.CreateEventPresenter;
import interface_adapter.alex.event_related.create_event.CreatedEventViewModel;
import interface_adapter.alex.event_related.todays_events_module.delete_todays_event.DeleteTodaysEventController;
import interface_adapter.alex.event_related.todays_events_module.delete_todays_event.DeleteTodaysEventPresenter;
import interface_adapter.alex.event_related.todays_events_module.delete_todays_event.DeleteTodaysEventViewModel;
import interface_adapter.alex.event_related.todays_events_module.edit_todays_event.EditTodaysEventController;
import interface_adapter.alex.event_related.todays_events_module.edit_todays_event.EditTodaysEventPresenter;
import interface_adapter.alex.event_related.todays_events_module.edit_todays_event.EditTodaysEventViewModel;
import interface_adapter.alex.event_related.todays_events_module.todays_events.TodaysEventsViewModel;

import use_case.alex.event_related.add_event.*;
import use_case.alex.event_related.create_event.*;
import use_case.alex.event_related.avaliable_events_module.delete_event.*;
import use_case.alex.event_related.avaliable_events_module.edit_event.*;
import use_case.alex.event_related.todays_events_module.delete_todays_event.*;
import use_case.alex.event_related.todays_events_module.edit_todays_event.*;

import data_access.EventAvailableDataAccessObject;
import data_access.TodaysEventDataAccessObject;
import entity.info.InfoFactory;

import view.Alex.Event.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class EventPageBuilder {

    public JPanel build() {

        // --- factories ---
        EventFactoryInterf eventFactory = new EventFactory();
        BeginAndDueDatesFactoryInterf datesFactory = new BeginAndDueDatesFactory();


        // --- ViewModels ---
        CreatedEventViewModel createdEventViewModel = new CreatedEventViewModel();
        AvailableEventViewModel availableEventViewModel = new AvailableEventViewModel();
        DeletedEventViewModel deletedEventViewModel = new DeletedEventViewModel();
        EditedEventViewModel editedEventViewModel = new EditedEventViewModel();
        AddedEventViewModel addEventViewModel = new AddedEventViewModel();
        TodaysEventsViewModel todaysEventsViewModel = new TodaysEventsViewModel();
        DeleteTodaysEventViewModel deleteTodaysEventViewModel = new DeleteTodaysEventViewModel();
        EditTodaysEventViewModel editTodaysEventViewModel = new EditTodaysEventViewModel();

        // --- Data Access & Factory ---
        EventAvailableFactoryInterf eventAvailableFactory = new EventAvailableFactory();
        EventAvailableDataAccessObject commonDao = new EventAvailableDataAccessObject(eventAvailableFactory);
        DailyEventLogFactoryInterf dailyEventLogFactory = new DailyEventLogFactory(); // 假设你实现了这个类
        TodaysEventDataAccessObject todaysEventDAO = new TodaysEventDataAccessObject(dailyEventLogFactory);

        InfoFactory infoFactory = new InfoFactory();

        // --- Use Case Wiring ---
        CreateEventOutputBoundary createEventPresenter = new CreateEventPresenter(createdEventViewModel, availableEventViewModel, commonDao);
        CreateEventInputBoundary createEventInteractor = new CreateEventInteractor(commonDao, createEventPresenter, infoFactory);
        CreateEventController createEventController = new CreateEventController(createEventInteractor);

        DeleteEventOutputBoundary deleteEventPresenter = new DeleteEventPresenter(deletedEventViewModel, availableEventViewModel, addEventViewModel);
        DeleteEventInputBoundary deleteEventInteractor = new DeleteEventInteractor(commonDao, deleteEventPresenter);
        DeleteEventController deleteEventController = new DeleteEventController(deleteEventInteractor);

        EditEventOutputBoundary editEventPresenter = new EditEventPresenter(editedEventViewModel, availableEventViewModel);
        EditEventInputBoundary editEventInteractor = new EditEventInteractor(commonDao, editEventPresenter);
        EditEventController editEventController = new EditEventController(editEventInteractor);

        AddEventOutputBoundary addEventPresenter = new AddEventPresenter(addEventViewModel, todaysEventsViewModel, todaysEventDAO);
        AddEventInputBoundary addEventInteractor =
                new AddEventInteractor(todaysEventDAO, commonDao, addEventPresenter, eventFactory, datesFactory);
        AddEventController addEventController = new AddEventController(addEventInteractor);

        DeleteTodaysEventOutputBoundary delTodayPresenter = new DeleteTodaysEventPresenter(deleteTodaysEventViewModel, todaysEventsViewModel, addEventViewModel);
        DeleteTodaysEventInputBoundary delTodayInteractor = new DeleteTodaysEventInteractor(todaysEventDAO, delTodayPresenter);
        DeleteTodaysEventController deleteTodaysEventController = new DeleteTodaysEventController(delTodayInteractor);

        EditTodaysEventOutputBoundary editTodayPresenter = new EditTodaysEventPresenter(editTodaysEventViewModel, todaysEventsViewModel);
        EditTodaysEventInputBoundary editTodayInteractor = new EditTodaysEventInteractor(todaysEventDAO, editTodayPresenter);
        EditTodaysEventController editTodaysEventController = new EditTodaysEventController(editTodayInteractor);

        // --- Views ---
        AddEventView addEventView = new AddEventView(addEventViewModel, addEventController);
        TodaysEventsView todaysEventsView = new TodaysEventsView(
                todaysEventsViewModel, addEventController, addEventViewModel,
                deleteTodaysEventController, editTodaysEventController, editTodaysEventViewModel
        );

        // 初始化 addEventViewModel 的下拉框数据
        List<String> names = commonDao.getAllEvents().stream().map(e -> e.getName()).toList();
        AddedEventState state = addEventViewModel.getState();
        state.setAvailableNames(names);
        addEventViewModel.setState(state);

        CreateEventView createEventView = new CreateEventView(createdEventViewModel, addEventViewModel, commonDao);
        createEventView.setCreateEventController(createEventController);

        AvailableEventView availableEventView = new AvailableEventView(
                availableEventViewModel, deleteEventController, deletedEventViewModel,
                createdEventViewModel, editEventController, editedEventViewModel
        );

        // --- Layout Panels ---
        JSplitPane verticalSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, createEventView, addEventView);
        verticalSplit.setResizeWeight(0.5);
        verticalSplit.setDividerSize(2);
        verticalSplit.setEnabled(false);

        JPanel upperRightPanel = new JPanel(new BorderLayout());
        upperRightPanel.add(todaysEventsView, BorderLayout.CENTER);
        upperRightPanel.setBackground(new Color(240, 240, 255));

        JPanel topCenterRow = new JPanel(new GridLayout(1, 2));
        topCenterRow.add(verticalSplit);
        topCenterRow.add(upperRightPanel);

        JPanel bottomBox = new JPanel(new BorderLayout());
        bottomBox.add(availableEventView, BorderLayout.CENTER);
        bottomBox.setPreferredSize(new Dimension(800, 300));
        bottomBox.setBackground(Color.GRAY);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(topCenterRow, BorderLayout.CENTER);
        centerPanel.add(bottomBox, BorderLayout.SOUTH);


        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setPreferredSize(new Dimension(300, 0));
        rightPanel.setBackground(Color.WHITE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(rightPanel, BorderLayout.EAST);

        return mainPanel;
    }
}


