package view.alex.Event;

import data_access.alex.EventAvailableDataAccessObject;
import data_access.alex.TodaysEventDataAccessObject;
import entity.BeginAndDueDates.BeginAndDueDates;
import entity.BeginAndDueDates.BeginAndDueDatesFactory;
import entity.alex.Event.EventFactory;
import entity.alex.Event.EventFactoryInterf;
import entity.alex.Event.EventInterf;
import entity.info.InfoFactory;
import entity.info.InfoFactoryInterf;
import entity.info.InfoInterf;
import interface_adapter.alex.event_related.add_event.AddEventController;
import interface_adapter.alex.event_related.add_event.AddedEventState;
import interface_adapter.alex.event_related.add_event.AddedEventViewModel;
import interface_adapter.alex.event_related.todays_events_module.delete_todays_event.DeleteTodaysEventController;
import interface_adapter.alex.event_related.todays_events_module.edit_todays_event.EditTodaysEventController;
import interface_adapter.alex.event_related.todays_events_module.edit_todays_event.EditTodaysEventState;
import interface_adapter.alex.event_related.todays_events_module.edit_todays_event.EditTodaysEventViewModel;
import interface_adapter.alex.event_related.todays_events_module.todays_events.TodaysEventsState;
import interface_adapter.alex.event_related.todays_events_module.todays_events.TodaysEventsViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.alex.event_related.add_event.*;
import use_case.alex.event_related.todays_events_module.delete_todays_event.*;
import use_case.alex.event_related.todays_events_module.edit_todays_event.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class TodaysEventViewTest {

    private TodaysEventDataAccessObject todaysDAO;
    private EventAvailableDataAccessObject availableDAO;

    private TodaysEventsViewModel todaysEventsViewModel;
    private AddedEventViewModel addedEventViewModel;
    private EditTodaysEventViewModel editTodaysEventViewModel;

    private TodaysEventsView view;
    private EventInterf sampleEvent;

    @BeforeEach
    void setUp() {
        // ==== Factories ====
        InfoFactoryInterf infoFactory = new InfoFactory();
        EventFactoryInterf eventFactory = new EventFactory();
        BeginAndDueDatesFactory datesFactory = new BeginAndDueDatesFactory();

        // ==== DAO ====
        todaysDAO = new TodaysEventDataAccessObject(new entity.alex.DailyEventLog.DailyEventLogFactory());
        availableDAO = new EventAvailableDataAccessObject(new entity.alex.EventAvailable.EventAvailableFactory());

        // ==== ViewModels ====
        todaysEventsViewModel = new TodaysEventsViewModel();
        addedEventViewModel = new AddedEventViewModel();
        editTodaysEventViewModel = new EditTodaysEventViewModel();

        // ==== Presenters & Interactors & Controllers ====
        AddEventInteractor addEventInteractor = new AddEventInteractor(
                todaysDAO,
                availableDAO,
                new AddEventOutputBoundary() {
                    @Override
                    public void prepareSuccessView(AddEventOutputData outputData) {
                        todaysEventsViewModel.setState(new TodaysEventsState());
                    }
                    @Override
                    public void prepareFailView(String error) {
                        // no-op
                    }
                },
                eventFactory,
                datesFactory
        );
        AddEventController addEventController = new AddEventController(addEventInteractor);

        DeleteTodaysEventInteractor deleteInteractor = new DeleteTodaysEventInteractor(
                todaysDAO,
                new DeleteTodaysEventOutputBoundary() {
                    @Override
                    public void prepareSuccessView(DeleteTodaysEventOutputData outputData) {
                        todaysEventsViewModel.setState(new TodaysEventsState());
                    }
                    @Override
                    public void prepareFailView(DeleteTodaysEventOutputData outputData) {
                        // no-op
                    }
                }
        );
        DeleteTodaysEventController deleteController = new DeleteTodaysEventController(deleteInteractor);

        EditTodaysEventInteractor editInteractor = new EditTodaysEventInteractor(
                todaysDAO,
                new EditTodaysEventOutputBoundary() {
                    @Override
                    public void prepareSuccessView(EditTodaysEventOutputData outputData) {
                        editTodaysEventViewModel.setState(new EditTodaysEventState());
                    }
                    @Override
                    public void prepareFailView(EditTodaysEventOutputData outputData) {
                        editTodaysEventViewModel.setState(new EditTodaysEventState());
                    }
                }
        );
        EditTodaysEventController editController = new EditTodaysEventController(editInteractor);

        // ==== View ====
        view = new TodaysEventsView(
                todaysEventsViewModel,
                addEventController,
                addedEventViewModel,
                deleteController,
                editController,
                editTodaysEventViewModel
        );

        // ==== 构造一个 sample Event ====
        InfoInterf info = infoFactory.create("e1", "SampleEvent", "desc");
        BeginAndDueDates dates = new BeginAndDueDates(LocalDate.now(), LocalDate.now().plusDays(1));
        sampleEvent = eventFactory.createEvent(info, dates);

        todaysDAO.save(sampleEvent);
    }

    @Test
    void testEmptyList() {
        todaysDAO.clearAll();
        todaysEventsViewModel.setState(new TodaysEventsState());
        todaysEventsViewModel.firePropertyChanged("state");

        JPanel listPanel = getListPanel();
        assertTrue(((JLabel) listPanel.getComponent(0)).getText().contains("No available events"));
    }

    @Test
    void testForceRefresh() {
        view.forceRefresh();
    }

    @Test
    void testAddedEventPropertyChange() {
        AddedEventState state = new AddedEventState();
        state.setSelectedName("NewEvent");
        addedEventViewModel.setState(state);
        addedEventViewModel.firePropertyChanged("addedEvent");
    }

    @Test
    void testEditEventPropertyChange() {
        EditTodaysEventState state = new EditTodaysEventState();
        state.setEditError("Some error");
        editTodaysEventViewModel.setState(state);
        editTodaysEventViewModel.firePropertyChanged("state");
    }

    @Test
    void testRefreshEventListAndClickButtons() {
        TodaysEventsState state = new TodaysEventsState();
        state.setTodaysEvents(Collections.singletonList(sampleEvent));
        todaysEventsViewModel.setState(state);
        todaysEventsViewModel.firePropertyChanged("state");

        JPanel row = (JPanel) getListPanel().getComponent(0);
        assertEquals("SampleEvent", ((JLabel) row.getComponent(0)).getText());

        // 点击 delete
        JButton deleteBtn = (JButton) row.getComponent(4);
        deleteBtn.getActionListeners()[0].actionPerformed(
                new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "delete"));
        assertFalse(todaysDAO.existsById("e1"));

        // 重新加回事件再测试 edit
        todaysDAO.save(sampleEvent);
        state.setTodaysEvents(Collections.singletonList(sampleEvent));
        todaysEventsViewModel.setState(state);
        todaysEventsViewModel.firePropertyChanged("state");

        row = (JPanel) getListPanel().getComponent(0);
        JButton editBtn = (JButton) row.getComponent(3);
        editBtn.getActionListeners()[0].actionPerformed(
                new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "edit"));
        assertTrue(todaysDAO.existsById("e1"));
    }

    private JPanel getListPanel() {
        try {
            var f = TodaysEventsView.class.getDeclaredField("todaysEventsListPanel");
            f.setAccessible(true);
            return (JPanel) f.get(view);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
