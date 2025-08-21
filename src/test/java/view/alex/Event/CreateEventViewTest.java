package view.alex.Event;

import data_access.alex.EventAvailableDataAccessObject;
import entity.alex.EventAvailable.EventAvailableFactory;
import entity.alex.EventAvailable.EventAvailableFactoryInterf;
import entity.info.InfoInterf;
import entity.info.InfoFactoryInterf;
import entity.info.InfoFactory;
import interface_adapter.alex.event_related.add_event.AddedEventState;
import interface_adapter.alex.event_related.add_event.AddedEventViewModel;
import interface_adapter.alex.event_related.create_event.CreatedEventState;
import interface_adapter.alex.event_related.create_event.CreatedEventViewModel;
import interface_adapter.alex.event_related.create_event.CreateEventController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.alex.event_related.create_event.CreateEventInputBoundary;
import use_case.alex.event_related.create_event.CreateEventInputData;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test for CreateEventView without category dependencies.
 */
public class CreateEventViewTest {

    private CreatedEventViewModel createdEventViewModel;
    private AddedEventViewModel addedEventViewModel;
    private EventAvailableDataAccessObject availableDAO;
    private CreateEventView view;

    @BeforeEach
    void setUp() {
        createdEventViewModel = new CreatedEventViewModel();
        addedEventViewModel = new AddedEventViewModel();

        // 使用 InfoFactory 实例化 DAO
        InfoFactoryInterf factory = new InfoFactory();
        EventAvailableFactoryInterf eventAvailableFactory = new EventAvailableFactory();
        availableDAO = new EventAvailableDataAccessObject(eventAvailableFactory);

        // 由于 CreateEventView 构造函数原本需要 CategoryGateway，这里传 null
        view = new CreateEventView(createdEventViewModel, addedEventViewModel, availableDAO, null);
    }

    @Test
    void testViewName() {
        assertEquals("New Available Event", view.getViewName());
    }

    @Test
    void testOnViewModelUpdatedSyncsFields() {
        CreatedEventState state = new CreatedEventState();
        state.setName("My Event");
        state.setDescription("Some description");
        createdEventViewModel.setState(state);

        view.onViewModelUpdated();

        JTextField nameField = (JTextField) getPrivateField(view, "nameInputField");
        JTextArea descArea = (JTextArea) getPrivateField(view, "descriptionInputArea");

        assertEquals("My Event", nameField.getText());
        assertEquals("Some description", descArea.getText());
    }

    @Test
    void testPropertyChangeUpdatesAddEventViewModel() {
        // 往 DAO 存一个事件
        InfoInterf info = new InfoFactory().create("1", "event1", "desc");
        availableDAO.save(info);

        CreatedEventState state = new CreatedEventState();
        state.setNameError(""); // 没有错误
        PropertyChangeEvent evt = new PropertyChangeEvent(this,
                CreatedEventViewModel.CREATED_EVENT_STATE_PROPERTY, null, state);

        view.propertyChange(evt);

        AddedEventState addState = addedEventViewModel.getState();
        assertTrue(addState.getAvailableNames().contains("event1"));
    }

    @Test
    void testPropertyChangeWithErrorMessage() {
        CreatedEventState state = new CreatedEventState();
        state.setNameError("Name required");

        PropertyChangeEvent evt = new PropertyChangeEvent(this,
                CreatedEventViewModel.CREATED_EVENT_STATE_PROPERTY, null, state);

        // 不会抛异常，只是弹窗
        view.propertyChange(evt);
    }

    @Test
    void testSetCreateEventControllerAndTriggerAction() {
        final boolean[] executed = {false};
        view.setCreateEventController(new CreateEventController(
                new CreateEventInputBoundary() {
                    @Override
                    public void execute(CreateEventInputData data) {
                        executed[0] = true;
                        assertNotNull(data.getId());
                        assertEquals(LocalDate.now(), data.getCreatedDate());
                    }
                }
        ));

        JButton createButton = (JButton) getPrivateField(view, "create");
        for (var l : createButton.getActionListeners()) {
            l.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "click"));
        }
        assertTrue(executed[0]);
    }

    // 辅助方法：反射访问私有字段
    private Object getPrivateField(Object target, String fieldName) {
        try {
            var f = target.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            return f.get(target);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
